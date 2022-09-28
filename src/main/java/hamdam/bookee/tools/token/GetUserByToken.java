package hamdam.bookee.tools.token;

import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class GetUserByToken {

    public static AppUserEntity getUserByRequest(AppUserRepository userRepository) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();

        return userRepository.findAppUserByUserName(username).orElseThrow(
                () -> new ResourceNotFoundException("User", "username", username));
    }
}
