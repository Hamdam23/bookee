package hamdam.bookee.APIs.role_request;

import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.APIs.role.Permissions;
import hamdam.bookee.APIs.role.helpers.RoleMappers;
import hamdam.bookee.APIs.role_request.helpers.ReviewRequestDTO;
import hamdam.bookee.APIs.role_request.helpers.RoleRequestDTO;
import hamdam.bookee.APIs.role_request.helpers.RoleRequestMappers;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.APIs.user.helpers.UserMappers;
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

import java.util.*;

import static hamdam.bookee.APIs.role.Permissions.*;
import static hamdam.bookee.APIs.role_request.State.*;
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
        AppUserEntity userLi = UserMappers.mapToAppUserEntity("Li", "li", "pass");
        userLi.setId(1L);
        AppUserEntity userKun = UserMappers.mapToAppUserEntity("Kun", "kun", "pass");
        userKun.setId(2L);
        AppRoleEntity requestedRole = RoleMappers.mapToAppRoleEntity("author");
        RoleRequestEntity request = RoleRequestMappers.mapToRoleRequestEntity(userKun, requestedRole);

        //when
        boolean actual = underTest.roleRequestBelongsUser(userLi, request);

        //then
        assertThat(actual).isFalse();
    }

    @Test
    void roleRequestBelongsUser_shouldReturnTrueWhenRequestIsByUser() {
        //given
        AppUserEntity userLi = UserMappers.mapToAppUserEntity("Li", "li", "pass");
        userLi.setId(1L);
        AppRoleEntity requestedRole = RoleMappers.mapToAppRoleEntity("author");
        RoleRequestEntity request = RoleRequestMappers.mapToRoleRequestEntity(userLi, requestedRole);

        //when
        boolean actual = underTest.roleRequestBelongsUser(userLi, request);

        //then
        assertThat(actual).isTrue();
    }

    @Test
    void getUserPermissions_shouldReturnEmptySetWhenUserDoesNotHavePermissions() {
        //given
        AppRoleEntity role = RoleMappers.mapToAppRoleEntity("author", Collections.emptySet());
        AppUserEntity userLi = UserMappers.mapToAppUserEntity("Li", "li", role);

        //when
        Set<Permissions> actual = underTest.getUserPermissions(userLi);

        //then
        assertThat(actual).isEmpty();
    }

    @Test
    void getUserPermissions_shouldReturnPermissionsWhenRequestIsValid() {
        //given
        AppRoleEntity role = RoleMappers.mapToAppRoleEntity("author", Set.of(MONITOR_USER, GET_USER));
        AppUserEntity userLi = UserMappers.mapToAppUserEntity("Li", "li", role);

        //when
        Set<Permissions> actual = underTest.getUserPermissions(userLi);

        //then
        assertThat(actual).contains(MONITOR_USER, GET_USER);
    }

    @Test
    @WithMockUser("li")
    void postRoleRequest_throwsExceptionWhenUserNotAllowedToRequestRole() {
        //given
        AppUserEntity currUser = UserMappers.mapToAppUserEntity("Li", "li", "pass");
        when(userRepository.findAppUserByUsername(currUser.getUsername())).thenReturn(Optional.of(currUser));
        when(roleRequestRepository.existsByUserAndState(currUser, IN_PROGRESS)).thenReturn(true);

        //when
        //then
        assertThatThrownBy(() -> underTest.postRoleRequest(new RoleRequestDTO()))
                .isInstanceOf(AlreadyHasInProgressRequestException.class);
        verify(userRepository).findAppUserByUsername(currUser.getUsername());
    }

    @Test
    @WithMockUser("li")
    void postRoleRequest_throwsExceptionWhenRequestedRoleIdIsInvalid() {
        //given
        AppRoleEntity role = RoleMappers.mapToAppRoleEntity("author");
        role.setId(1L);

        RoleRequestDTO request = new RoleRequestDTO(role.getId());

        AppUserEntity currUser = UserMappers.mapToAppUserEntity("Li", "li", "pass");
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
        AppRoleEntity requestedRole = RoleMappers.mapToAppRoleEntity("author", Collections.emptySet());
        requestedRole.setId(1L);

        AppRoleEntity userRole = RoleMappers.mapToAppRoleEntity("user", Collections.emptySet());
        userRole.setId(2L);

        RoleRequestDTO request = new RoleRequestDTO(requestedRole.getId());

        AppUserEntity currUser = UserMappers.mapToAppUserEntity("Li", "li", userRole);
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
        AppRoleEntity requestedRole = RoleMappers.mapToAppRoleEntity("author", Set.of(MONITOR_ROLE_REQUEST));
        requestedRole.setId(1L);

        AppRoleEntity userRole = RoleMappers.mapToAppRoleEntity("user", Set.of(CREATE_ROLE_REQUEST));
        userRole.setId(2L);

        RoleRequestDTO request = new RoleRequestDTO(requestedRole.getId());

        AppUserEntity currUser = UserMappers.mapToAppUserEntity("Li", "li", userRole);
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
        AppRoleEntity requestedRole = RoleMappers.mapToAppRoleEntity("author", Collections.emptySet());
        requestedRole.setId(1L);
        AppRoleEntity userRole = RoleMappers.mapToAppRoleEntity("user", Set.of(CREATE_ROLE_REQUEST));
        userRole.setId(2L);

        AppUserEntity currUser = UserMappers.mapToAppUserEntity("Li", "li", userRole);
        RoleRequestEntity roleRequestEntity = RoleRequestMappers.mapToRoleRequestEntity(currUser, requestedRole, State.IN_PROGRESS);
        RoleRequestDTO requestDTO = new RoleRequestDTO(requestedRole.getId());

        when(userRepository.findAppUserByUsername(currUser.getUsername())).thenReturn(Optional.of(currUser));
        when(roleRequestRepository.existsByUserAndState(currUser, IN_PROGRESS)).thenReturn(false);
        when(roleRepository.findById(requestedRole.getId())).thenReturn(Optional.of(requestedRole));
        when(roleRequestRepository.save(any())).thenReturn(roleRequestEntity);

        //when
        RoleRequestResponse actual = underTest.postRoleRequest(requestDTO);

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
        AppRoleEntity userRole = RoleMappers.mapToAppRoleEntity("user", Set.of(GET_USER));
        AppUserEntity currUser = UserMappers.mapToAppUserEntity("Li", "li", userRole);

        AppRoleEntity authorRole = RoleMappers.mapToAppRoleEntity("author", Collections.emptySet());
        AppRoleEntity policeRole = RoleMappers.mapToAppRoleEntity("author", Collections.emptySet());

        List<RoleRequestEntity> requestEntities = new ArrayList<>();
        requestEntities.add(RoleRequestMappers.mapToRoleRequestEntity(currUser, authorRole, State.IN_PROGRESS));
        requestEntities.add(RoleRequestMappers.mapToRoleRequestEntity(currUser, policeRole, DECLINED));

        when(userRepository.findAppUserByUsername(currUser.getUsername())).thenReturn(Optional.of(currUser));
        when(roleRequestRepository.findAll()).thenReturn(requestEntities);
        when(roleRequestRepository.findAllByUser(currUser)).thenReturn(requestEntities);

        //when
        List<RoleRequestResponse> actual = underTest.getAllRoleRequests(null);

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
        AppRoleEntity userRole = RoleMappers.mapToAppRoleEntity("user", Set.of(GET_USER));
        AppUserEntity currUser = UserMappers.mapToAppUserEntity("Li", "li", userRole);
        AppUserEntity userAnn = UserMappers.mapToAppUserEntity("Ann", "ann", userRole);
        AppUserEntity userJack = UserMappers.mapToAppUserEntity("Jack", "jackie", userRole);

        AppRoleEntity authorRole = RoleMappers.mapToAppRoleEntity("author", Collections.emptySet());
        AppRoleEntity policeRole = RoleMappers.mapToAppRoleEntity("author", Collections.emptySet());

        List<RoleRequestEntity> requestEntities = new ArrayList<>();
        requestEntities.add(RoleRequestMappers.mapToRoleRequestEntity(currUser, authorRole, IN_PROGRESS));
        requestEntities.add(RoleRequestMappers.mapToRoleRequestEntity(userAnn, authorRole, IN_PROGRESS));
        requestEntities.add(RoleRequestMappers.mapToRoleRequestEntity(userJack, authorRole, IN_PROGRESS));
        requestEntities.add(RoleRequestMappers.mapToRoleRequestEntity(currUser, policeRole, DECLINED));

        when(userRepository.findAppUserByUsername(currUser.getUsername())).thenReturn(Optional.of(currUser));
        when(roleRequestRepository.findAllByState(IN_PROGRESS)).thenReturn(List.of(requestEntities.get(0),
                requestEntities.get(1), requestEntities.get(2)));
        when(roleRequestRepository.findAllByUser(currUser)).thenReturn(List.of(requestEntities.get(0)));

        //when
        List<RoleRequestResponse> actual = underTest.getAllRoleRequests(IN_PROGRESS);

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
        AppRoleEntity userRole = RoleMappers.mapToAppRoleEntity("user", Set.of(GET_USER));
        AppRoleEntity authorRole = RoleMappers.mapToAppRoleEntity("author", Collections.emptySet());
        AppRoleEntity policeRole = RoleMappers.mapToAppRoleEntity("author", Collections.emptySet());

        AppUserEntity userAnn = UserMappers.mapToAppUserEntity("Ann", "ann", userRole);
        AppUserEntity userJack = UserMappers.mapToAppUserEntity("Jack", "jackie", userRole);
        AppUserEntity currUser = UserMappers.mapToAppUserEntity(
                "Li", "li", RoleMappers.mapToAppRoleEntity("user", Set.of(MONITOR_ROLE_REQUEST)));

        List<RoleRequestEntity> requestEntities = new ArrayList<>();
        requestEntities.add(RoleRequestMappers.mapToRoleRequestEntity(currUser, authorRole, ACCEPTED));
        requestEntities.add(RoleRequestMappers.mapToRoleRequestEntity(userAnn, authorRole, IN_PROGRESS));
        requestEntities.add(RoleRequestMappers.mapToRoleRequestEntity(userJack, authorRole, IN_PROGRESS));
        requestEntities.add(RoleRequestMappers.mapToRoleRequestEntity(userAnn, policeRole, DECLINED));

        when(userRepository.findAppUserByUsername(currUser.getUsername())).thenReturn(Optional.of(currUser));
        when(roleRequestRepository.findAll()).thenReturn(requestEntities);

        //when
        List<RoleRequestResponse> actual = underTest.getAllRoleRequests(null);

        //then
        verify(userRepository).findAppUserByUsername(currUser.getUsername());
        verify(roleRequestRepository).findAll();
        assertThat(actual.size()).isEqualTo(requestEntities.size());
    }

    @Test
    @WithMockUser("li")
    void getAllRoleRequests_returnValidDataWhenUserWithStateAndWithPermission() {
        //given
        AppRoleEntity userRole = RoleMappers.mapToAppRoleEntity("user", Set.of(GET_USER));
        AppRoleEntity authorRole = RoleMappers.mapToAppRoleEntity("author", Collections.emptySet());
        AppRoleEntity policeRole = RoleMappers.mapToAppRoleEntity("author", Collections.emptySet());

        AppUserEntity userAnn = UserMappers.mapToAppUserEntity("Ann", "ann", userRole);
        AppUserEntity userJack = UserMappers.mapToAppUserEntity("Jack", "jackie", userRole);
        AppUserEntity currUser = UserMappers.mapToAppUserEntity(
                "Li", "li", RoleMappers.mapToAppRoleEntity("user", Set.of(MONITOR_ROLE_REQUEST)));

        List<RoleRequestEntity> requestEntities = new ArrayList<>();
        requestEntities.add(RoleRequestMappers.mapToRoleRequestEntity(currUser, authorRole, ACCEPTED));
        requestEntities.add(RoleRequestMappers.mapToRoleRequestEntity(userAnn, authorRole, IN_PROGRESS));
        requestEntities.add(RoleRequestMappers.mapToRoleRequestEntity(userJack, authorRole, IN_PROGRESS));
        requestEntities.add(RoleRequestMappers.mapToRoleRequestEntity(userAnn, policeRole, DECLINED));

        when(userRepository.findAppUserByUsername(currUser.getUsername())).thenReturn(Optional.of(currUser));
        when(roleRequestRepository.findAllByState(IN_PROGRESS)).thenReturn(List.of(requestEntities.get(1), requestEntities.get(2)));

        //when
        List<RoleRequestResponse> actual = underTest.getAllRoleRequests(IN_PROGRESS);

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
        assertThatThrownBy(() -> underTest.reviewRequest(id, new ReviewRequestDTO()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(id.toString());
        verify(roleRequestRepository).findById(id);
    }

    @Test
    @WithMockUser("ann")
    void reviewRequest_throwsExceptionWhenUserHasLimitedPermission() {
        //given
        Long id = 1L;
        AppRoleEntity userRole = RoleMappers.mapToAppRoleEntity("user", Collections.emptySet());
        AppUserEntity userAnn = UserMappers.mapToAppUserEntity("Ann", "ann", userRole);

        when(roleRequestRepository.findById(id)).thenReturn(Optional.of(new RoleRequestEntity()));
        when(userRepository.findAppUserByUsername(userAnn.getUsername())).thenReturn(Optional.of(userAnn));

        //when
        //then
        assertThatThrownBy(() -> underTest.reviewRequest(id, new ReviewRequestDTO()))
                .isInstanceOf(LimitedPermissionException.class);
        verify(roleRequestRepository).findById(id);
        verify(userRepository).findAppUserByUsername(userAnn.getUsername());
    }

    @Test
    @WithMockUser("ann")
    void reviewRequest_throwsExceptionWhenReviewStateIsInvalid() {
        //given
        Long id = 1L;
        AppRoleEntity userRole = RoleMappers.mapToAppRoleEntity("user", Set.of(MONITOR_ROLE_REQUEST));
        AppUserEntity userAnn = UserMappers.mapToAppUserEntity("Ann", "ann", userRole);

        when(roleRequestRepository.findById(id)).thenReturn(Optional.of(new RoleRequestEntity()));
        when(userRepository.findAppUserByUsername(userAnn.getUsername())).thenReturn(Optional.of(userAnn));

        //when
        //then
        assertThatThrownBy(() -> underTest.reviewRequest(id, RoleRequestMappers.mapToReviewRequestDTO(IN_PROGRESS)))
                .isInstanceOf(IncorrectStateValueException.class);
        verify(roleRequestRepository).findById(id);
        verify(userRepository).findAppUserByUsername(userAnn.getUsername());
    }

    @Test
    @WithMockUser("ann")
    void reviewRequest_returnValidDataWhenStateIsDeclinedWithoutDescription() {
        //given
        Long id = 1L;
        AppRoleEntity userRole = RoleMappers.mapToAppRoleEntity("user", Set.of(MONITOR_ROLE_REQUEST));
        AppRoleEntity requestedRole = RoleMappers.mapToAppRoleEntity("author", Set.of(GET_USER));
        AppUserEntity userAnn = UserMappers.mapToAppUserEntity("Ann", "ann", userRole);
        RoleRequestEntity request = RoleRequestMappers.mapToRoleRequestEntity(userAnn, requestedRole, IN_PROGRESS);

        when(roleRequestRepository.findById(id)).thenReturn(Optional.of(request));
        when(userRepository.findAppUserByUsername(userAnn.getUsername())).thenReturn(Optional.of(userAnn));

        //when
        RoleRequestResponse actual = underTest.reviewRequest(id, RoleRequestMappers.mapToReviewRequestDTO(DECLINED));

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
        AppRoleEntity userRole = RoleMappers.mapToAppRoleEntity("user", Set.of(MONITOR_ROLE_REQUEST));
        AppRoleEntity requestedRole = RoleMappers.mapToAppRoleEntity("author", Set.of(GET_USER));
        AppUserEntity userAnn = UserMappers.mapToAppUserEntity("Ann", "ann", userRole);
        RoleRequestEntity request = RoleRequestMappers.mapToRoleRequestEntity(userAnn, requestedRole, IN_PROGRESS);

        when(roleRequestRepository.findById(id)).thenReturn(Optional.of(request));
        when(userRepository.findAppUserByUsername(userAnn.getUsername())).thenReturn(Optional.of(userAnn));

        //when
        RoleRequestResponse actual = underTest.reviewRequest(id, new ReviewRequestDTO(ACCEPTED, "nice"));

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
        AppRoleEntity userRole = RoleMappers.mapToAppRoleEntity("user", Collections.emptySet());
        AppUserEntity userAnn = UserMappers.mapToAppUserEntity("Ann", "ann", userRole);
        userAnn.setId(2L);
        AppUserEntity userJon = UserMappers.mapToAppUserEntity("Jon", "snow", userRole);
        userJon.setId(3L);
        RoleRequestEntity roleRequestEntity = RoleRequestMappers.mapToRoleRequestEntity(userJon, userRole, IN_PROGRESS);

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
        AppRoleEntity userRole = RoleMappers.mapToAppRoleEntity("user", Collections.emptySet());
        AppUserEntity userAnn = UserMappers.mapToAppUserEntity("Ann", "ann", userRole);
        userAnn.setId(2L);
        RoleRequestEntity roleRequestEntity = RoleRequestMappers.mapToRoleRequestEntity(userAnn, userRole, IN_PROGRESS);

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
        AppRoleEntity userRole = RoleMappers.mapToAppRoleEntity("user", Set.of(MONITOR_ROLE_REQUEST));
        AppUserEntity userAnn = UserMappers.mapToAppUserEntity("Ann", "ann", userRole);
        userAnn.setId(2L);
        RoleRequestEntity roleRequestEntity = RoleRequestMappers.mapToRoleRequestEntity(userAnn, userRole, IN_PROGRESS);

        when(roleRequestRepository.findById(id)).thenReturn(Optional.of(roleRequestEntity));
        when(userRepository.findAppUserByUsername(userAnn.getUsername())).thenReturn(Optional.of(userAnn));

        //when
        underTest.deleteRequest(id);

        //then
        verify(roleRequestRepository).findById(id);
        verify(roleRequestRepository).deleteById(id);
    }
}