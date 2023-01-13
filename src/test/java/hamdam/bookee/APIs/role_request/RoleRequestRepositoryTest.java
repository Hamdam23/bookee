package hamdam.bookee.APIs.role_request;

import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.AppRoleRepository;
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
        AppUserEntity userLi = userRepository.save(AppUserEntity
                .builder()
                .name("name")
                .username("username")
                .password("pass")
                .build());

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
        AppUserEntity userLi = userRepository.save(AppUserEntity
                .builder()
                .name("name")
                .username("username")
                .password("pass")
                .build());

        //when
        boolean actual = underTest.existsByUserAndState(userLi, IN_PROGRESS);

        //then
        assertThat(actual).isFalse();
    }

    @Test
    void existsByUserAndState_shouldReturnFalseWhenUserDoesNotHaveInProgressRoleRequest() {
        //given
        AppUserEntity userLi = userRepository.save(AppUserEntity
                .builder()
                .name("name")
                .username("username")
                .password("pass")
                .build());
        AppRoleEntity requestedRole = roleRepository.save(AppRoleEntity.builder().roleName("role").build());

        underTest.save(RoleRequestEntity.builder().user(userLi).requestedRole(requestedRole).state(DECLINED).build());

        //when
        boolean actual = underTest.existsByUserAndState(userLi, IN_PROGRESS);

        //then
        assertThat(actual).isFalse();
    }

    @Test
    void existsByUserAndState_shouldReturnTrueWhenUserHasInProgressRoleRequest() {
        //given
        AppUserEntity userLi = userRepository.save(AppUserEntity
                .builder()
                .name("name")
                .username("username")
                .password("pass")
                .build());
        AppRoleEntity requestedRole = roleRepository.save(AppRoleEntity.builder().roleName("role").build());

        underTest.save(RoleRequestEntity.builder().user(userLi).requestedRole(requestedRole).state(IN_PROGRESS).build());

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
        AppRoleEntity requestedRole = roleRepository.save(AppRoleEntity.builder().roleName("role").build());

        AppUserEntity userLi = userRepository.save(AppUserEntity
                .builder()
                .name("name")
                .username("username")
                .password("pass")
                .build());
        AppUserEntity userKun = userRepository.save(AppUserEntity
                .builder()
                .name("Kun")
                .username("kun")
                .password("pass")
                .build());
        AppUserEntity userTi = userRepository.save(AppUserEntity
                .builder()
                .name("Ti")
                .username("ti")
                .password("pass")
                .build());

        List<RoleRequestEntity> expected = new ArrayList<>();
        expected.add(RoleRequestEntity.builder().user(userLi).requestedRole(requestedRole).state(state).build());
        expected.add(RoleRequestEntity.builder().user(userKun).requestedRole(requestedRole).state(state).build());
        expected.add(RoleRequestEntity.builder().user(userTi).requestedRole(requestedRole).state(state).build());
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
        AppUserEntity user = userRepository.save(AppUserEntity
                .builder()
                .name("John")
                .username("john")
                .password("pass")
                .build());

        AppRoleEntity requestedRole = roleRepository.save(AppRoleEntity.builder().roleName("role").build());

        AppUserEntity userLi = userRepository.save(AppUserEntity
                .builder()
                .name("name")
                .username("username")
                .password("pass")
                .build());
        AppUserEntity userKun = userRepository.save(AppUserEntity
                .builder()
                .name("li")
                .username("Li")
                .password("pass")
                .build());
        AppUserEntity userTi = userRepository.save(AppUserEntity
                .builder()
                .name("Ti")
                .username("ti")
                .password("pass")
                .build());

        List<RoleRequestEntity> expected = new ArrayList<>();
        expected.add(RoleRequestEntity.builder().user(userLi).requestedRole(requestedRole).build());
        expected.add(RoleRequestEntity.builder().user(userKun).requestedRole(requestedRole).build());
        expected.add(RoleRequestEntity.builder().user(userTi).requestedRole(requestedRole).build());
        underTest.saveAll(expected);

        //when
        List<RoleRequestEntity> actual = underTest.findAllByUser(user);

        //then
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    void findAllByUser_shouldReturnValidDataWhenRequestIsValid() {
        //given
        AppUserEntity user = userRepository.save(AppUserEntity
                .builder()
                .name("John")
                .username("john")
                .password("pass")
                .build());

        AppRoleEntity requestedRole = roleRepository.save(AppRoleEntity.builder().roleName("role").build());

        AppUserEntity userLi = userRepository.save(AppUserEntity
                .builder()
                .name("name")
                .username("username")
                .password("pass")
                .build());
        AppUserEntity userKun = userRepository.save(AppUserEntity
                .builder()
                .name("Kun")
                .username("kun")
                .password("pass")
                .build());
        AppUserEntity userTi = userRepository.save(AppUserEntity
                .builder()
                .name("Ti")
                .username("ti")
                .password("pass")
                .build());

        List<RoleRequestEntity> expected = new ArrayList<>();
        expected.add(RoleRequestEntity.builder().user(user).requestedRole(requestedRole).build());
        expected.add(RoleRequestEntity.builder().user(userLi).requestedRole(requestedRole).build());
        expected.add(RoleRequestEntity.builder().user(userKun).requestedRole(requestedRole).build());
        expected.add(RoleRequestEntity.builder().user(userTi).requestedRole(requestedRole).build());
        underTest.saveAll(expected);

        //when
        List<RoleRequestEntity> actual = underTest.findAllByUser(user);

        //then
        assertThat(actual.get(0).getUser()).isEqualTo(user);
    }
}