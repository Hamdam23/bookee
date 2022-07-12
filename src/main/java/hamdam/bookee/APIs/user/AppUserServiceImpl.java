package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.image.Image;
import hamdam.bookee.APIs.image.UserImageDTO;
import hamdam.bookee.APIs.image.ImageRepository;
import hamdam.bookee.APIs.role.AppRole;
import hamdam.bookee.APIs.role.AppRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService, UserDetailsService {
    private final AppUserRepository userRepository;
    private final AppRoleRepository roleRepository;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findAppUserByUserName(username).orElseThrow(()
                -> new RuntimeException("User not found!")
        );
        Collection<SimpleGrantedAuthority> authority = new ArrayList<>();
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(user.getRoles().getRoleName());
        authority.add(simpleGrantedAuthority);
        return new User(user.getName(), user.getPassword(), authority);
    }

    @Override
    public AppUser addUser(AppUserDTO user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(new AppUser(user));
    }

    @Override
    public AppUser getUser(String userName){
        AppUser user = userRepository.findAppUserByUserName(userName).orElseThrow(()
            -> new RuntimeException("User not found!")
        );
        return user;
    }

    @Override
    public List<AppUser> getAllUsers(){
        return userRepository.findAll();
    }

    @Override
    public AppUser updateUser(AppUser newUser, long id){
        AppUser user = userRepository.findById(id).orElseThrow(()
        -> new RuntimeException("User not found!")
        );
        user.setName(newUser.getName());
        user.setPassword(newUser.getPassword());
        return user;
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void setImageToUser(long id, UserImageDTO imageDTO){
        Image image = imageRepository.findById(imageDTO.getImageId()).orElseThrow(()
        -> new RuntimeException("Image not found!")
        );
        AppUser user = userRepository.findById(id).orElseThrow(()
                -> new RuntimeException("User not found!")
        );
        user.setUserImage(image);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public AppUser setRoleToUser(long id, AppUserRoleDTO appUserRoleDTO) {
        AppUser user = userRepository.findById(id).orElseThrow(()
                -> new RuntimeException("User not found!")
        );
        AppRole appRole = roleRepository.findAppRoleByRoleName(appUserRoleDTO.getRoleName());
        user.setRoles(appRole);
        userRepository.save(user);
        return user;
    }
}
