package hamdam.bookee.APIs.role;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class AppRoleRepositoryTest {

    @Autowired
    private AppRoleRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void findFirstByIsDefaultIsTrue_returnsEmptyDataIfNoRoleExists() {
        //when
        Optional<AppRoleEntity> actual = underTest.findFirstByIsDefaultIsTrue();

        //then
        assertTrue(actual.isEmpty());
    }

    @Test
    void findFirstByIsDefaultIsTrue_returnsEmptyDataIfSingleNonDefaultRoleExists() {
        //given
        underTest.save(AppRoleEntity.builder().roleName("ADMIN").isDefault(false).timeStamp(LocalDateTime.now()).build());

        //when
        Optional<AppRoleEntity> actual = underTest.findFirstByIsDefaultIsTrue();

        //then
        assertTrue(actual.isEmpty());
    }

    @Test
    void findFirstByIsDefaultIsTrue_returnsValidDataIfSingleDefaultRoleExists() {
        //given
        AppRoleEntity expected = underTest.save(AppRoleEntity.builder().roleName("USER").isDefault(true).timeStamp(LocalDateTime.now()).build());

        //when
        Optional<AppRoleEntity> actual = underTest.findFirstByIsDefaultIsTrue();

        //then
        assertTrue(actual.isPresent());
        assertThat(actual.get().getId()).isEqualTo(expected.getId());
    }

    @Test
    void findFirstByIsDefaultIsTrue_returnsValidDataIfContainsMultipleNonDefaultRolesAndSingleDefaultRole() {
        //given
        AppRoleEntity expected = underTest.save(AppRoleEntity.builder().roleName("USER").isDefault(true).timeStamp(LocalDateTime.now()).build());
        underTest.save(AppRoleEntity.builder().roleName("ADMIN").isDefault(false).timeStamp(LocalDateTime.now()).build());
        underTest.save(AppRoleEntity.builder().roleName("AUTHOR").isDefault(false).timeStamp(LocalDateTime.now()).build());

        //when
        Optional<AppRoleEntity> actual = underTest.findFirstByIsDefaultIsTrue();

        //then
        assertTrue(actual.isPresent());
        assertThat(actual.get().getId()).isEqualTo(expected.getId());
    }

    @Test
    void findFirstByIsDefaultIsTrue_returnsValidDataWhenMultipleNonDefaultRolesAndMultipleDefaultRolesAvailable() {
        //given
        AppRoleEntity expected = underTest.save(AppRoleEntity.builder().roleName("USER-ben").isDefault(true).timeStamp(LocalDateTime.now()).build());
        underTest.save(AppRoleEntity.builder().roleName("USER-john").isDefault(true).timeStamp(LocalDateTime.now()).build());
        underTest.save(AppRoleEntity.builder().roleName("ADMIN").isDefault(false).timeStamp(LocalDateTime.now()).build());
        underTest.save(AppRoleEntity.builder().roleName("AUTHOR").isDefault(false).timeStamp(LocalDateTime.now()).build());

        //when
        Optional<AppRoleEntity> actual = underTest.findFirstByIsDefaultIsTrue();

        //then
        assertTrue(actual.isPresent());
        assertThat(actual.get().getId()).isEqualTo(expected.getId());
    }

    @Test
    void findAllByOrderByTimeStampDesc_returnEmptyDataWhenNoRoleOrderedByTimeStampDesc() {
        //given
        //when
        Page<AppRoleEntity> roles = underTest.findAllByOrderByTimeStampDesc(PageRequest.of(0, 1));

        //then
        assertTrue(roles.isEmpty());
    }

    @Test
    void findAllByOrderByTimeStampDesc_returnOrderedRolesByTimeStampDescWhenMultipleRolesAvailable() {
        //given
        List<AppRoleEntity> actual = new ArrayList<>();
        actual.add(underTest.save(AppRoleEntity.builder().roleName("AUTHOR").isDefault(false).timeStamp(LocalDateTime.now()).build()));
        actual.add(underTest.save(AppRoleEntity.builder().roleName("AUTHOR-ben").isDefault(false).timeStamp(LocalDateTime.now()).build()));
        actual.add(underTest.save(AppRoleEntity.builder().roleName("USER-john").isDefault(true).timeStamp(LocalDateTime.now()).build()));

        //when
        Page<AppRoleEntity> expected = underTest.findAllByOrderByTimeStampDesc(PageRequest.of(0, actual.size()));

        //then
        assertThat(expected.getSize()).isEqualTo(actual.size());
        assertThat(expected.getContent().get(0).getTimeStamp()).isAfter(
                expected.getContent().get(1).getTimeStamp());
        assertThat(expected.getContent().get(1).getTimeStamp()).isAfter(
                expected.getContent().get(2).getTimeStamp());
    }

    @Test
    void findAllByOrderByTimeStampDesc_returnOrderedRolesByTimeStampDescWhenMultipleRolesAvailableAndRoleUpdated() {
        //given
        List<AppRoleEntity> actual = new ArrayList<>();
        actual.add(underTest.save(AppRoleEntity.builder().roleName("AUTHOR-ben").isDefault(false).timeStamp(LocalDateTime.now()).build()));
        actual.add(underTest.save(AppRoleEntity.builder().roleName("AUTHOR-john").isDefault(false).timeStamp(LocalDateTime.now()).build()));
        actual.add(underTest.save(AppRoleEntity.builder().roleName("USER-fin").isDefault(false).timeStamp(LocalDateTime.now()).build()));
        AppRoleEntity updated = actual.get(2);
        updated.setRoleName(updated.getRoleName() + " UPDATED");
        updated = underTest.save(updated);

        //when
        Page<AppRoleEntity> pagedRoles = underTest.findAllByOrderByTimeStampDesc(PageRequest.of(0, actual.size()));

        //then
        assertThat(pagedRoles.getSize()).isEqualTo(actual.size());
        assertThat(pagedRoles.getContent().get(2).getTimeStamp()).isBefore(
                pagedRoles.getContent().get(1).getTimeStamp());
        assertThat(pagedRoles.getContent().get(1).getTimeStamp()).isBefore(
                pagedRoles.getContent().get(0).getTimeStamp());
        assertThat(pagedRoles.getContent().get(0).getId()).isEqualTo(updated.getId());
    }

    @ParameterizedTest
    @CsvSource({
            "USER,true",
            "TEST, false",
            "null, false"
    })
    void existsByRoleName_returnTrueWhenRoleNameExists() {
        // given
        String roleName = "USER";
        underTest.save(AppRoleEntity.builder().roleName(roleName).build());

        //when
        boolean expected = underTest.existsByRoleName(roleName);

        //then
        assertTrue(expected);
    }

//    @Test
//    void existsByRoleName_returnFalseWhenRoleNameDoesNotExists() {
//        // given
//        String roleName = "USER";
//        underTest.save(new AppRoleEntity(roleName));
//
//        //when
//        boolean expected = underTest.existsByRoleName("TEST");
//
//        //then
//        assertFalse(expected);
//    }
//
//    @Test
//    void existsByRoleName_returnFalseWhenRoleNameIsNull() {
//        //given
//        //when
//        boolean actual = underTest.existsByRoleName(null);
//
//        //then
//        assertThat(actual).isFalse();
//    }
}