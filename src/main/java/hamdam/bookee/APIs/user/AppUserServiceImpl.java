package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.image.ImagEntity;
import hamdam.bookee.APIs.image.ImageRepository;
import hamdam.bookee.APIs.image.UserImageDTO;
import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.tools.exeptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public AppUserEntity getUserByUsername(String username) {
        return userRepository.findAppUserByUserName(username).orElseThrow(()
                // TODO: 9/2/22 custom exception
                -> new ResourceNotFoundException("User", "username", username)
        );
    }

    @Override
    public List<AppUserEntity> getAllUsers() {
        return userRepository.findAllByOrderByTimeStampDesc();
    }

    @Override
    public AppUserEntity updateUser(AppUserDTO newUser, long id) {
        // TODO: 9/2/22 code duplication
        AppUserEntity user = getAppUserById(id);
        user.setName(newUser.getName());
        return user;
    }

    @Override
    public void deleteUser(long id) {
        // TODO: 9/2/22 searching for image or user? imageRepository? why?
        userRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("User", "id", id)
        );
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void setImageToUser(long id, UserImageDTO imageDTO) {
        ImagEntity imagEntity = imageRepository.findById(imageDTO.getImageId()).orElseThrow(()
                -> new ResourceNotFoundException("Image", "id", imageDTO.getImageId())
        );
        // TODO: 9/2/22 code duplication
        AppUserEntity user = getAppUserById(id);
        user.setUserImagEntity(imagEntity);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public AppUserEntity setRoleToUser(long id, AppUserRoleDTO roleDTO) {
        AppUserEntity user = userRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("User", "id", id)
        );
        AppRoleEntity appRoleEntity = roleRepository.findById(roleDTO.getRoleId()).orElseThrow(
                () -> new ResourceNotFoundException("Role", "id", roleDTO.getRoleId())
        );
        user.setRole(appRoleEntity);
        // TODO: 9/2/22 you can return value from repository method call
        return userRepository.save(user);
    }

    // TODO: 9/2/22 needs rename (and maybe some docs)
    @Override
    public boolean isPasswordInvalid(String username) {
        return userRepository.existsByUserName(username);
    }

    private AppUserEntity getAppUserById(Long id){
        return userRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("User", "id", id)
        );
    }

}
