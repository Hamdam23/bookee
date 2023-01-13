package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.auth.RegistrationRequest;
import hamdam.bookee.APIs.image.ImageEntity;
import hamdam.bookee.APIs.image.ImageRepository;
import hamdam.bookee.APIs.user.helpers.SetUserImageRequest;
import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.APIs.user.helpers.*;
import hamdam.bookee.tools.exceptions.DuplicateResourceException;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import hamdam.bookee.tools.exceptions.pemission.LimitedPermissionException;
import hamdam.bookee.tools.exceptions.role.NoDefaultRoleException;
import hamdam.bookee.tools.exceptions.user.PasswordMismatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

import static hamdam.bookee.APIs.role.Permissions.MONITOR_USER;
import static hamdam.bookee.tools.utils.SecurityUtils.getUserByRequest;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository userRepository;
    private final AppRoleRepository roleRepository;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * > It takes a username, finds the user in the database, and returns a User object with the
     * username, password, and a list of permissions
     *
     * @param username The username of the user to load.
     * @return UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUserEntity user = userRepository.findAppUserByUsernameWithPermission(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        // TODO: 9/2/22 write mapper method/class for AppUser <-> User
        return new User(
                user.getUsername(),
                user.getPassword(),
                user.getRole().getPermissions().stream().map(
                        permission -> new SimpleGrantedAuthority(permission.name())
                ).collect(Collectors.toList())
        );
    }

    /**
     * If the username is not already taken, create a new user with the given username, password, and
     * image, and save it to the database
     *
     * @param request The request object that contains the user's information.
     */
    @Override
    public void saveUser(RegistrationRequest request) {
        if (existsWithUsername(request.getUsername())) {
            throw new DuplicateResourceException("User", "username", request.getUsername());
        }

        AppUserEntity appUserEntity = UserMappers.mapToAppUserEntity(request);

        // Checking if the imageId is not null, and if it is not,
        // it is setting the userImage to the imageId.
        if (request.getImageId() != null) {
            appUserEntity.setUserImage(imageRepository.findById(request.getImageId())
                    .orElseThrow(() -> new ResourceNotFoundException("Image", "id", request.getImageId())));
        }
        appUserEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        AppRoleEntity role = roleRepository.findFirstByIsDefaultIsTrue()
                .orElseThrow(NoDefaultRoleException::new);
        appUserEntity.setRole(role);
        userRepository.save(appUserEntity);
    }

    /**
     * It returns a page of AppUserResponseDTO objects.
     *
     * @param pageable This is a Spring Data interface that allows pagination to be specified.
     * @return A Page of AppUserResponseDTO
     */
    @Override
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAllByOrderByTimeStampDesc(pageable).map(UserMappers::mapToAppUserResponseDTO);
    }

    /**
     * It returns a user by id.
     *
     * @param id The id of the user you want to retrieve.
     * @return A user object
     */
    @Override
    public UserResponseDTO getUserById(Long id) {
        return UserMappers.mapToAppUserResponseDTO(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id))
        );
    }

    // Updating the user.
    @Override
    public UserResponseDTO updateUser(UserRequestDTO request, Long id) {
        AppUserEntity existingUser = getAppUserById(id);
        AppUserEntity requestingUser = getUserByRequest(userRepository);

        // Checking if the user has the permission to update the user.
        if (requestingUser.getRole().getPermissions().contains(MONITOR_USER)
                || requestingUser.getId().equals(id)) {
            // Checking if the imageId is not null, and if it is not,
            // it is setting the userImage to the imageId.
            if (request.getImageId() != null) {
                existingUser.setUserImage(imageRepository.findById(request.getImageId())
                        .orElseThrow(() -> new ResourceNotFoundException("Image", "id", request.getImageId()))
                );
            }

            // Checking if the username is not the same as the existing user's username,
            // if it is not, it is checking if the username exists in the database.
            // If it does, it is throwing a DuplicateResourceException.
            if (!request.getUsername().equals(existingUser.getUsername()) &&
                    userRepository.existsByUsername(request.getUsername())) {
                throw new DuplicateResourceException("username", "User", request.getUsername());
            }
            existingUser.setUsername(request.getUsername());
            existingUser.setName(request.getName());

            return UserMappers.mapToAppUserResponseDTO(userRepository.save(existingUser));
        } else {
            throw new LimitedPermissionException();
        }
    }

    /**
     * If the requesting user is the same as the user whose image is being changed, or if the
     * requesting user has the permission to monitor users, then the image is changed
     *
     * @param id The id of the user to set the image to.
     * @param imageDTO This is the object that is passed in from the frontend. It contains the id of
     * the image that the user wants to set as their profile picture.
     * @return The user's image is being returned.
     */
    @Override
    public UserResponseDTO setImageToUser(Long id, SetUserImageRequest imageDTO) {

        AppUserEntity requestingUser = getUserByRequest(userRepository);

        // Checking if the user has the permission to update the user.
        if (requestingUser.getId().equals(id) || requestingUser.getRole().getPermissions().contains(MONITOR_USER)) {
            // Checking if the imageId is not null, and if it is not,
            // it is setting the userImage to the imageId.
            ImageEntity imageEntity = imageRepository.findById(imageDTO.getImageId()).orElseThrow(()
                    -> new ResourceNotFoundException("Image", "id", imageDTO.getImageId())
            );
            AppUserEntity user = getAppUserById(id);
            user.setUserImage(imageEntity);
            return UserMappers.mapToAppUserResponseDTO(userRepository.save(user));
        } else {
            throw new LimitedPermissionException();
        }
    }

    /**
     * It sets the role of the user.
     *
     * @param id The id of the user to be updated.
     * @param roleDTO This is the request body that is sent to the endpoint.
     * @return AppUserResponseDTO
     */
    @Override
    public UserResponseDTO setRoleToUser(Long id, SetUseRoleRequest roleDTO) {
        AppUserEntity user = getAppUserById(id);
        AppRoleEntity appRoleEntity = roleRepository.findById(roleDTO.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleDTO.getRoleId()));
        user.setRole(appRoleEntity);
        return UserMappers.mapToAppUserResponseDTO(userRepository.save(user));
    }

    /**
     * If the user is not the requesting user and
     * the requesting user does not have the permission to monitor users, then throw an exception
     *
     * @param id The id of the user to delete
     */
    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }

        AppUserEntity requestingUser = getUserByRequest(userRepository);

        if (requestingUser.getId().equals(id) || requestingUser.getRole().getPermissions().contains(MONITOR_USER)) {
            userRepository.deleteById(id);
        } else {
            throw new LimitedPermissionException();
        }
    }

    /**
     * If the user is not the same as the requesting user, throw an exception
     *
     * @param passwordDTO The request body that contains the old password, new password, and confirmed
     * password.
     * @param id The id of the user whose password is being updated.
     * @return A user object
     */
    @Override
    public UserResponseDTO updatePassword(PasswordUpdateRequest passwordDTO, Long id) {
        AppUserEntity user = getAppUserById(id);
        AppUserEntity requestingUser = getUserByRequest(userRepository);

        // The above code is changing the password of a user.
        if (requestingUser.getId().equals(id)) {
            String oldPassword = passwordDTO.getOldPassword();
            String newPassword = passwordDTO.getNewPassword();
            String confirmedPassword = passwordDTO.getConfirmNewPassword();

            // Checking if the new password and the confirmed password are the same.
            // If they are not, it is throwing a PasswordMismatchException.
            if (!newPassword.equals(confirmedPassword)) {
                throw new PasswordMismatchException(newPassword, confirmedPassword);
            }
            // Checking if the new password and the old password are the same.
            // If they are, it is throwing a DuplicateResourceException.
            if (oldPassword.equals(newPassword)) {
                throw new DuplicateResourceException("passwords", oldPassword, newPassword);
            }
            // Checking if the old password matches the user's password.
            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                throw new PasswordMismatchException(oldPassword, "user's password");
            }

            user.setPassword(passwordEncoder.encode(newPassword));

            return UserMappers.mapToAppUserResponseDTO(userRepository.save(user));
        } else {
            throw new LimitedPermissionException();
        }
    }

    /**
     * > This function checks if a user exists with the given username
     *
     * @param username The username of the user to check for.
     * @return A boolean value.
     */
    @Override
    public boolean existsWithUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * If the user is found, return the user, otherwise throw an exception
     *
     * @param username The username of the user to retrieve
     * @param withPermissions This is a boolean value that determines whether or not to return the
     * user's permissions.
     */
    @Override
    public AppUserEntity getUserByUsername(String username, boolean withPermissions) {
        Optional<AppUserEntity> optional;
        // Checking if the user has permissions and then returning the user.
        if (withPermissions) {
            optional = userRepository.findAppUserByUsernameWithPermission(username);
        } else {
            optional = userRepository.findAppUserByUsername(username);
        }
        return optional.orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    /**
     * If the user exists, return the user, otherwise throw an exception.
     *
     * @param id The id of the user to be retrieved.
     * @return The userRepository.findById(id) is being returned.
     */
    AppUserEntity getAppUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }
}
