package hamdam.bookee.APIs.role.helpers;

import hamdam.bookee.APIs.role.AppRoleEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RoleMappersTest {

    @Test
    void mapToAppRoleResponseDTO_shouldReturnNullWhenRequestIsNull() {
        //given
        //when
        AppRoleResponseDTO actual = RoleMappers.mapToAppRoleResponseDTO(null);

        //then
        assertThat(actual).isNull();
    }

    @Test
    void mapToAppRoleResponseDTO_shouldReturnValidResponseWhenRequestIsValid() {
        //given
        AppRoleEntity appRole = AppRoleEntity.builder().roleName("roleName").isDefault(false).build();

        //when
        AppRoleResponseDTO actual = RoleMappers.mapToAppRoleResponseDTO(appRole);

        //then
        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("timeStamp", "id", "permissions")
                .isEqualTo(appRole);
    }

    @Test
    void mapToAppRoleEntity_shouldReturnNullWhenRequestIsNull() {
        //given
        //when
        AppRoleEntity actual = RoleMappers.mapToAppRoleEntity(null);

        //then
        assertThat(actual).isNull();
    }

    @Test
    void mapToAppRoleEntity_shouldReturnValidResponseWhenRequestIsValid() {
        //given
        AppRoleRequestDTO appRole = new AppRoleRequestDTO("role", false, Collections.emptySet());

        //when
        AppRoleEntity actual = RoleMappers.mapToAppRoleEntity(appRole);

        //then
        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("timeStamp", "id", "roleRequests")
                .isEqualTo(appRole);
    }
}