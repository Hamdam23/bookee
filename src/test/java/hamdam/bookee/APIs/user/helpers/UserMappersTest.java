package hamdam.bookee.APIs.user.helpers;

import hamdam.bookee.APIs.auth.RegistrationRequest;
import hamdam.bookee.APIs.image.ImageEntity;
import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.user.AppUserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static hamdam.bookee.APIs.role.Permissions.GET_USER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserMappersTest {

    @Test
    void mapToAppUserResponseDTO_shouldReturnNullWhenRequestIsNull() {
        //given
        //when
        AppUserResponseDTO actual = UserMappers.mapToAppUserResponseDTO(null);

        //then
        assertThat(actual).isNull();
    }

    @Test
    void mapToAppUserResponseDTO_shouldReturnValidResponseWhenRequestIsValid() {
        //given
        AppRoleEntity role = AppRoleEntity
                .builder()
                .roleName("USER")
                .permissions(Set.of(GET_USER)).
                build();

        ImageEntity image = ImageEntity.builder().imageName("imageName").url("url").build();

        AppUserEntity user = AppUserEntity
                .builder()
                .name("nikola")
                .username("niko")
                .password("pass")
                .role(role)
                .userImage(image)
                .build();
        //when
        AppUserResponseDTO actual = UserMappers.mapToAppUserResponseDTO(user);

        //then
        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("role", "image", "password", "roleRequests", "timeStamp")
                .isEqualTo(user);

        Assertions.assertThat(actual.getRole())
                .usingRecursiveComparison()
                .ignoringFields("timeStamp", "id", "permissions")
                .isEqualTo(role);

        Assertions.assertThat(actual.getImage())
                .usingRecursiveComparison()
                .ignoringFields("timeStamp", "id")
                .isEqualTo(image);
    }

    @Test
    void mapToAppUserEntity_shouldReturnNullWhenRequestIsNull() {
        //given
        //when
        AppUserEntity actual = UserMappers.mapToAppUserEntity(null);

        //then
        assertThat(actual).isNull();
    }

    @Test
    void mapToAppUserEntity_shouldReturnValidResponseWhenRequestIsValid() {
        //given
        RegistrationRequest request = new RegistrationRequest("name", "username", "pass", null);

        //when
        AppUserEntity actual = UserMappers.mapToAppUserEntity(request);

        //then
        assertThat(actual.getName()).isEqualTo(request.getName());
        assertThat(actual.getUsername()).isEqualTo(request.getUsername());
    }
}