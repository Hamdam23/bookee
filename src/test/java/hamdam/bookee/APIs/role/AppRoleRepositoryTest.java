package hamdam.bookee.APIs.role;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AppRoleRepositoryTest {

    @Autowired
    private AppRoleRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void returnsEmptyDataIfNoRoleExists() {
        //when
        Optional<AppRoleEntity> actual = underTest.findFirstByIsDefault(true);

        //then
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    void returnsEmptyDataIfSingleNonDefaultRoleExists() {
        //given
        underTest.save(new AppRoleEntity(
                "ADMIN",
                false,
                LocalDateTime.now()
        ));

        //when
        Optional<AppRoleEntity> actual = underTest.findFirstByIsDefault(true);

        //then
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    void returnsValidDataIfSingleDefaultRoleExists() {
        //given
        AppRoleEntity expected = underTest.save(new AppRoleEntity(
                "USER",
                true,
                LocalDateTime.now()
        ));

        //when
        Optional<AppRoleEntity> actual = underTest.findFirstByIsDefault(true);

        //then
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getId()).isEqualTo(expected.getId());
    }

    @Test
    void returnsValidDataIfContainsMultipleNonDefaultRolesAndSingleDefaultRole() {
        //given
        AppRoleEntity role = underTest.save(new AppRoleEntity(
                "USER",
                true,
                LocalDateTime.now()
        ));
        underTest.save(new AppRoleEntity(
                "ADMIN",
                false,
                LocalDateTime.now()
        ));
        underTest.save(new AppRoleEntity(
                "AUTHOR",
                false,
                LocalDateTime.now()
        ));

        //when
        Optional<AppRoleEntity> actual = underTest.findFirstByIsDefault(true);

        //then
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getRoleName()).isEqualTo(role.getRoleName());
    }

    @Test
    void returnsValidDataIfContainsMultipleNonDefaultRolesAndMultipleDefaultRoles() {
        //given
        AppRoleEntity role1 = underTest.save(new AppRoleEntity(
                "USER1",
                true,
                LocalDateTime.now()
        ));
        AppRoleEntity role2 = underTest.save(new AppRoleEntity(
                "USER2",
                true,
                LocalDateTime.now()
        ));
        AppRoleEntity role3 = underTest.save(new AppRoleEntity(
                "ADMIN",
                false,
                LocalDateTime.now()
        ));
        AppRoleEntity role4 = underTest.save(new AppRoleEntity(
                "AUTHOR",
                false,
                LocalDateTime.now()
        ));

        //when
        Optional<AppRoleEntity> actual = underTest.findFirstByIsDefault(true);

        //then
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getRoleName()).isEqualTo(role1.getRoleName());
    }

    @Test
    @Disabled
    void findAllByOrderByTimeStampDesc() {
    }
}