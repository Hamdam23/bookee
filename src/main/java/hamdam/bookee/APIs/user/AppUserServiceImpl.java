package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.image.ImagEntity;
import hamdam.bookee.APIs.image.ImageRepository;
import hamdam.bookee.APIs.image.UserImageDTO;
import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.APIs.user.helpers.AppUserRequestDTO;
import hamdam.bookee.APIs.user.helpers.AppUserResponseDTO;
import hamdam.bookee.APIs.user.helpers.AppUserRoleIdDTO;
import hamdam.bookee.tools.exceptions.ApiResponse;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import hamdam.bookee.tools.exceptions.pemission.LimitedPermissionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

import static hamdam.bookee.APIs.role.Permissions.MONITOR_USER;
import static hamdam.bookee.tools.token.GetUserByToken.getUserByRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserServiceImpl implements AppUserService, UserDetailsService {

    private final AppUserRepository userRepository;
    private final AppRoleRepository roleRepository;
    private final ImageRepository imageRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUserEntity> user = userRepository.findAppUserByUserName(username);
        // TODO: 9/2/22 write mapper method/class for AppUser <-> User
        // TODO TODO
        return new User(
                // TODO: 9/2/22 handle get() call
                user.get().getUserName(),
                user.get().getPassword(),
                user.get().getRole().getPermissions().stream().map(
                        permission -> new SimpleGrantedAuthority(permission.name())
                ).collect(Collectors.toList())
        );
    }

    @Override
    public Page<AppUserResponseDTO> getAllUsers(@PageableDefault Pageable pageable) {
        return userRepository.findAllByOrderByTimeStampDesc(pageable).map(AppUserResponseDTO::new);
    }

    @Override
    public AppUserResponseDTO getUserById(Long id) {
        // TODO: 9/2/22 custom exception
        return new AppUserResponseDTO(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id))
        );
    }

    @Override
    public AppUserResponseDTO updateUser(AppUserRequestDTO newUser, Long id) {
        // TODO: 9/2/22 code duplication
        AppUserEntity requestedUser = getAppUserById(id);
        AppUserEntity currentUser = getUserByRequest(userRepository);

        if (currentUser.getRole().getPermissions().contains(MONITOR_USER) && newUser.getRoleId() != null) {
            requestedUser.setRole(roleRepository.findById(newUser.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role", "id", newUser.getRoleId()))
            );
        }

        if (currentUser.getId().equals(id) || currentUser.getRole().getPermissions().contains(MONITOR_USER)) {
            requestedUser.setUserImagEntity(imageRepository.findById(newUser.getImageId())
                    .orElseThrow(() -> new ResourceNotFoundException("Image", "id", id))
            );

            requestedUser.setName(newUser.getName());
            requestedUser.setUserName(newUser.getUserName());

            return new AppUserResponseDTO(userRepository.save(requestedUser));
        } else {
            throw new LimitedPermissionException();
        }

    }

    @Override
    @Transactional
    public AppUserResponseDTO setImageToUser(Long id, UserImageDTO imageDTO) {

        AppUserEntity currentUser = getUserByRequest(userRepository);

        if (currentUser.getId().equals(id) || currentUser.getRole().getPermissions().contains(MONITOR_USER)) {
            userRepository.findById(id).orElseThrow(()
                    -> new ResourceNotFoundException("User", "id", id)
            );
            ImagEntity imagEntity = imageRepository.findById(imageDTO.getImageId()).orElseThrow(()
                    -> new ResourceNotFoundException("Image", "id", imageDTO.getImageId())
            );
            // TODO: 9/2/22 code duplication
            AppUserEntity user = getAppUserById(id);
            user.setUserImagEntity(imagEntity);
            return new AppUserResponseDTO(userRepository.save(user));
        } else {
            throw new LimitedPermissionException();
        }
    }

    @Override
    @Transactional
    public AppUserResponseDTO setRoleToUser(Long id, AppUserRoleIdDTO roleDTO) {
        AppUserEntity user = userRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("User", "id", id)
        );
        AppRoleEntity appRoleEntity = roleRepository.findById(roleDTO.getRoleId()).orElseThrow(
                () -> new ResourceNotFoundException("Role", "id", roleDTO.getRoleId())
        );
        user.setRole(appRoleEntity);
        // TODO: 9/2/22 you can return value from repository method call
        return new AppUserResponseDTO(userRepository.save(user));
    }

    @Override
    public ApiResponse deleteUser(Long id) {
        // TODO: 9/2/22 searching for image or user? imageRepository? why?
        AppUserEntity currentUser = getUserByRequest(userRepository);

        if (currentUser.getId().equals(id) || currentUser.getRole().getPermissions().contains(MONITOR_USER)) {
            userRepository.findById(id).orElseThrow(()
                    -> new ResourceNotFoundException("User", "id", id)
            );
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

    // TODO: 9/2/22 needs rename (and maybe some docs)
    @Override
    public boolean isPasswordInvalid(String username) {
        return userRepository.existsByUserName(username);
    }

    @Override
    public AppUserEntity getUserByUsername(String username) {
        return userRepository.findAppUserByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    private AppUserEntity getAppUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

}
