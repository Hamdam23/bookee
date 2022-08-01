package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.image.Image;
import hamdam.bookee.APIs.image.ImageRepository;
import hamdam.bookee.APIs.image.UserImageDTO;
import hamdam.bookee.APIs.role.AppRole;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserServiceImpl implements AppUserService, UserDetailsService {
    private final AppUserRepository userRepository;
    private final AppRoleRepository roleRepository;
    private final ImageRepository imageRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //TODO here, when User is not found Exception does not work anyway,because Security EntryPoint Ex. will execute.
        //AppUser user = userRepository.findAppUserByUserName(username).orElseThrow(()
        //-> new RuntimeException("User not found!")
        //);
        Optional<AppUser> user = userRepository.findAppUserByUserName(username);
//        Collection<SimpleGrantedAuthority> authority = new ArrayList<>();
        //TODO Here is where it catches the exception.
//        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(user.get().getRole().getRoleName());
//        authority.add(simpleGrantedAuthority);
        return new User(
                user.get().getUserName(),
                user.get().getPassword(),
                user.get().getRole().getPermissions().stream().map(
                        permission -> new SimpleGrantedAuthority(permission.name())
                ).collect(Collectors.toList())
        );
    }

    @Override
    public AppUser getUserByUsername(String userName) {
        return userRepository.findAppUserByUserName(userName).orElseThrow(()
                -> new RuntimeException("User not found!")
        );
    }

    @Override
    public List<AppUser> getAllUsers() {
        return userRepository.findAllByOrderByTimeStampDesc();
    }

    @Override
    public AppUser updateUser(AppUser newUser, long id) {
        AppUser user = userRepository.findById(id).orElseThrow(()
                -> new RuntimeException("User not found!")
        );
        user.setName(newUser.getName());
        user.setPassword(newUser.getPassword());
        return user;
    }

    @Override
    public void deleteUser(long id) {
        imageRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("User", "id", id)
        );
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void setImageToUser(long id, UserImageDTO imageDTO) {
        Image image = imageRepository.findById(imageDTO.getImageId()).orElseThrow(()
                -> new ResourceNotFoundException("Image", "id", id)
        );
        AppUser user = userRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("User", "id", imageDTO.getImageId())
        );
        user.setUserImage(image);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public AppUser setRoleToUser(long id, AppUserRoleDTO roleDTO) {
        AppUser user = userRepository.findById(id).orElseThrow(()
                -> new RuntimeException("User not found!")
        );
        AppRole appRole = roleRepository.findById(roleDTO.getRoleId()).orElseThrow(
                () -> new ResourceNotFoundException("Role", "id", roleDTO.getRoleId())
        );
        user.setRole(appRole);
        userRepository.save(user);
        return user;
    }

}
