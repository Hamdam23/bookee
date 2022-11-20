package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.auth.RegistrationRequest;
import hamdam.bookee.APIs.image.ImageEntity;
import hamdam.bookee.APIs.image.ImageRepository;
import hamdam.bookee.APIs.image.helpers.UserImageDTO;
import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.APIs.user.helpers.AppUserRequestDTO;
import hamdam.bookee.APIs.user.helpers.AppUserResponseDTO;
import hamdam.bookee.APIs.user.helpers.UpdatePasswordRequest;
import hamdam.bookee.APIs.user.helpers.SetRoleUserRequest;
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

    @Override
    public void saveUser(RegistrationRequest request) {
        if (existsWithUsername(request.getUsername())) {
            throw new DuplicateResourceException("User", "username", request.getUsername());
        }

        AppUserEntity appUserEntity = new AppUserEntity(request);

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
        AppUserEntity requestingUser = getUserByRequest(userRepository);

        if (requestingUser.getRole().getPermissions().contains(MONITOR_USER)
                || requestingUser.getId().equals(id)) {
            if (request.getImageId() != null) {
                existingUser.setUserImage(imageRepository.findById(request.getImageId())
                        .orElseThrow(() -> new ResourceNotFoundException("Image", "id", request.getImageId()))
                );
            }

            if (!request.getUsername().equals(existingUser.getUsername()) &&
                    userRepository.existsByUsername(request.getUsername())) {
                throw new DuplicateResourceException("username", "User", request.getUsername());
            }
            existingUser.setUsername(request.getUsername());
            existingUser.setName(request.getName());

            return new AppUserResponseDTO(userRepository.save(existingUser));
        } else {
            throw new LimitedPermissionException();
        }
    }

    @Override
    public AppUserResponseDTO setImageToUser(Long id, UserImageDTO imageDTO) {

        AppUserEntity requestingUser = getUserByRequest(userRepository);

        if (requestingUser.getId().equals(id) || requestingUser.getRole().getPermissions().contains(MONITOR_USER)) {
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
    public AppUserResponseDTO setRoleToUser(Long id, SetRoleUserRequest roleDTO) {
        AppUserEntity user = getAppUserById(id);
        AppRoleEntity appRoleEntity = roleRepository.findById(roleDTO.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleDTO.getRoleId()));
        user.setRole(appRoleEntity);
        return new AppUserResponseDTO(userRepository.save(user));
    }

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

    @Override
    public AppUserResponseDTO updatePassword(UpdatePasswordRequest passwordDTO, Long id) {
        AppUserEntity user = getAppUserById(id);
        AppUserEntity requestingUser = getUserByRequest(userRepository);

        if (requestingUser.getId().equals(id)) {
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

    @Override
    public boolean existsWithUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public AppUserEntity getUserByUsername(String username, boolean withPermissions) {
        Optional<AppUserEntity> optional;
        if (withPermissions) {
            optional = userRepository.findAppUserByUsernameWithPermission(username);
        } else {
            optional = userRepository.findAppUserByUsername(username);
        }
        return optional.orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    AppUserEntity getAppUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }
}
