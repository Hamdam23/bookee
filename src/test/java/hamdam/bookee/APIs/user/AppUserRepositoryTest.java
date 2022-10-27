package hamdam.bookee.APIs.user;

import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
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
        boolean expected = underTest.existsByUsername(null);

        //then
        assertFalse(expected);
    }

    @Test
    void existsByUsername_returnFalseWhenUserDoesNotExistsWithUsername() {
        //given
        String username = "user_kh";
        underTest.save(
                new AppUserEntity("Hamdam",
                        username,
                        "$2a$10$u.olISwSqjbaZCHADL0fIuw7eBijpqzvfSavgXnPcfniJTwORGNvm")
        );

        //when
        boolean expected = underTest.existsByUsername(username + " test");

        //then
        assertFalse(expected);
    }

    @Test
    void existsByUsername_returnTrueWhenUserExistsWithUsername() {
        //given
        String username = "hamdam_kh";
        underTest.save(
                new AppUserEntity("Hamdam",
                        username,
                        "$2a$10$u.olISwSqjbaZCHADL0fIuw7eBijpqzvfSavgXnPcfniJTwORGNvm")
        );

        //when
        boolean expected = underTest.existsByUsername(username);

        //then
        assertTrue(expected);
    }

    @Test
    void findAppUserByUsername_returnEmptyDataWhenUsernameIsNull() {
        //given
        //when
        Optional<AppUserEntity> expected = underTest.findAppUserByUsername(null);

        //then
        assertTrue(expected.isEmpty());
    }

    @Test
    void findAppUserByUsername_returnEmptyDataWhenUserNotFoundWithUsername() {
        //given
        String username = "hamdam";

        //when
        Optional<AppUserEntity> expected = underTest.findAppUserByUsername(username);

        //then
        assertTrue(expected.isEmpty());
    }

    @Test
    void findAppUserByUsername_returnValidDataWhenUserFoundWithUsername() {
        //given
        String username = "hamdam";
        AppUserEntity actual = underTest.save(
                new AppUserEntity("Hamdam",
                        username,
                        "$2a$10$u.olISwSqjbaZCHADL0fIuw7eBijpqzvfSavgXnPcfniJTwORGNvm")
        );

        //when
        Optional<AppUserEntity> expected = underTest.findAppUserByUsername(username);

        //then
        AppUserEntity user = expected.orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        assertThat(actual.getId()).isEqualTo(user.getId());
    }

    @Test
    void findAllByOrderByTimeStampDesc_returnEmptyDataWhenNoUser() {
        //given
        Page<AppUserEntity> actual = new PageImpl<>(List.of(), Pageable.ofSize(1), 0);

        //when
        Page<AppUserEntity> expected = underTest.findAllByOrderByTimeStampDesc(Pageable.ofSize(1));

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAllByOrderByTimeStampDesc_returnSingleDataWhenSingleUser() {
        //given
        underTest.save(
                new AppUserEntity("Hamdam",
                        "hamdam_kh",
                        "$2a$10$u.olISwSqjbaZCHADL0fIuw7eBijpqzvfSavgXnPcfniJTwORGNvm")
        );

        //when
        Page<AppUserEntity> expected = underTest.findAllByOrderByTimeStampDesc(Pageable.ofSize(1));

        //then
        assertEquals(expected.getSize(), 1);
    }

    @Test
    void findAllByOrderByTimeStampDesc_returnOrderedUsersWhenMultipleUsers() {
        //given
        List<AppUserEntity> actual = new ArrayList<>();
        actual.add(underTest.save(
                new AppUserEntity("Phil",
                        "philly",
                        "$2a$10$u.olISwSqjbaZCHADL0fIuw7eBijpqzvfSavgXnPcfniJTwORGNvm",
                        LocalDateTime.now()
                )
        ));
        actual.add(underTest.save(
                new AppUserEntity("Bob",
                        "bobby",
                        "$2a$10$u.olISwSqjbaZCHADL0fIuw7eBijpqzvfSavgXnPcfniJTwORGNvm",
                        LocalDateTime.now()
                )
        ));
        actual.add(underTest.save(
                new AppUserEntity("Luna",
                        "lun",
                        "$2a$10$u.olISwSqjbaZCHADL0fIuw7eBijpqzvfSavgXnPcfniJTwORGNvm",
                        LocalDateTime.now()
                )
        ));

        //when
        Page<AppUserEntity> expected = underTest.findAllByOrderByTimeStampDesc(Pageable.ofSize(actual.size()));

        //then
        assertThat(expected.getSize()).isEqualTo(actual.size());
        assertThat(expected.getContent().get(0).getTimeStamp()).isAfter(
                expected.getContent().get(1).getTimeStamp());
        assertThat(expected.getContent().get(1).getTimeStamp()).isAfter(
                expected.getContent().get(2).getTimeStamp());
    }

    @Test
    void findAllByOrderByTimeStampDesc_returnOrderedUsersWhenMultipleUsersWithUpdatedUser() {
        //given
        List<AppUserEntity> actual = new ArrayList<>();
        actual.add(underTest.save(
                new AppUserEntity("Phil",
                        "philly",
                        "$2a$10$u.olISwSqjbaZCHADL0fIuw7eBijpqzvfSavgXnPcfniJTwORGNvm",
                        LocalDateTime.now()
                )
        ));
        actual.add(underTest.save(
                new AppUserEntity("Bob",
                        "bobby",
                        "$2a$10$u.olISwSqjbaZCHADL0fIuw7eBijpqzvfSavgXnPcfniJTwORGNvm",
                        LocalDateTime.now()
                )
        ));
        actual.add(underTest.save(
                new AppUserEntity("Luna",
                        "lun",
                        "$2a$10$u.olISwSqjbaZCHADL0fIuw7eBijpqzvfSavgXnPcfniJTwORGNvm",
                        LocalDateTime.now()
                )
        ));
        AppUserEntity updated = actual.get(2);
        updated.setUsername(updated.getUsername() + " UPDATED");
        updated = underTest.save(updated);

        //when
        Page<AppUserEntity> expected = underTest.findAllByOrderByTimeStampDesc(Pageable.ofSize(actual.size()));

        //then
        assertThat(expected.getSize()).isEqualTo(actual.size());
        assertThat(expected.getContent().get(2).getTimeStamp()).isBefore(
                expected.getContent().get(1).getTimeStamp());
        assertThat(expected.getContent().get(1).getTimeStamp()).isBefore(
                expected.getContent().get(0).getTimeStamp());
        assertThat(expected.getContent().get(0).getId()).isEqualTo(updated.getId());
    }
}