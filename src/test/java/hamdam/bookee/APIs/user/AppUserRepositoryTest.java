package hamdam.bookee.APIs.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
    void existsByUserName_returnTrueWhenUserExistsWithUserName() {
        //given
        String username = "hamdam_kh";
        underTest.save(
                new AppUserEntity("Hamdam",
                        username,
                        "$2a$10$u.olISwSqjbaZCHADL0fIuw7eBijpqzvfSavgXnPcfniJTwORGNvm")
        );

        //when
        boolean expected = underTest.existsByUserName(username);

        //then
        assertTrue(expected);
    }

    @Test
    void existsByUserName_returnFalseWhenUserDoesNotExistsWithUserName() {
        //given
        String username = "user_kh";
        underTest.save(
                new AppUserEntity("Hamdam",
                        username,
                        "$2a$10$u.olISwSqjbaZCHADL0fIuw7eBijpqzvfSavgXnPcfniJTwORGNvm")
        );

        //when
        boolean expected = underTest.existsByUserName(username + " test");

        //then
        assertFalse(expected);
    }

    @Test
    void findAppUserByUserName_returnEmptyDataWhenUserNotFoundWithUserName() {
        //given
        String username = "hamdam";

        //when
        Optional<AppUserEntity> expected = underTest.findAppUserByUserName(username);

        //then
        assertTrue(expected.isEmpty());
    }

    @Test
    void findAppUserByUserName_returnValidDataWhenUserFoundWithUserName() {
        //given
        String username = "hamdam";
        AppUserEntity actual = underTest.save(
                new AppUserEntity("Hamdam",
                        username,
                        "$2a$10$u.olISwSqjbaZCHADL0fIuw7eBijpqzvfSavgXnPcfniJTwORGNvm")
        );

        //when
        Optional<AppUserEntity> expected = underTest.findAppUserByUserName(username);

        //then
        assertThat(actual.getId()).isEqualTo(expected.get().getId());
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
        updated.setUserName(updated.getUserName() + " UPDATED");
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