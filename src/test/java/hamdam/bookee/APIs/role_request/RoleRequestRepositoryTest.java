package hamdam.bookee.APIs.role_request;

import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.APIs.role.helpers.RoleMappers;
import hamdam.bookee.APIs.role_request.helpers.RoleRequestMappers;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.APIs.user.helpers.UserMappers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static hamdam.bookee.APIs.role_request.State.DECLINED;
import static hamdam.bookee.APIs.role_request.State.IN_PROGRESS;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RoleRequestRepositoryTest {

    @Autowired
    private RoleRequestRepository underTest;

    @Autowired
    private AppRoleRepository roleRepository;

    @Autowired
    private AppUserRepository userRepository;

    @BeforeEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void existsByUserAndState_shouldReturnFalseWhenUserIsNull() {
        //given
        //when
        boolean actual = underTest.existsByUserAndState(null, IN_PROGRESS);

        //then
        assertThat(actual).isFalse();
    }

    @Test
    void existsByUserAndState_shouldReturnFalseWhenStateIsNull() {
        //given
        AppUserEntity userLi = userRepository.save(UserMappers.mapToAppUserEntity("Li", "li", "pass"));

        //when
        boolean actual = underTest.existsByUserAndState(userLi, null);

        //then
        assertThat(actual).isFalse();
    }

    @Test
    void existsByUserAndState_shouldReturnFalseWhenUserAndStateIsNull() {
        //given
        //when
        boolean actual = underTest.existsByUserAndState(null, null);

        //then
        assertThat(actual).isFalse();
    }

    @Test
    void existsByUserAndState_shouldReturnFalseWhenUserDoesNotHaveRoleRequest() {
        //given
        AppUserEntity userLi = userRepository.save(UserMappers.mapToAppUserEntity("Li", "li", "pass"));

        //when
        boolean actual = underTest.existsByUserAndState(userLi, IN_PROGRESS);

        //then
        assertThat(actual).isFalse();
    }

    @Test
    void existsByUserAndState_shouldReturnFalseWhenUserDoesNotHaveInProgressRoleRequest() {
        //given
        AppUserEntity userLi = userRepository.save(UserMappers.mapToAppUserEntity("Li", "li", "pass"));
        AppRoleEntity requestedRole = roleRepository.save(RoleMappers.mapToAppRoleEntity("author"));

        underTest.save(RoleRequestMappers.mapToRoleRequestEntity(userLi, requestedRole, DECLINED));

        //when
        boolean actual = underTest.existsByUserAndState(userLi, IN_PROGRESS);

        //then
        assertThat(actual).isFalse();
    }

    @Test
    void existsByUserAndState_shouldReturnTrueWhenUserHasInProgressRoleRequest() {
        //given
        AppUserEntity userLi = userRepository.save(UserMappers.mapToAppUserEntity("Li", "li", "pass"));
        AppRoleEntity requestedRole = roleRepository.save(RoleMappers.mapToAppRoleEntity("author"));

        underTest.save(RoleRequestMappers.mapToRoleRequestEntity(userLi, requestedRole, IN_PROGRESS));

        //when
        boolean actual = underTest.existsByUserAndState(userLi, IN_PROGRESS);

        //then
        assertThat(actual).isTrue();
    }

    @Test
    void findAllByState_shouldReturnEmptyListWhenStateIsNull() {
        //given
        //when
        List<RoleRequestEntity> actual = underTest.findAllByState(null);

        //then
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    void findAllByState_shouldReturnEmptyListWhenNoRoleRequestByState() {
        //given
        //when
        List<RoleRequestEntity> actual = underTest.findAllByState(IN_PROGRESS);

        //then
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    void findAllByState_shouldReturnValidDataWhenRequestIsValid() {
        //given
        State state = IN_PROGRESS;
        AppRoleEntity requestedRole = roleRepository.save(RoleMappers.mapToAppRoleEntity("author"));

        AppUserEntity userLi = userRepository.save(UserMappers.mapToAppUserEntity("Li", "li", "pass"));
        AppUserEntity userKun = userRepository.save(UserMappers.mapToAppUserEntity("Kun", "kun", "pass"));
        AppUserEntity userTi = userRepository.save(UserMappers.mapToAppUserEntity("Ti", "ti", "pass"));

        List<RoleRequestEntity> expected = new ArrayList<>();
        expected.add(RoleRequestMappers.mapToRoleRequestEntity(userLi, requestedRole, state));
        expected.add(RoleRequestMappers.mapToRoleRequestEntity(userKun, requestedRole, state));
        expected.add(RoleRequestMappers.mapToRoleRequestEntity(userTi, requestedRole, state));
        underTest.saveAll(expected);

        //when
        List<RoleRequestEntity> actual = underTest.findAllByState(state);

        //then
        assertThat(expected.size()).isEqualTo(actual.size());
    }

    @Test
    void findAllByUser_shouldReturnEmptyListWhenUserIsNull() {
        //given
        //when
        List<RoleRequestEntity> actual = underTest.findAllByUser(null);

        //then
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    void findAllByUser_shouldReturnEmptyListWhenNoRoleRequestByUser() {
        //given
        AppUserEntity user = userRepository.save(UserMappers.mapToAppUserEntity("Jon", "jon", "pass"));

        AppRoleEntity requestedRole = roleRepository.save(RoleMappers.mapToAppRoleEntity("author"));

        AppUserEntity userLi = userRepository.save(UserMappers.mapToAppUserEntity("Li", "li", "pass"));
        AppUserEntity userKun = userRepository.save(UserMappers.mapToAppUserEntity("Kun", "kun", "pass"));
        AppUserEntity userTi = userRepository.save(UserMappers.mapToAppUserEntity("Ti", "ti", "pass"));

        List<RoleRequestEntity> expected = new ArrayList<>();
        expected.add(RoleRequestMappers.mapToRoleRequestEntity(userLi, requestedRole));
        expected.add(RoleRequestMappers.mapToRoleRequestEntity(userKun, requestedRole));
        expected.add(RoleRequestMappers.mapToRoleRequestEntity(userTi, requestedRole));
        underTest.saveAll(expected);

        //when
        List<RoleRequestEntity> actual = underTest.findAllByUser(user);

        //then
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    void findAllByUser_shouldReturnValidDataWhenRequestIsValid() {
        //given
        AppUserEntity user = userRepository.save(UserMappers.mapToAppUserEntity("Jon", "jon", "pass"));

        AppRoleEntity requestedRole = roleRepository.save(RoleMappers.mapToAppRoleEntity("author"));

        AppUserEntity userLi = userRepository.save(UserMappers.mapToAppUserEntity("Li", "li", "pass"));
        AppUserEntity userKun = userRepository.save(UserMappers.mapToAppUserEntity("Kun", "kun", "pass"));
        AppUserEntity userTi = userRepository.save(UserMappers.mapToAppUserEntity("Ti", "ti", "pass"));

        List<RoleRequestEntity> expected = new ArrayList<>();
        expected.add(RoleRequestMappers.mapToRoleRequestEntity(user, requestedRole));
        expected.add(RoleRequestMappers.mapToRoleRequestEntity(userLi, requestedRole));
        expected.add(RoleRequestMappers.mapToRoleRequestEntity(userKun, requestedRole));
        expected.add(RoleRequestMappers.mapToRoleRequestEntity(userTi, requestedRole));
        underTest.saveAll(expected);

        //when
        List<RoleRequestEntity> actual = underTest.findAllByUser(user);

        //then
        assertThat(actual.get(0).getUser()).isEqualTo(user);
    }
}