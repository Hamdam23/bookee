package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.user.helpers.UserMappers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void existsByUsername_returnFalseWhenUsernameIsNull() {
        //given
        //when
        boolean actual = underTest.existsByUsername(null);

        //then
        assertFalse(actual);
    }

    @Test
    void existsByUsername_returnFalseWhenUserDoesNotExistsWithUsername() {
        //given
        String username = "jackie";
        underTest.save(UserMappers.mapToAppUserEntity("bob", username, "pass")
        );

        //when
        boolean actual = underTest.existsByUsername(username + " test");

        //then
        assertFalse(actual);
    }

    @Test
    void existsByUsername_returnTrueWhenUserExistsWithUsername() {
        //given
        String username = "jackie";
        underTest.save(UserMappers.mapToAppUserEntity("bob", username, "pass")
        );

        //when
        boolean actual = underTest.existsByUsername(username);

        //then
        assertTrue(actual);
    }

    @Test
    void findAppUserByUsername_returnEmptyDataWhenUsernameIsNull() {
        //given
        //when
        Optional<AppUserEntity> actual = underTest.findAppUserByUsername(null);

        //then
        assertTrue(actual.isEmpty());
    }

    @Test
    void findAppUserByUsername_returnEmptyDataWhenUserNotFoundWithUsername() {
        //given
        String username = "bob";

        //when
        Optional<AppUserEntity> actual = underTest.findAppUserByUsername(username);

        //then
        assertTrue(actual.isEmpty());
    }

    @Test
    void findAppUserByUsername_returnValidDataWhenUserFoundWithUsername() {
        //given
        String username = "jackie";
        AppUserEntity expected = underTest.save(UserMappers.mapToAppUserEntity("bob", username, "pass")
        );

        //when
        Optional<AppUserEntity> actual = underTest.findAppUserByUsername(username);

        //then
        assertThat(actual).isPresent();
        assertThat(actual.get().getId()).isEqualTo(expected.getId());
    }

    @Test
    void findAllByOrderByTimeStampDesc_returnEmptyDataWhenNoUser() {
        //given
        Page<AppUserEntity> expected = new PageImpl<>(List.of(), Pageable.ofSize(1), 0);

        //when
        Page<AppUserEntity> actual = underTest.findAllByOrderByTimeStampDesc(Pageable.ofSize(1));

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAllByOrderByTimeStampDesc_returnSingleDataWhenSingleUser() {
        //given
        underTest.save(UserMappers.mapToAppUserEntity("Jackie", "jackie", "pass"));

        //when
        Page<AppUserEntity> actual = underTest.findAllByOrderByTimeStampDesc(Pageable.ofSize(1));

        //then
        assertEquals(actual.getSize(), 1);
    }

    @Test
    void findAllByOrderByTimeStampDesc_returnOrderedUsersWhenMultipleUsers() {
        //given
        List<AppUserEntity> expected = new ArrayList<>();
        expected.add(underTest.save(UserMappers.mapToAppUserEntity("Phil", "philly", "pass", LocalDateTime.now())));
        expected.add(underTest.save(UserMappers.mapToAppUserEntity("Bob", "bobby", "pass", LocalDateTime.now())));
        expected.add(underTest.save(UserMappers.mapToAppUserEntity("Luna", "lun", "pass", LocalDateTime.now())));

        //when
        Page<AppUserEntity> actual = underTest.findAllByOrderByTimeStampDesc(Pageable.ofSize(expected.size()));

        //then
        assertThat(actual.getSize()).isEqualTo(expected.size());
        assertThat(actual.getContent().get(0).getTimeStamp()).isAfter(
                actual.getContent().get(1).getTimeStamp());
        assertThat(actual.getContent().get(1).getTimeStamp()).isAfter(
                actual.getContent().get(2).getTimeStamp());
    }

    @Test
    void findAllByOrderByTimeStampDesc_returnOrderedUsersWhenMultipleUsersWithUpdatedUser() {
        //given
        List<AppUserEntity> expected = new ArrayList<>();
        expected.add(underTest.save(UserMappers.mapToAppUserEntity("Phil", "philly", "pass", LocalDateTime.now())));
        expected.add(underTest.save(UserMappers.mapToAppUserEntity("Bob", "bobby", "pass", LocalDateTime.now())));
        expected.add(underTest.save(UserMappers.mapToAppUserEntity("Luna", "lun", "pass", LocalDateTime.now())));

        AppUserEntity updated = expected.get(2);
        updated.setUsername(updated.getUsername() + " UPDATED");
        updated = underTest.save(updated);

        //when
        Page<AppUserEntity> actual = underTest.findAllByOrderByTimeStampDesc(Pageable.ofSize(expected.size()));

        //then
        assertThat(actual.getSize()).isEqualTo(expected.size());
        assertThat(actual.getContent().get(2).getTimeStamp()).isBefore(
                actual.getContent().get(1).getTimeStamp());
        assertThat(actual.getContent().get(1).getTimeStamp()).isBefore(
                actual.getContent().get(0).getTimeStamp());
        assertThat(actual.getContent().get(0).getId()).isEqualTo(updated.getId());
    }
}