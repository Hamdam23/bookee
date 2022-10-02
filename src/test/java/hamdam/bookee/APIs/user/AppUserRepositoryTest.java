package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.auth.RegistrationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@SpringBootTest
class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository underTest;

    @BeforeEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void canGetUserByExitingUserName() {
        //given
        String username = "user_kh";
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "User Userov",
                username,
                "$2a$10$u.olISwSqjbaZCHADL0fIuw7eBijpqzvfSavgXnPcfniJTwORGNvm"
        );
        AppUserEntity user = new AppUserEntity(registrationRequest);
        underTest.save(user);

        //when
        boolean expected = underTest.existsByUserName(username);

        //then
        assertThat(expected).isTrue();
    }

    @Test
    void canGetUserByNonExitingUserName() {
        //given
        String username = "user_kh";
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "User Userov",
                username,
                "$2a$10$u.olISwSqjbaZCHADL0fIuw7eBijpqzvfSavgXnPcfniJTwORGNvm"
        );
        AppUserEntity user = new AppUserEntity(registrationRequest);
        underTest.save(user);

        //when
        boolean expected = underTest.existsByUserName("user");

        //then
        assertThat(expected).isFalse();
    }
}