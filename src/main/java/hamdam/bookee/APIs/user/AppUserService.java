package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.image.Image;
import hamdam.bookee.APIs.image.UserImageDTO;
import hamdam.bookee.APIs.image.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AppUserRepository userRepository;
    private final ImageRepository imageRepository;

    public void addUser(AppUserDTO user){
        userRepository.save(new AppUser(user));
    }

    public AppUser getUserById(long id){
        AppUser user = userRepository.findById(id).orElseThrow(()
            -> new RuntimeException("User not found!")
        );
        return user;
    }

    public List<AppUser> getAllUsers(){
        return userRepository.findAll();
    }

    public AppUser updateUser(AppUser newUser, long id){
        AppUser user = userRepository.findById(id).orElseThrow(()
        -> new RuntimeException("User not found!")
        );
        user.setName(newUser.getName());
        user.setPassword(newUser.getPassword());
        return user;
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

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
}
