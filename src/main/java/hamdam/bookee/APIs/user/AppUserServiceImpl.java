package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.auth.RegistrationRequest;
import hamdam.bookee.APIs.image.ImageEntity;
import hamdam.bookee.APIs.image.ImageRepository;
import hamdam.bookee.APIs.image.UserImageDTO;
import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.APIs.user.helpers.AppUserRequestDTO;
import hamdam.bookee.APIs.user.helpers.AppUserResponseDTO;
import hamdam.bookee.APIs.user.helpers.SetUserPasswordDTO;
import hamdam.bookee.APIs.user.helpers.SetUserRoleDTO;
import hamdam.bookee.tools.exceptions.ApiResponse;
import hamdam.bookee.tools.exceptions.DuplicateResourceException;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import hamdam.bookee.tools.exceptions.pemission.LimitedPermissionException;
import hamdam.bookee.tools.exceptions.role.NoDefaultRoleException;
import hamdam.bookee.tools.exceptions.user.PasswordMismatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static hamdam.bookee.APIs.role.Permissions.MONITOR_USER;
import static hamdam.bookee.tools.token.GetUserByToken.getUserByRequest;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository userRepository;
    private final AppRoleRepository roleRepository;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUserEntity user = userRepository.findAppUserByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        // TODO: 9/2/22 write mapper method/class for AppUser <-> User
        return new User(
                user.getUserName(),
                user.getPassword(),
                user.getRole().getPermissions().stream().map(
                        permission -> new SimpleGrantedAuthority(permission.name())
                ).collect(Collectors.toList())
        );
    }

    @Override
    public void saveUser(RegistrationRequest request) {
        if (existsWithUsername(request.getUsername())) {
            throw new DuplicateResourceException("username");
        }
        AppUserEntity appUserEntity = new AppUserEntity(request,
                imageRepository.findById(request.getImageId())
                        .orElseThrow(() -> new ResourceNotFoundException("Image", "id", request.getImageId())));

        appUserEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        AppRoleEntity role = roleRepository.findFirstByIsDefaultIsTrue()
                .orElseThrow(NoDefaultRoleException::new);
        appUserEntity.setRole(role);
        userRepository.save(appUserEntity);
    }

    @Override
    public Page<AppUserResponseDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAllByOrderByTimeStampDesc(pageable).map(AppUserResponseDTO::new);
    }

    @Override
    public AppUserResponseDTO getUserById(Long id) {
        return new AppUserResponseDTO(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id))
        );
    }

    @Override
    public AppUserResponseDTO updateUser(AppUserRequestDTO request, Long id) {
        AppUserEntity existingUser = getAppUserById(id);
        AppUserEntity currentUser = getUserByRequest(userRepository);

        if (currentUser.getRole().getPermissions().contains(MONITOR_USER)
                || currentUser.getId().equals(id)) {
            if (request.getImageId() != null) {
                existingUser.setUserImage(imageRepository.findById(request.getImageId())
                        .orElseThrow(() -> new ResourceNotFoundException("Image", "id", request.getImageId()))
                );
            }

            if (!request.getUsername().equals(existingUser.getUserName()) &&
                    userRepository.existsByUserName(request.getUsername())) {
                throw new DuplicateResourceException("username", "User", request.getUsername());
            }
            existingUser.setUserName(request.getUsername());
            existingUser.setName(request.getName());

            return new AppUserResponseDTO(userRepository.save(existingUser));
        } else {
            throw new LimitedPermissionException();
        }
    }

    @Override
    public AppUserResponseDTO setImageToUser(Long id, UserImageDTO imageDTO) {

        AppUserEntity currentUser = getUserByRequest(userRepository);

        if (currentUser.getId().equals(id) || currentUser.getRole().getPermissions().contains(MONITOR_USER)) {
            ImageEntity imageEntity = imageRepository.findById(imageDTO.getImageId()).orElseThrow(()
                    -> new ResourceNotFoundException("Image", "id", imageDTO.getImageId())
            );
            AppUserEntity user = getAppUserById(id);
            user.setUserImage(imageEntity);
            return new AppUserResponseDTO(userRepository.save(user));
        } else {
            throw new LimitedPermissionException();
        }
    }

    @Override
    public AppUserResponseDTO setRoleToUser(Long id, SetUserRoleDTO roleDTO) {
        AppUserEntity user = getAppUserById(id);
        AppRoleEntity appRoleEntity = roleRepository.findById(roleDTO.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleDTO.getRoleId()));
        user.setRole(appRoleEntity);
        return new AppUserResponseDTO(userRepository.save(user));
    }

    @Override
    public ApiResponse deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }

        AppUserEntity currentUser = getUserByRequest(userRepository);

        if (currentUser.getId().equals(id) || currentUser.getRole().getPermissions().contains(MONITOR_USER)) {

            userRepository.deleteById(id);

            return new ApiResponse(
                    HttpStatus.NO_CONTENT,
                    LocalDateTime.now(),
                    "User with id: " + id + " successfully deleted!"
            );
        } else {
            throw new LimitedPermissionException();
        }
    }

    @Override
    public AppUserResponseDTO updatePassword(SetUserPasswordDTO passwordDTO, Long id) {
        AppUserEntity user = getAppUserById(id);
        AppUserEntity currentUser = getUserByRequest(userRepository);

        if (currentUser.getId().equals(id)) {
            String oldPassword = passwordDTO.getOldPassword();
            String newPassword = passwordDTO.getNewPassword();
            String confirmedPassword = passwordDTO.getConfirmNewPassword();

            if (!newPassword.equals(confirmedPassword)) {
                throw new PasswordMismatchException(newPassword, confirmedPassword);
            }
            if (oldPassword.equals(newPassword)) {
                throw new DuplicateResourceException("passwords", oldPassword, newPassword);
            }
            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                throw new PasswordMismatchException(oldPassword, "user's password");
            }

            user.setPassword(passwordEncoder.encode(newPassword));

            return new AppUserResponseDTO(userRepository.save(user));
        } else {
            throw new LimitedPermissionException();
        }
    }

    // TODO: 9/2/22 needs rename (and maybe some docs)
    @Override
    public boolean existsWithUsername(String username) {
        return userRepository.existsByUserName(username);
    }

    @Override
    public AppUserEntity getUserByUsername(String username) {
        return userRepository.findAppUserByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    AppUserEntity getAppUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }
}
