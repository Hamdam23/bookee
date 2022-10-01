package hamdam.bookee.APIs.role;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
        Optional<AppRoleEntity> actual = underTest.findFirstByIsDefaultIsTrue();

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
        Optional<AppRoleEntity> actual = underTest.findFirstByIsDefaultIsTrue();

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
        Optional<AppRoleEntity> actual = underTest.findFirstByIsDefaultIsTrue();

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
        Optional<AppRoleEntity> actual = underTest.findFirstByIsDefaultIsTrue();

        //then
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getRoleName()).isEqualTo(role.getRoleName());
    }

    @Test
    void returnsValidDataWhenMultipleNonDefaultRolesAndMultipleDefaultRolesAvailable() {
        //given
        AppRoleEntity expected = underTest.save(new AppRoleEntity(
                "USER1",
                true,
                LocalDateTime.now()
        ));
        underTest.save(new AppRoleEntity(
                "USER2",
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
        Optional<AppRoleEntity> actual = underTest.findFirstByIsDefaultIsTrue();

        //then
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getRoleName()).isEqualTo(expected.getRoleName());
    }

    @Test
    void returnEmptyDataWhenNoRoleOrderedByTimeStampDesc() {
        //given
        //when
        Page<AppRoleEntity> pagedRoles = underTest.findAllByOrderByTimeStampDesc(PageRequest.of(0, 1));

        //then
        assertThat(pagedRoles.isEmpty()).isTrue();
    }

    @Test
    void returnOrderedRolesByTimeStampDescWhenMultipleRolesAvailable() {
        //given
        List<AppRoleEntity> actual = new ArrayList<>();
        actual.add(underTest.save(new AppRoleEntity(
                "AUTHOR1",
                false,
                LocalDateTime.now()
        )));
        actual.add(underTest.save(new AppRoleEntity(
                "AUTHOR2",
                false,
                LocalDateTime.now()
        )));
        actual.add(underTest.save(new AppRoleEntity(
                "USER1",
                true,
                LocalDateTime.now()
        )));

        //when
        Page<AppRoleEntity> pagedRoles = underTest.findAllByOrderByTimeStampDesc(PageRequest.of(0, actual.size()));

        //then
        assertThat(pagedRoles.getSize()).isEqualTo(actual.size());
        assertThat(pagedRoles.getContent().get(0).getTimeStamp()).isAfter(
                pagedRoles.getContent().get(1).getTimeStamp());
        assertThat(pagedRoles.getContent().get(1).getTimeStamp()).isAfter(
                pagedRoles.getContent().get(2).getTimeStamp());
    }

    @Test
    void returnOrderedRolesByTimeStampDescWhenMultipleRolesAvailableAndRoleUpdated() {
        //given
        List<AppRoleEntity> actual = new ArrayList<>();
        actual.add(underTest.save(new AppRoleEntity(
                "AUTHOR1",
                false,
                LocalDateTime.now()
        )));
        actual.add(underTest.save(new AppRoleEntity(
                "AUTHOR2",
                false,
                LocalDateTime.now()
        )));
        actual.add(underTest.save(new AppRoleEntity(
                "USER1",
                true,
                LocalDateTime.now()
        )));
        AppRoleEntity updated = actual.get(2);
        updated.setRoleName(updated.getRoleName() + " UPDATED");
        updated = underTest.save(updated);

        //when
        Page<AppRoleEntity> pagedRoles = underTest.findAllByOrderByTimeStampDesc(PageRequest.of(0, actual.size()));

        //then
        assertThat(pagedRoles.getSize()).isEqualTo(actual.size());
        assertThat(pagedRoles.getContent().get(0).getTimeStamp()).isAfter(
                pagedRoles.getContent().get(1).getTimeStamp());
        assertThat(pagedRoles.getContent().get(1).getTimeStamp()).isAfter(
                pagedRoles.getContent().get(2).getTimeStamp());
        assertThat(pagedRoles.getContent().get(0).getId()).isEqualTo(updated.getId());
    }

    @Test
    void returnTrueWhenRoleNameExists() {
        // given
        String roleName = "USER";
        underTest.save(new AppRoleEntity(roleName));

        //when
        boolean expected = underTest.existsByRoleName(roleName);

        //then
        assertThat(expected).isTrue();
    }

    @Test
    void returnFalseWhenRoleNameDoesNotExists() {
        // given
        String roleName = "USER";
        underTest.save(new AppRoleEntity(roleName));

        //when
        boolean expected = underTest.existsByRoleName("TEST");

        //then
        assertThat(expected).isFalse();
    }
}