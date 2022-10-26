package hamdam.bookee.APIs.role_request;

import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.APIs.role.Permissions;
import hamdam.bookee.APIs.role_request.helpers.ReviewRequestDTO;
import hamdam.bookee.APIs.role_request.helpers.RoleRequestDTO;
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
class RequestServiceImplTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private AppRoleRepository roleRepository;

    @Mock
    private AppUserRepository userRepository;

    @InjectMocks
    private RequestServiceImpl underTest;

    @Test
    void roleRequestBelongsUser_shouldReturnFalseWhenRequestIsNotByUser() {
        //given
        AppUserEntity userLi = new AppUserEntity("Li", "li", "pass");
        userLi.setId(1L);
        AppUserEntity userKun = new AppUserEntity("Kun", "kun", "pass");
        userKun.setId(2L);
        AppRoleEntity requestedRole = new AppRoleEntity("author");
        RequestEntity request = new RequestEntity(userKun, requestedRole);

        //when
        boolean actual = underTest.roleRequestBelongsUser(userLi, request);

        //then
        assertThat(actual).isFalse();
    }

    @Test
    void roleRequestBelongsUser_shouldReturnTrueWhenRequestIsByUser() {
        //given
        AppUserEntity userLi = new AppUserEntity("Li", "li", "pass");
        userLi.setId(1L);
        AppRoleEntity requestedRole = new AppRoleEntity("author");
        RequestEntity request = new RequestEntity(userLi, requestedRole);

        //when
        boolean actual = underTest.roleRequestBelongsUser(userLi, request);

        //then
        assertThat(actual).isTrue();
    }

    @Test
    void getUserPermissions_shouldReturnEmptySetWhenUserDoesNotHavePermissions() {
        //given
        AppRoleEntity role = new AppRoleEntity("author", Collections.emptySet());
        AppUserEntity userLi = new AppUserEntity("Li", "li", role);

        //when
        Set<Permissions> actual = underTest.getUserPermissions(userLi);

        //then
        assertThat(actual).isEmpty();
    }

    @Test
    void getUserPermissions_shouldReturnPermissionsWhenRequestIsValid() {
        //given
        AppRoleEntity role = new AppRoleEntity("author", Set.of(MONITOR_USER, GET_USER));
        AppUserEntity userLi = new AppUserEntity("Li", "li", role);

        //when
        Set<Permissions> actual = underTest.getUserPermissions(userLi);

        //then
        assertThat(actual).contains(MONITOR_USER, GET_USER);
    }

    @Test
    @WithMockUser("li")
    void postRoleRequest_throwsExceptionWhenUserNotAllowedToRequestRole() {
        //given
        AppUserEntity currUser = new AppUserEntity("Li", "li", "pass");
        when(userRepository.findAppUserByUserName(currUser.getUserName())).thenReturn(Optional.of(currUser));
        when(requestRepository.existsByUserAndState(currUser, IN_PROGRESS)).thenReturn(true);

        //when
        //then
        assertThatThrownBy(() -> underTest.postRoleRequest(new RoleRequestDTO()))
                .isInstanceOf(AlreadyHasInProgressRequestException.class);
        verify(userRepository).findAppUserByUserName(currUser.getUserName());
    }

    @Test
    @WithMockUser("li")
    void postRoleRequest_throwsExceptionWhenRequestedRoleIdIsInvalid() {
        //given
        AppRoleEntity role = new AppRoleEntity("author");
        role.setId(1L);

        RoleRequestDTO request = new RoleRequestDTO(role.getId());

        AppUserEntity currUser = new AppUserEntity("Li", "li", "pass");
        when(userRepository.findAppUserByUserName(currUser.getUserName())).thenReturn(Optional.of(currUser));
        when(requestRepository.existsByUserAndState(currUser, IN_PROGRESS)).thenReturn(false);
        when(roleRepository.findById(role.getId())).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.postRoleRequest(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(role.getId().toString());
        verify(userRepository).findAppUserByUserName(currUser.getUserName());
        verify(roleRepository).findById(role.getId());
    }

    @Test
    @WithMockUser("li")
    void postRoleRequest_throwsExceptionWhenUserDoesNotHavePermissionToRequest() {
        //given
        AppRoleEntity requestedRole = new AppRoleEntity("author", Collections.emptySet());
        requestedRole.setId(1L);

        AppRoleEntity userRole = new AppRoleEntity("user", Collections.emptySet());
        userRole.setId(2L);

        RoleRequestDTO request = new RoleRequestDTO(requestedRole.getId());

        AppUserEntity currUser = new AppUserEntity("Li", "li", userRole);
        when(userRepository.findAppUserByUserName(currUser.getUserName())).thenReturn(Optional.of(currUser));
        when(requestRepository.existsByUserAndState(currUser, IN_PROGRESS)).thenReturn(false);
        when(roleRepository.findById(requestedRole.getId())).thenReturn(Optional.of(requestedRole));

        //when
        //then
        assertThatThrownBy(() -> underTest.postRoleRequest(request))
                .isInstanceOf(LimitedPermissionException.class);
        verify(userRepository).findAppUserByUserName(currUser.getUserName());
    }

    @Test
    @WithMockUser("li")
    void postRoleRequest_throwsExceptionWhenUserHasAlreadyAcceptedRoleRequest() {
        //given
        AppRoleEntity requestedRole = new AppRoleEntity("author", Set.of(MONITOR_ROLE_REQUEST));
        requestedRole.setId(1L);

        AppRoleEntity userRole = new AppRoleEntity("user", Set.of(CREATE_ROLE_REQUEST));
        userRole.setId(2L);

        RoleRequestDTO request = new RoleRequestDTO(requestedRole.getId());

        AppUserEntity currUser = new AppUserEntity("Li", "li", userRole);
        when(userRepository.findAppUserByUserName(currUser.getUserName())).thenReturn(Optional.of(currUser));
        when(requestRepository.existsByUserAndState(currUser, IN_PROGRESS)).thenReturn(false);
        when(roleRepository.findById(requestedRole.getId())).thenReturn(Optional.of(requestedRole));

        //when
        //then
        assertThatThrownBy(() -> underTest.postRoleRequest(request))
                .isInstanceOf(NotAllowedRoleOnRequestException.class);
        verify(userRepository).findAppUserByUserName(currUser.getUserName());
    }

    @Test
    @WithMockUser("li")
    void postRoleRequest_returnValidDataWhenRequestIsValid() {
        //given
        AppRoleEntity requestedRole = new AppRoleEntity("author", Collections.emptySet());
        requestedRole.setId(1L);
        AppRoleEntity userRole = new AppRoleEntity("user", Set.of(CREATE_ROLE_REQUEST));
        userRole.setId(2L);

        AppUserEntity currUser = new AppUserEntity("Li", "li", userRole);
        RequestEntity requestEntity = new RequestEntity(currUser, requestedRole, State.IN_PROGRESS);
        RoleRequestDTO requestDTO = new RoleRequestDTO(requestedRole.getId());

        when(userRepository.findAppUserByUserName(currUser.getUserName())).thenReturn(Optional.of(currUser));
        when(requestRepository.existsByUserAndState(currUser, IN_PROGRESS)).thenReturn(false);
        when(roleRepository.findById(requestedRole.getId())).thenReturn(Optional.of(requestedRole));
        when(requestRepository.save(any())).thenReturn(requestEntity);

        //when
        RoleRequestResponse actual = underTest.postRoleRequest(requestDTO);

        //then
        verify(userRepository).findAppUserByUserName(currUser.getUserName());
        verify(requestRepository).existsByUserAndState(currUser, IN_PROGRESS);
        verify(roleRepository).findById(requestedRole.getId());

        ArgumentCaptor<RequestEntity> requestArgCaptor = ArgumentCaptor.forClass(RequestEntity.class);
        verify(requestRepository).save(requestArgCaptor.capture());
        RequestEntity actualRequest = requestArgCaptor.getValue();
        verify(requestRepository).save(any());

        assertThat(actual.getUser().getUserName()).isEqualTo(actualRequest.getUser().getUserName());
        assertThat(actual.getRequestedRole()).isEqualTo(actualRequest.getRequestedRole().getRoleName());
        assertThat(actual.getState()).isEqualTo(actualRequest.getState());
    }

    @Test
    @WithMockUser("li")
    void getAllRoleRequests_returnValidDataWhenUserWithoutStateAndWithoutPermission() {
        //given
        AppRoleEntity userRole = new AppRoleEntity("user", Set.of(GET_USER));
        AppUserEntity currUser = new AppUserEntity("Li", "li", userRole);

        AppRoleEntity authorRole = new AppRoleEntity("author", Collections.emptySet());
        AppRoleEntity policeRole = new AppRoleEntity("author", Collections.emptySet());

        List<RequestEntity> requestEntities = new ArrayList<>();
        requestEntities.add(new RequestEntity(currUser, authorRole, State.IN_PROGRESS));
        requestEntities.add(new RequestEntity(currUser, policeRole, DECLINED));

        when(userRepository.findAppUserByUserName(currUser.getUserName())).thenReturn(Optional.of(currUser));
        when(requestRepository.findAll()).thenReturn(requestEntities);
        when(requestRepository.findAllByUser(currUser)).thenReturn(requestEntities);

        //when
        List<RoleRequestResponse> actual = underTest.getAllRoleRequests(null);

        //then
        verify(userRepository).findAppUserByUserName(currUser.getUserName());
        verify(requestRepository).findAll();
        verify(requestRepository).findAllByUser(currUser);
        assertThat(actual.size()).isEqualTo(requestEntities.size());
    }

    @Test
    @WithMockUser("li")
    void getAllRoleRequests_returnValidDataWhenUserWithStateAndWithoutPermission() {
        //given
        AppRoleEntity userRole = new AppRoleEntity("user", Set.of(GET_USER));
        AppUserEntity currUser = new AppUserEntity("Li", "li", userRole);
        AppUserEntity userAnn = new AppUserEntity("Ann", "ann", userRole);
        AppUserEntity userJack = new AppUserEntity("Jack", "jackie", userRole);

        AppRoleEntity authorRole = new AppRoleEntity("author", Collections.emptySet());
        AppRoleEntity policeRole = new AppRoleEntity("author", Collections.emptySet());

        List<RequestEntity> requestEntities = new ArrayList<>();
        requestEntities.add(new RequestEntity(currUser, authorRole, IN_PROGRESS));
        requestEntities.add(new RequestEntity(userAnn, authorRole, IN_PROGRESS));
        requestEntities.add(new RequestEntity(userJack, authorRole, IN_PROGRESS));
        requestEntities.add(new RequestEntity(currUser, policeRole, DECLINED));

        when(userRepository.findAppUserByUserName(currUser.getUserName())).thenReturn(Optional.of(currUser));
        when(requestRepository.findAllByState(IN_PROGRESS)).thenReturn(List.of(requestEntities.get(0),
                requestEntities.get(1), requestEntities.get(2)));
        when(requestRepository.findAllByUser(currUser)).thenReturn(List.of(requestEntities.get(0)));

        //when
        List<RoleRequestResponse> actual = underTest.getAllRoleRequests(IN_PROGRESS);

        //then
        verify(userRepository).findAppUserByUserName(currUser.getUserName());
        verify(requestRepository).findAllByState(IN_PROGRESS);
        verify(requestRepository).findAllByUser(currUser);
        assertThat(actual.size()).isEqualTo(1);
    }

    @Test
    @WithMockUser("li")
    void getAllRoleRequests_returnValidDataWhenUserWithoutStateAndWithPermission() {
        //given
        AppRoleEntity userRole = new AppRoleEntity("user", Set.of(GET_USER));
        AppRoleEntity authorRole = new AppRoleEntity("author", Collections.emptySet());
        AppRoleEntity policeRole = new AppRoleEntity("author", Collections.emptySet());

        AppUserEntity userAnn = new AppUserEntity("Ann", "ann", userRole);
        AppUserEntity userJack = new AppUserEntity("Jack", "jackie", userRole);
        AppUserEntity currUser = new AppUserEntity(
                "Li", "li", new AppRoleEntity("user", Set.of(MONITOR_ROLE_REQUEST)));

        List<RequestEntity> requestEntities = new ArrayList<>();
        requestEntities.add(new RequestEntity(currUser, authorRole, ACCEPTED));
        requestEntities.add(new RequestEntity(userAnn, authorRole, IN_PROGRESS));
        requestEntities.add(new RequestEntity(userJack, authorRole, IN_PROGRESS));
        requestEntities.add(new RequestEntity(userAnn, policeRole, DECLINED));

        when(userRepository.findAppUserByUserName(currUser.getUserName())).thenReturn(Optional.of(currUser));
        when(requestRepository.findAll()).thenReturn(requestEntities);

        //when
        List<RoleRequestResponse> actual = underTest.getAllRoleRequests(null);

        //then
        verify(userRepository).findAppUserByUserName(currUser.getUserName());
        verify(requestRepository).findAll();
        assertThat(actual.size()).isEqualTo(requestEntities.size());
    }

    @Test
    @WithMockUser("li")
    void getAllRoleRequests_returnValidDataWhenUserWithStateAndWithPermission() {
        //given
        AppRoleEntity userRole = new AppRoleEntity("user", Set.of(GET_USER));
        AppRoleEntity authorRole = new AppRoleEntity("author", Collections.emptySet());
        AppRoleEntity policeRole = new AppRoleEntity("author", Collections.emptySet());

        AppUserEntity userAnn = new AppUserEntity("Ann", "ann", userRole);
        AppUserEntity userJack = new AppUserEntity("Jack", "jackie", userRole);
        AppUserEntity currUser = new AppUserEntity(
                "Li", "li", new AppRoleEntity("user", Set.of(MONITOR_ROLE_REQUEST)));

        List<RequestEntity> requestEntities = new ArrayList<>();
        requestEntities.add(new RequestEntity(currUser, authorRole, ACCEPTED));
        requestEntities.add(new RequestEntity(userAnn, authorRole, IN_PROGRESS));
        requestEntities.add(new RequestEntity(userJack, authorRole, IN_PROGRESS));
        requestEntities.add(new RequestEntity(userAnn, policeRole, DECLINED));

        when(userRepository.findAppUserByUserName(currUser.getUserName())).thenReturn(Optional.of(currUser));
        when(requestRepository.findAllByState(IN_PROGRESS)).thenReturn(List.of(requestEntities.get(1), requestEntities.get(2)));

        //when
        List<RoleRequestResponse> actual = underTest.getAllRoleRequests(IN_PROGRESS);

        //then
        verify(userRepository).findAppUserByUserName(currUser.getUserName());
        verify(requestRepository).findAllByState(IN_PROGRESS);
        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void reviewRequest_throwsExceptionWhenRequestIdIsInvalid() {
        //given
        Long id = 1L;
        when(requestRepository.findById(id)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.reviewRequest(id, new ReviewRequestDTO()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(id.toString());
        verify(requestRepository).findById(id);
    }

    @Test
    @WithMockUser("ann")
    void reviewRequest_throwsExceptionWhenUserHasLimitedPermission() {
        //given
        Long id = 1L;
        AppRoleEntity userRole = new AppRoleEntity("user", Collections.emptySet());
        AppUserEntity userAnn = new AppUserEntity("Ann", "ann", userRole);

        when(requestRepository.findById(id)).thenReturn(Optional.of(new RequestEntity()));
        when(userRepository.findAppUserByUserName(userAnn.getUserName())).thenReturn(Optional.of(userAnn));

        //when
        //then
        assertThatThrownBy(() -> underTest.reviewRequest(id, new ReviewRequestDTO()))
                .isInstanceOf(LimitedPermissionException.class);
        verify(requestRepository).findById(id);
        verify(userRepository).findAppUserByUserName(userAnn.getUserName());
    }

    @Test
    @WithMockUser("ann")
    void reviewRequest_throwsExceptionWhenReviewStateIsInvalid() {
        //given
        Long id = 1L;
        AppRoleEntity userRole = new AppRoleEntity("user", Set.of(MONITOR_ROLE_REQUEST));
        AppUserEntity userAnn = new AppUserEntity("Ann", "ann", userRole);

        when(requestRepository.findById(id)).thenReturn(Optional.of(new RequestEntity()));
        when(userRepository.findAppUserByUserName(userAnn.getUserName())).thenReturn(Optional.of(userAnn));

        //when
        //then
        assertThatThrownBy(() -> underTest.reviewRequest(id, new ReviewRequestDTO(IN_PROGRESS)))
                .isInstanceOf(IncorrectStateValueException.class);
        verify(requestRepository).findById(id);
        verify(userRepository).findAppUserByUserName(userAnn.getUserName());
    }

    @Test
    @WithMockUser("ann")
    void reviewRequest_returnValidDataWhenStateIsDeclinedWithoutDescription() {
        //given
        Long id = 1L;
        AppRoleEntity userRole = new AppRoleEntity("user", Set.of(MONITOR_ROLE_REQUEST));
        AppRoleEntity requestedRole = new AppRoleEntity("author", Set.of(GET_USER));
        AppUserEntity userAnn = new AppUserEntity("Ann", "ann", userRole);
        RequestEntity request = new RequestEntity(userAnn, requestedRole, IN_PROGRESS);

        when(requestRepository.findById(id)).thenReturn(Optional.of(request));
        when(userRepository.findAppUserByUserName(userAnn.getUserName())).thenReturn(Optional.of(userAnn));

        //when
        RoleRequestResponse actual = underTest.reviewRequest(id, new ReviewRequestDTO(DECLINED));

        //then
        verify(requestRepository).findById(id);
        verify(userRepository).findAppUserByUserName(userAnn.getUserName());
        verify(requestRepository).save(request);
        assertThat(actual.getUser().getUserName()).isEqualTo(userAnn.getUserName());
        assertThat(actual.getRequestedRole()).isEqualTo(requestedRole.getRoleName());
        assertThat(actual.getState()).isEqualTo(DECLINED);
    }

    @Test
    @WithMockUser("ann")
    void reviewRequest_returnValidDataWhenStateIsAcceptedWithDescription() {
        //given
        Long id = 1L;
        AppRoleEntity userRole = new AppRoleEntity("user", Set.of(MONITOR_ROLE_REQUEST));
        AppRoleEntity requestedRole = new AppRoleEntity("author", Set.of(GET_USER));
        AppUserEntity userAnn = new AppUserEntity("Ann", "ann", userRole);
        RequestEntity request = new RequestEntity(userAnn, requestedRole, IN_PROGRESS);

        when(requestRepository.findById(id)).thenReturn(Optional.of(request));
        when(userRepository.findAppUserByUserName(userAnn.getUserName())).thenReturn(Optional.of(userAnn));

        //when
        RoleRequestResponse actual = underTest.reviewRequest(id, new ReviewRequestDTO(ACCEPTED, "nice"));

        //then
        verify(requestRepository).findById(id);
        verify(userRepository).findAppUserByUserName(userAnn.getUserName());
        verify(requestRepository).save(request);
        assertThat(actual.getUser().getUserName()).isEqualTo(userAnn.getUserName());
        assertThat(actual.getRequestedRole()).isEqualTo(requestedRole.getRoleName());
        assertThat(actual.getState()).isEqualTo(ACCEPTED);
        assertThat(actual.getDescription()).isEqualTo("nice");
    }

    @Test
    void deleteRequest_throwsExceptionWhenRequestIdIsInvalid() {
        Long id = 1L;
        when(requestRepository.findById(id)).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.deleteRequest(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(id.toString());
        verify(requestRepository).findById(id);
    }

    @Test
    @WithMockUser("ann")
    void deleteRequest_throwsExceptionWhenUserDoesNotHaveAccess() {
        Long id = 1L;
        AppRoleEntity userRole = new AppRoleEntity("user", Collections.emptySet());
        AppUserEntity userAnn = new AppUserEntity("Ann", "ann", userRole);
        userAnn.setId(2L);
        AppUserEntity userJon = new AppUserEntity("Jon", "snow", userRole);
        userJon.setId(3L);
        RequestEntity requestEntity = new RequestEntity(userJon, userRole, IN_PROGRESS);

        when(requestRepository.findById(id)).thenReturn(Optional.of(requestEntity));
        when(userRepository.findAppUserByUserName(userAnn.getUserName())).thenReturn(Optional.of(userAnn));

        //when
        //then
        assertThatThrownBy(() -> underTest.deleteRequest(id))
                .isInstanceOf(NotAccessibleRequestException.class);
        verify(requestRepository).findById(id);
    }

    @Test
    @WithMockUser("ann")
    void deleteRequest_deletesDataWhenUserOwnsRequest() {
        Long id = 1L;
        AppRoleEntity userRole = new AppRoleEntity("user", Collections.emptySet());
        AppUserEntity userAnn = new AppUserEntity("Ann", "ann", userRole);
        userAnn.setId(2L);
        RequestEntity requestEntity = new RequestEntity(userAnn, userRole, IN_PROGRESS);

        when(requestRepository.findById(id)).thenReturn(Optional.of(requestEntity));
        when(userRepository.findAppUserByUserName(userAnn.getUserName())).thenReturn(Optional.of(userAnn));

        //when
        underTest.deleteRequest(id);

        //then
        verify(requestRepository).findById(id);
        verify(requestRepository).deleteById(id);
    }

    @Test
    @WithMockUser("ann")
    void deleteRequest_deletesDataWhenUserHasMonitoringPermission() {
        Long id = 1L;
        AppRoleEntity userRole = new AppRoleEntity("user", Set.of(MONITOR_ROLE_REQUEST));
        AppUserEntity userAnn = new AppUserEntity("Ann", "ann", userRole);
        userAnn.setId(2L);
        RequestEntity requestEntity = new RequestEntity(userAnn, userRole, IN_PROGRESS);

        when(requestRepository.findById(id)).thenReturn(Optional.of(requestEntity));
        when(userRepository.findAppUserByUserName(userAnn.getUserName())).thenReturn(Optional.of(userAnn));

        //when
        underTest.deleteRequest(id);

        //then
        verify(requestRepository).findById(id);
        verify(requestRepository).deleteById(id);
    }
}