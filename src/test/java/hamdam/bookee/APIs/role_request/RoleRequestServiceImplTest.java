package hamdam.bookee.APIs.role_request;

import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.APIs.role.Permissions;
import hamdam.bookee.APIs.role_request.helpers.ReviewRoleRequestRequestDTO;
import hamdam.bookee.APIs.role_request.helpers.RoleIdRoleRequest;
import hamdam.bookee.APIs.role_request.helpers.RoleRequestResponseDTO;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import hamdam.bookee.tools.exceptions.pemission.LimitedPermissionException;
import hamdam.bookee.tools.exceptions.roleRequest.AlreadyHasInProgressRequestException;
import hamdam.bookee.tools.exceptions.roleRequest.IncorrectStateValueException;
import hamdam.bookee.tools.exceptions.roleRequest.NotAccessibleRequestException;
import hamdam.bookee.tools.exceptions.roleRequest.NotAllowedRoleOnRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.annotation.SecurityTestExecutionListeners;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static hamdam.bookee.APIs.role.Permissions.CREATE_ROLE_REQUEST;
import static hamdam.bookee.APIs.role.Permissions.GET_USER;
import static hamdam.bookee.APIs.role.Permissions.MONITOR_ROLE_REQUEST;
import static hamdam.bookee.APIs.role.Permissions.MONITOR_USER;
import static hamdam.bookee.APIs.role_request.State.ACCEPTED;
import static hamdam.bookee.APIs.role_request.State.DECLINED;
import static hamdam.bookee.APIs.role_request.State.IN_PROGRESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SecurityTestExecutionListeners
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class RoleRequestServiceImplTest {

    @Mock
    private RoleRequestRepository roleRequestRepository;

    @Mock
    private AppRoleRepository roleRepository;

    @Mock
    private AppUserRepository userRepository;

    @InjectMocks
    private RoleRequestServiceImpl underTest;

    @Test
    void roleRequestBelongsUser_shouldReturnFalseWhenRequestIsNotByUser() {
        //given
        AppUserEntity userLi = AppUserEntity
                .builder()
                .name("Li")
                .username("li")
                .password("pass")
                .build();
        userLi.setId(1L);
        AppUserEntity userKun = AppUserEntity
                .builder()
                .name("Kun")
                .username("kun")
                .password("pass")
                .build();
        userKun.setId(2L);
        AppRoleEntity requestedRole = AppRoleEntity.builder().roleName("role").build();
        RoleRequestEntity request = RoleRequestEntity.builder().user(userKun).requestedRole(requestedRole).build();

        //when
        boolean actual = underTest.roleRequestBelongsUser(userLi, request);

        //then
        assertThat(actual).isFalse();
    }

    @Test
    void roleRequestBelongsUser_shouldReturnTrueWhenRequestIsByUser() {
        //given
        AppUserEntity userLi = AppUserEntity
                .builder()
                .name("Kun")
                .username("kun")
                .password("pass")
                .build();
        userLi.setId(1L);
        AppRoleEntity requestedRole = AppRoleEntity.builder().roleName("author").build();
        RoleRequestEntity request = RoleRequestEntity.builder().user(userLi).requestedRole(requestedRole).build();

        //when
        boolean actual = underTest.roleRequestBelongsUser(userLi, request);

        //then
        assertThat(actual).isTrue();
    }

    @Test
    void getUserPermissions_shouldReturnEmptySetWhenUserDoesNotHavePermissions() {
        //given
        AppRoleEntity role = AppRoleEntity.builder().roleName("USER").permissions(Collections.emptySet()).build();
        AppUserEntity userLi = AppUserEntity.builder().name("Li").username("li").role(role).build();

        //when
        Set<Permissions> actual = underTest.getUserPermissions(userLi);

        //then
        assertThat(actual).isEmpty();
    }

    @Test
    void getUserPermissions_shouldReturnPermissionsWhenRequestIsValid() {
        //given
        AppRoleEntity role = AppRoleEntity.builder().roleName("role").permissions(Set.of(MONITOR_USER, GET_USER)).build();
        AppUserEntity userLi = AppUserEntity.builder().name("Li").username("li").role(role).build();

        //when
        Set<Permissions> actual = underTest.getUserPermissions(userLi);

        //then
        assertThat(actual).contains(MONITOR_USER, GET_USER);
    }

    @Test
    @WithMockUser("li")
    void postRoleRequest_throwsExceptionWhenUserNotAllowedToRequestRole() {
        //given
        AppUserEntity currUser = AppUserEntity
                .builder()
                .name("Li")
                .username("li")
                .password("pass")
                .build();
        when(userRepository.findAppUserByUsername(currUser.getUsername())).thenReturn(Optional.of(currUser));
        when(roleRequestRepository.existsByUserAndState(currUser, IN_PROGRESS)).thenReturn(true);

        //when
        //then
        assertThatThrownBy(() -> underTest.postRoleRequest(new RoleIdRoleRequest()))
                .isInstanceOf(AlreadyHasInProgressRequestException.class);
        verify(userRepository).findAppUserByUsername(currUser.getUsername());
    }

    @Test
    @WithMockUser("li")
    void postRoleRequest_throwsExceptionWhenRequestedRoleIdIsInvalid() {
        //given
        AppRoleEntity role = AppRoleEntity.builder().roleName("author").build();
        role.setId(1L);

        RoleIdRoleRequest request = new RoleIdRoleRequest(role.getId());

        AppUserEntity currUser = AppUserEntity
                .builder()
                .name("Li")
                .username("li")
                .password("pass")
                .build();
        when(userRepository.findAppUserByUsername(currUser.getUsername())).thenReturn(Optional.of(currUser));
        when(roleRequestRepository.existsByUserAndState(currUser, IN_PROGRESS)).thenReturn(false);
        when(roleRepository.findById(role.getId())).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.postRoleRequest(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(role.getId().toString());
        verify(userRepository).findAppUserByUsername(currUser.getUsername());
        verify(roleRepository).findById(role.getId());
    }

    @Test
    @WithMockUser("li")
    void postRoleRequest_throwsExceptionWhenUserDoesNotHavePermissionToRequest() {
        //given
        AppRoleEntity requestedRole = AppRoleEntity.builder().roleName("author").permissions(Collections.emptySet()).build();
        requestedRole.setId(1L);

        AppRoleEntity userRole = AppRoleEntity.builder().roleName("user").permissions(Collections.emptySet()).build();
        userRole.setId(2L);

        RoleIdRoleRequest request = new RoleIdRoleRequest(requestedRole.getId());

        AppUserEntity currUser = AppUserEntity.builder().name("Li").username("li").role(userRole).build();
        when(userRepository.findAppUserByUsername(currUser.getUsername())).thenReturn(Optional.of(currUser));
        when(roleRequestRepository.existsByUserAndState(currUser, IN_PROGRESS)).thenReturn(false);
        when(roleRepository.findById(requestedRole.getId())).thenReturn(Optional.of(requestedRole));

        //when
        //then
        assertThatThrownBy(() -> underTest.postRoleRequest(request))
                .isInstanceOf(LimitedPermissionException.class);
        verify(userRepository).findAppUserByUsername(currUser.getUsername());
    }

    @Test
    @WithMockUser("li")
    void postRoleRequest_throwsExceptionWhenUserHasAlreadyAcceptedRoleRequest() {
        //given
        AppRoleEntity requestedRole = AppRoleEntity.builder().roleName("author").permissions(Set.of(MONITOR_ROLE_REQUEST)).build();
        requestedRole.setId(1L);

        AppRoleEntity userRole = AppRoleEntity.builder().roleName("role").permissions(Set.of(CREATE_ROLE_REQUEST)).build();
        userRole.setId(2L);

        RoleIdRoleRequest request = new RoleIdRoleRequest(requestedRole.getId());

        AppUserEntity currUser = AppUserEntity.builder().name("Li").username("li").role(userRole).build();
        when(userRepository.findAppUserByUsername(currUser.getUsername())).thenReturn(Optional.of(currUser));
        when(roleRequestRepository.existsByUserAndState(currUser, IN_PROGRESS)).thenReturn(false);
        when(roleRepository.findById(requestedRole.getId())).thenReturn(Optional.of(requestedRole));

        //when
        //then
        assertThatThrownBy(() -> underTest.postRoleRequest(request))
                .isInstanceOf(NotAllowedRoleOnRequestException.class);
        verify(userRepository).findAppUserByUsername(currUser.getUsername());
    }

    @Test
    @WithMockUser("li")
    void postRoleRequest_returnValidDataWhenRequestIsValid() {
        //given
        AppRoleEntity requestedRole = AppRoleEntity.builder().roleName("author").permissions(Collections.emptySet()).build();
        requestedRole.setId(1L);
        AppRoleEntity userRole = AppRoleEntity.builder().roleName("role").permissions(Set.of(CREATE_ROLE_REQUEST)).build();
        userRole.setId(2L);

        AppUserEntity currUser = AppUserEntity.builder().name("Li").username("li").role(userRole).build();
        RoleRequestEntity roleRequestEntity = RoleRequestEntity.builder().user(currUser).requestedRole(requestedRole).state(IN_PROGRESS).build();
        RoleIdRoleRequest requestDTO = new RoleIdRoleRequest(requestedRole.getId());

        when(userRepository.findAppUserByUsername(currUser.getUsername())).thenReturn(Optional.of(currUser));
        when(roleRequestRepository.existsByUserAndState(currUser, IN_PROGRESS)).thenReturn(false);
        when(roleRepository.findById(requestedRole.getId())).thenReturn(Optional.of(requestedRole));
        when(roleRequestRepository.save(any())).thenReturn(roleRequestEntity);

        //when
        RoleRequestResponseDTO actual = underTest.postRoleRequest(requestDTO);

        //then
        verify(userRepository).findAppUserByUsername(currUser.getUsername());
        verify(roleRequestRepository).existsByUserAndState(currUser, IN_PROGRESS);
        verify(roleRepository).findById(requestedRole.getId());

        ArgumentCaptor<RoleRequestEntity> requestArgCaptor = ArgumentCaptor.forClass(RoleRequestEntity.class);
        verify(roleRequestRepository).save(requestArgCaptor.capture());
        RoleRequestEntity actualRequest = requestArgCaptor.getValue();
        verify(roleRequestRepository).save(any());

        assertThat(actual.getUser().getUsername()).isEqualTo(actualRequest.getUser().getUsername());
        assertThat(actual.getRequestedRole()).isEqualTo(actualRequest.getRequestedRole().getRoleName());
        assertThat(actual.getState()).isEqualTo(actualRequest.getState());
    }

    @Test
    @WithMockUser("li")
    void getAllRoleRequests_returnValidDataWhenUserWithoutStateAndWithoutPermission() {
        //given
        AppRoleEntity userRole = AppRoleEntity.builder().roleName("role").permissions(Set.of(GET_USER)).build();
        AppUserEntity currUser = AppUserEntity.builder().name("Li").username("li").role(userRole).build();

        AppRoleEntity authorRole = AppRoleEntity.builder().roleName("author").permissions(Collections.emptySet()).build();
        AppRoleEntity policeRole = AppRoleEntity.builder().roleName("author").permissions(Collections.emptySet()).build();

        List<RoleRequestEntity> requestEntities = new ArrayList<>();
        requestEntities.add(RoleRequestEntity.builder().user(currUser).requestedRole(authorRole).state(IN_PROGRESS).build());
        requestEntities.add(RoleRequestEntity.builder().user(currUser).requestedRole(policeRole).state(DECLINED).build());

        when(userRepository.findAppUserByUsername(currUser.getUsername())).thenReturn(Optional.of(currUser));
        when(roleRequestRepository.findAll()).thenReturn(requestEntities);
        when(roleRequestRepository.findAllByUser(currUser)).thenReturn(requestEntities);

        //when
        List<RoleRequestResponseDTO> actual = underTest.getAllRoleRequests(null);

        //then
        verify(userRepository).findAppUserByUsername(currUser.getUsername());
        verify(roleRequestRepository).findAll();
        verify(roleRequestRepository).findAllByUser(currUser);
        assertThat(actual.size()).isEqualTo(requestEntities.size());
    }

    @Test
    @WithMockUser("li")
    void getAllRoleRequests_returnValidDataWhenUserWithStateAndWithoutPermission() {
        //given
        AppRoleEntity userRole = AppRoleEntity.builder().roleName("role").permissions(Set.of(GET_USER)).build();
        AppUserEntity currUser = AppUserEntity.builder().name("Li").username("li").role(userRole).build();
        AppUserEntity userAnn = AppUserEntity.builder().name("Ann").username("ann").role(userRole).build();
        AppUserEntity userJack = AppUserEntity.builder().name("Jack").username("jackie").role(userRole).build();

        AppRoleEntity authorRole = AppRoleEntity.builder().roleName("author").permissions(Collections.emptySet()).build();
        AppRoleEntity policeRole = AppRoleEntity.builder().roleName("author").permissions(Collections.emptySet()).build();

        List<RoleRequestEntity> requestEntities = new ArrayList<>();
        requestEntities.add(RoleRequestEntity.builder().user(currUser).requestedRole(authorRole).state(IN_PROGRESS).build());
        requestEntities.add(RoleRequestEntity.builder().user(userAnn).requestedRole(authorRole).state(IN_PROGRESS).build());
        requestEntities.add(RoleRequestEntity.builder().user(userJack).requestedRole(authorRole).state(DECLINED).build());
        requestEntities.add(RoleRequestEntity.builder().user(currUser).requestedRole(policeRole).state(DECLINED).build());

        when(userRepository.findAppUserByUsername(currUser.getUsername())).thenReturn(Optional.of(currUser));
        when(roleRequestRepository.findAllByState(IN_PROGRESS)).thenReturn(List.of(requestEntities.get(0),
                requestEntities.get(1), requestEntities.get(2)));
        when(roleRequestRepository.findAllByUser(currUser)).thenReturn(List.of(requestEntities.get(0)));

        //when
        List<RoleRequestResponseDTO> actual = underTest.getAllRoleRequests(IN_PROGRESS);

        //then
        verify(userRepository).findAppUserByUsername(currUser.getUsername());
        verify(roleRequestRepository).findAllByState(IN_PROGRESS);
        verify(roleRequestRepository).findAllByUser(currUser);
        assertThat(actual.size()).isEqualTo(1);
    }

    @Test
    @WithMockUser("li")
    void getAllRoleRequests_returnValidDataWhenUserWithoutStateAndWithPermission() {
        //given
        AppRoleEntity userRole = AppRoleEntity.builder().roleName("role").permissions(Set.of(GET_USER)).build();
        AppRoleEntity authorRole = AppRoleEntity.builder().roleName("author").permissions(Collections.emptySet()).build();
        AppRoleEntity policeRole = AppRoleEntity.builder().roleName("author").permissions(Collections.emptySet()).build();

        AppUserEntity userAnn = AppUserEntity.builder().name("Ann").username("ann").role(userRole).build();
        AppUserEntity userJack = AppUserEntity.builder().name("Jack").username("jackie").role(userRole).build();
        AppUserEntity currUser = AppUserEntity
                .builder()
                .name("Li")
                .username("li")
                .role(AppRoleEntity.builder().roleName("role").permissions(Set.of(MONITOR_ROLE_REQUEST)).build())
                .build();

        List<RoleRequestEntity> requestEntities = new ArrayList<>();
        requestEntities.add(RoleRequestEntity.builder().user(currUser).requestedRole(authorRole).state(ACCEPTED).build());
        requestEntities.add(RoleRequestEntity.builder().user(userAnn).requestedRole(authorRole).state(IN_PROGRESS).build());
        requestEntities.add(RoleRequestEntity.builder().user(userJack).requestedRole(authorRole).state(IN_PROGRESS).build());
        requestEntities.add(RoleRequestEntity.builder().user(userAnn).requestedRole(policeRole).state(DECLINED).build());

        when(userRepository.findAppUserByUsername(currUser.getUsername())).thenReturn(Optional.of(currUser));
        when(roleRequestRepository.findAll()).thenReturn(requestEntities);

        //when
        List<RoleRequestResponseDTO> actual = underTest.getAllRoleRequests(null);

        //then
        verify(userRepository).findAppUserByUsername(currUser.getUsername());
        verify(roleRequestRepository).findAll();
        assertThat(actual.size()).isEqualTo(requestEntities.size());
    }

    @Test
    @WithMockUser("li")
    void getAllRoleRequests_returnValidDataWhenUserWithStateAndWithPermission() {
        //given
        AppRoleEntity userRole = AppRoleEntity.builder().roleName("role").permissions(Set.of(GET_USER)).build();
        AppRoleEntity authorRole = AppRoleEntity.builder().roleName("author").permissions(Collections.emptySet()).build();
        AppRoleEntity policeRole = AppRoleEntity.builder().roleName("author").permissions(Collections.emptySet()).build();

        AppUserEntity userAnn = AppUserEntity.builder().name("Ann").username("ann").role(userRole).build();
        AppUserEntity userJack = AppUserEntity.builder().name("Jack").username("jackie").role(userRole).build();
        AppUserEntity currUser = AppUserEntity
                .builder()
                .name("Li")
                .username("li")
                .role(AppRoleEntity.builder().roleName("role").permissions(Set.of(MONITOR_ROLE_REQUEST)).build())
                .build();

        List<RoleRequestEntity> requestEntities = new ArrayList<>();
        requestEntities.add(RoleRequestEntity.builder().user(currUser).requestedRole(authorRole).state(ACCEPTED).build());
        requestEntities.add(RoleRequestEntity.builder().user(userAnn).requestedRole(authorRole).state(IN_PROGRESS).build());
        requestEntities.add(RoleRequestEntity.builder().user(userJack).requestedRole(authorRole).state(IN_PROGRESS).build());
        requestEntities.add(RoleRequestEntity.builder().user(userAnn).requestedRole(policeRole).state(DECLINED).build());

        when(userRepository.findAppUserByUsername(currUser.getUsername())).thenReturn(Optional.of(currUser));
        when(roleRequestRepository.findAllByState(IN_PROGRESS)).thenReturn(List.of(requestEntities.get(1), requestEntities.get(2)));

        //when
        List<RoleRequestResponseDTO> actual = underTest.getAllRoleRequests(IN_PROGRESS);

        //then
        verify(userRepository).findAppUserByUsername(currUser.getUsername());
        verify(roleRequestRepository).findAllByState(IN_PROGRESS);
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void reviewRequest_throwsExceptionWhenRequestIdIsInvalid() {
        //given
        Long id = 1L;
        when(roleRequestRepository.findById(id)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.reviewRequest(id, new ReviewRoleRequestRequestDTO()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(id.toString());
        verify(roleRequestRepository).findById(id);
    }

    @Test
    @WithMockUser("ann")
    void reviewRequest_throwsExceptionWhenUserHasLimitedPermission() {
        //given
        Long id = 1L;
        AppRoleEntity userRole = AppRoleEntity.builder().roleName("author").permissions(Collections.emptySet()).build();
        AppUserEntity userAnn = AppUserEntity.builder().name("Ann").username("ann").role(userRole).build();

        when(roleRequestRepository.findById(id)).thenReturn(Optional.of(new RoleRequestEntity()));
        when(userRepository.findAppUserByUsername(userAnn.getUsername())).thenReturn(Optional.of(userAnn));

        //when
        //then
        assertThatThrownBy(() -> underTest.reviewRequest(id, new ReviewRoleRequestRequestDTO()))
                .isInstanceOf(LimitedPermissionException.class);
        verify(roleRequestRepository).findById(id);
        verify(userRepository).findAppUserByUsername(userAnn.getUsername());
    }

    @Test
    @WithMockUser("ann")
    void reviewRequest_throwsExceptionWhenReviewStateIsInvalid() {
        //given
        Long id = 1L;
        AppRoleEntity userRole = AppRoleEntity.builder().roleName("role").permissions(Set.of(MONITOR_ROLE_REQUEST)).build();
        AppUserEntity userAnn = AppUserEntity.builder().name("Ann").username("ann").role(userRole).build();

        when(roleRequestRepository.findById(id)).thenReturn(Optional.of(new RoleRequestEntity()));
        when(userRepository.findAppUserByUsername(userAnn.getUsername())).thenReturn(Optional.of(userAnn));

        //when
        //then
        assertThatThrownBy(() -> underTest.reviewRequest(id, new ReviewRoleRequestRequestDTO(IN_PROGRESS, null)))
                .isInstanceOf(IncorrectStateValueException.class);
        verify(roleRequestRepository).findById(id);
        verify(userRepository).findAppUserByUsername(userAnn.getUsername());
    }

    @Test
    @WithMockUser("ann")
    void reviewRequest_returnValidDataWhenStateIsDeclinedWithoutDescription() {
        //given
        Long id = 1L;
        AppRoleEntity userRole = AppRoleEntity.builder().roleName("role").permissions(Set.of(MONITOR_ROLE_REQUEST)).build();
        AppRoleEntity requestedRole = AppRoleEntity.builder().roleName("role").permissions(Set.of(GET_USER)).build();
        AppUserEntity userAnn = AppUserEntity.builder().name("Ann").username("ann").role(userRole).build();
        RoleRequestEntity request = RoleRequestEntity.builder().user(userAnn).requestedRole(requestedRole).state(IN_PROGRESS).build();

        when(roleRequestRepository.findById(id)).thenReturn(Optional.of(request));
        when(userRepository.findAppUserByUsername(userAnn.getUsername())).thenReturn(Optional.of(userAnn));

        //when
        RoleRequestResponseDTO actual = underTest.reviewRequest(id, new ReviewRoleRequestRequestDTO(DECLINED, null));

        //then
        verify(roleRequestRepository).findById(id);
        verify(userRepository).findAppUserByUsername(userAnn.getUsername());
        verify(roleRequestRepository).save(request);
        assertThat(actual.getUser().getUsername()).isEqualTo(userAnn.getUsername());
        assertThat(actual.getRequestedRole()).isEqualTo(requestedRole.getRoleName());
        assertThat(actual.getState()).isEqualTo(DECLINED);
    }

    @Test
    @WithMockUser("ann")
    void reviewRequest_returnValidDataWhenStateIsAcceptedWithDescription() {
        //given
        Long id = 1L;
        AppRoleEntity userRole = AppRoleEntity.builder().roleName("role").permissions(Set.of(MONITOR_ROLE_REQUEST)).build();
        AppRoleEntity requestedRole = AppRoleEntity.builder().roleName("role").permissions(Set.of(GET_USER)).build();
        AppUserEntity userAnn = AppUserEntity.builder().name("Ann").username("ann").role(userRole).build();
        RoleRequestEntity request = RoleRequestEntity.builder().user(userAnn).requestedRole(requestedRole).state(IN_PROGRESS).build();


        when(roleRequestRepository.findById(id)).thenReturn(Optional.of(request));
        when(userRepository.findAppUserByUsername(userAnn.getUsername())).thenReturn(Optional.of(userAnn));

        //when
        RoleRequestResponseDTO actual = underTest.reviewRequest(id, new ReviewRoleRequestRequestDTO(ACCEPTED, "nice"));

        //then
        verify(roleRequestRepository).findById(id);
        verify(userRepository).findAppUserByUsername(userAnn.getUsername());
        verify(roleRequestRepository).save(request);
        assertThat(actual.getUser().getUsername()).isEqualTo(userAnn.getUsername());
        assertThat(actual.getRequestedRole()).isEqualTo(requestedRole.getRoleName());
        assertThat(actual.getState()).isEqualTo(ACCEPTED);
        assertThat(actual.getDescription()).isEqualTo("nice");
    }

    @Test
    void deleteRequest_throwsExceptionWhenRequestIdIsInvalid() {
        Long id = 1L;
        when(roleRequestRepository.findById(id)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.deleteRequest(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(id.toString());
        verify(roleRequestRepository).findById(id);
    }

    @Test
    @WithMockUser("ann")
    void deleteRequest_throwsExceptionWhenUserDoesNotHaveAccess() {
        Long id = 1L;
        AppRoleEntity userRole = AppRoleEntity.builder().roleName("author").permissions(Collections.emptySet()).build();
        AppUserEntity userAnn = AppUserEntity.builder().name("Ann").username("ann").role(userRole).build();
        userAnn.setId(2L);
        AppUserEntity userJon = AppUserEntity
                .builder()
                .name("John")
                .username("snow")
                .role(userRole)
                .build();
        userJon.setId(3L);
        RoleRequestEntity roleRequestEntity = RoleRequestEntity.builder().user(userJon).requestedRole(userRole).state(IN_PROGRESS).build();

        when(roleRequestRepository.findById(id)).thenReturn(Optional.of(roleRequestEntity));
        when(userRepository.findAppUserByUsername(userAnn.getUsername())).thenReturn(Optional.of(userAnn));

        //when
        //then
        assertThatThrownBy(() -> underTest.deleteRequest(id))
                .isInstanceOf(NotAccessibleRequestException.class);
        verify(roleRequestRepository).findById(id);
    }

    @Test
    @WithMockUser("ann")
    void deleteRequest_deletesDataWhenUserOwnsRequest() {
        Long id = 1L;
        AppRoleEntity userRole = AppRoleEntity.builder().roleName("author").permissions(Collections.emptySet()).build();
        AppUserEntity userAnn = AppUserEntity.builder().name("Ann").username("ann").role(userRole).build();
        userAnn.setId(2L);
        RoleRequestEntity roleRequestEntity = RoleRequestEntity.builder().user(userAnn).requestedRole(userRole).state(IN_PROGRESS).build();

        when(roleRequestRepository.findById(id)).thenReturn(Optional.of(roleRequestEntity));
        when(userRepository.findAppUserByUsername(userAnn.getUsername())).thenReturn(Optional.of(userAnn));

        //when
        underTest.deleteRequest(id);

        //then
        verify(roleRequestRepository).findById(id);
        verify(roleRequestRepository).deleteById(id);
    }

    @Test
    @WithMockUser("ann")
    void deleteRequest_deletesDataWhenUserHasMonitoringPermission() {
        Long id = 1L;
        AppRoleEntity userRole = AppRoleEntity.builder().roleName("role").permissions(Set.of(MONITOR_ROLE_REQUEST)).build();
        AppUserEntity userAnn = AppUserEntity.builder().name("Ann").username("ann").role(userRole).build();
        userAnn.setId(2L);
        RoleRequestEntity roleRequestEntity = RoleRequestEntity.builder().user(userAnn).requestedRole(userRole).state(IN_PROGRESS).build();

        when(roleRequestRepository.findById(id)).thenReturn(Optional.of(roleRequestEntity));
        when(userRepository.findAppUserByUsername(userAnn.getUsername())).thenReturn(Optional.of(userAnn));

        //when
        underTest.deleteRequest(id);

        //then
        verify(roleRequestRepository).findById(id);
        verify(roleRequestRepository).deleteById(id);
    }
}