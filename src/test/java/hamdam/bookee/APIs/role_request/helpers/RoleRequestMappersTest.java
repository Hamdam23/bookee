package hamdam.bookee.APIs.role_request.helpers;

import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role_request.RoleRequestEntity;
import hamdam.bookee.APIs.user.AppUserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static hamdam.bookee.APIs.role.Permissions.GET_USER;
import static hamdam.bookee.APIs.role.Permissions.MONITOR_USER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RoleRequestMappersTest {

    @Test
    void mapToRoleRequestResponse_shouldReturnNullWhenArgumentRequestEntityIsNull() {
        //given
        //when
        RoleRequestResponseDTO actual = RoleRequestMappers.mapToRoleRequestResponse(null, "roleName");

        //then
        assertThat(actual).isNull();
    }

    @Test
    void mapToRoleRequestResponse_shouldReturnValidResponseWhenRequestIsValid() {
        //given
        AppRoleEntity role = AppRoleEntity
                .builder()
                .roleName("USER")
                .permissions(Set.of(GET_USER)).
                build();

        AppRoleEntity requestedRole = AppRoleEntity
                .builder()
                .roleName("AUTHOR")
                .permissions(Set.of(MONITOR_USER)).
                build();

        AppUserEntity user = AppUserEntity
                .builder()
                .name("nikola")
                .username("niko")
                .password("pass")
                .role(role)
                .build();

        String requestedRoleName = requestedRole.getRoleName();
        RoleRequestEntity roleRequest = RoleRequestEntity
                .builder()
                .user(user)
                .requestedRole(requestedRole)
                .build();
        //when
        RoleRequestResponseDTO actual = RoleRequestMappers.mapToRoleRequestResponse(roleRequest, requestedRoleName);

        //then
        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("user", "requestedRole")
                .isEqualTo(roleRequest);
        Assertions.assertThat(actual.getUser())
                .usingRecursiveComparison()
                .ignoringFields("image")
                .isEqualTo(roleRequest.getUser());
        assertThat(actual.getRequestedRole()).isEqualTo(roleRequest.getRequestedRole().getRoleName());
    }
}