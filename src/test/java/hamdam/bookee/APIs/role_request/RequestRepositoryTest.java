package hamdam.bookee.APIs.role_request;

import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.APIs.role.helpers.RoleMappers;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
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
class RequestRepositoryTest {

    @Autowired
    private RequestRepository underTest;

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
        AppUserEntity userLi = userRepository.save(new AppUserEntity("Li", "li", "pass"));

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
        AppUserEntity userLi = userRepository.save(new AppUserEntity("Li", "li", "pass"));

        //when
        boolean actual = underTest.existsByUserAndState(userLi, IN_PROGRESS);

        //then
        assertThat(actual).isFalse();
    }

    @Test
    void existsByUserAndState_shouldReturnFalseWhenUserDoesNotHaveInProgressRoleRequest() {
        //given
        AppUserEntity userLi = userRepository.save(new AppUserEntity("Li", "li", "pass"));
        AppRoleEntity requestedRole = roleRepository.save(RoleMappers.mapToAppRoleEntity("author"));

        underTest.save(new RequestEntity(userLi, requestedRole, DECLINED));

        //when
        boolean actual = underTest.existsByUserAndState(userLi, IN_PROGRESS);

        //then
        assertThat(actual).isFalse();
    }

    @Test
    void existsByUserAndState_shouldReturnTrueWhenUserHasInProgressRoleRequest() {
        //given
        AppUserEntity userLi = userRepository.save(new AppUserEntity("Li", "li", "pass"));
        AppRoleEntity requestedRole = roleRepository.save(RoleMappers.mapToAppRoleEntity("author"));

        underTest.save(new RequestEntity(userLi, requestedRole, IN_PROGRESS));

        //when
        boolean actual = underTest.existsByUserAndState(userLi, IN_PROGRESS);

        //then
        assertThat(actual).isTrue();
    }

    @Test
    void findAllByState_shouldReturnEmptyListWhenStateIsNull() {
        //given
        //when
        List<RequestEntity> actual = underTest.findAllByState(null);

        //then
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    void findAllByState_shouldReturnEmptyListWhenNoRoleRequestByState() {
        //given
        //when
        List<RequestEntity> actual = underTest.findAllByState(IN_PROGRESS);

        //then
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    void findAllByState_shouldReturnValidDataWhenRequestIsValid() {
        //given
        State state = IN_PROGRESS;
        AppRoleEntity requestedRole = roleRepository.save(RoleMappers.mapToAppRoleEntity("author"));

        AppUserEntity userLi = userRepository.save(new AppUserEntity("Li", "li", "pass"));
        AppUserEntity userKun = userRepository.save(new AppUserEntity("Kun", "kun", "pass"));
        AppUserEntity userTi = userRepository.save(new AppUserEntity("Ti", "ti", "pass"));

        List<RequestEntity> expected = new ArrayList<>();
        expected.add(new RequestEntity(userLi, requestedRole, state));
        expected.add(new RequestEntity(userKun, requestedRole, state));
        expected.add(new RequestEntity(userTi, requestedRole, state));
        underTest.saveAll(expected);

        //when
        List<RequestEntity> actual = underTest.findAllByState(state);

        //then
        assertThat(expected.size()).isEqualTo(actual.size());
    }

    @Test
    void findAllByUser_shouldReturnEmptyListWhenUserIsNull() {
        //given
        //when
        List<RequestEntity> actual = underTest.findAllByUser(null);

        //then
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    void findAllByUser_shouldReturnEmptyListWhenNoRoleRequestByUser() {
        //given
        AppUserEntity user = userRepository.save(new AppUserEntity("Jon", "jon", "pass"));

        AppRoleEntity requestedRole = roleRepository.save(RoleMappers.mapToAppRoleEntity("author"));

        AppUserEntity userLi = userRepository.save(new AppUserEntity("Li", "li", "pass"));
        AppUserEntity userKun = userRepository.save(new AppUserEntity("Kun", "kun", "pass"));
        AppUserEntity userTi = userRepository.save(new AppUserEntity("Ti", "ti", "pass"));

        List<RequestEntity> expected = new ArrayList<>();
        expected.add(new RequestEntity(userLi, requestedRole));
        expected.add(new RequestEntity(userKun, requestedRole));
        expected.add(new RequestEntity(userTi, requestedRole));
        underTest.saveAll(expected);

        //when
        List<RequestEntity> actual = underTest.findAllByUser(user);

        //then
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    void findAllByUser_shouldReturnValidDataWhenRequestIsValid() {
        //given
        AppUserEntity user = userRepository.save(new AppUserEntity("Jon", "jon", "pass"));

        AppRoleEntity requestedRole = roleRepository.save(RoleMappers.mapToAppRoleEntity("author"));

        AppUserEntity userLi = userRepository.save(new AppUserEntity("Li", "li", "pass"));
        AppUserEntity userKun = userRepository.save(new AppUserEntity("Kun", "kun", "pass"));
        AppUserEntity userTi = userRepository.save(new AppUserEntity("Ti", "ti", "pass"));

        List<RequestEntity> expected = new ArrayList<>();
        expected.add(new RequestEntity(user, requestedRole));
        expected.add(new RequestEntity(userLi, requestedRole));
        expected.add(new RequestEntity(userKun, requestedRole));
        expected.add(new RequestEntity(userTi, requestedRole));
        underTest.saveAll(expected);

        //when
        List<RequestEntity> actual = underTest.findAllByUser(user);

        //then
        assertThat(actual.get(0).getUser()).isEqualTo(user);
    }
}