package hamdam.bookee.APIs.image;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import hamdam.bookee.APIs.image.helpers.ImageDTO;
import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.AppRoleRepository;
import hamdam.bookee.APIs.user.AppUserEntity;
import hamdam.bookee.APIs.user.AppUserRepository;
import hamdam.bookee.APIs.user.helpers.UserMappers;
import hamdam.bookee.tools.utils.TokenProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Set;

import static hamdam.bookee.APIs.role.Permissions.CREATE_GENRE;
import static hamdam.bookee.tools.constants.Endpoints.API_IMAGE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest
@AutoConfigureMockMvc
class ImageControllerTest {

    @Container
    static LocalStackContainer localStack = new LocalStackContainer(
            DockerImageName.parse("localstack/localstack:1.2.0")
    ).withServices(LocalStackContainer.Service.S3);

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private AppRoleRepository roleRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void setup() {
        imageRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void uploadImage_shouldUploadImage() throws Exception {
        //given
        AppRoleEntity role = roleRepository.save(AppRoleEntity.builder().roleName("name").permissions(Set.of(CREATE_GENRE)).build());
        AppUserEntity user = userRepository.save(UserMappers.mapToAppUserEntity("nikola", "niko", "pass", role));

        String name = "file";
        MockMultipartFile file = new MockMultipartFile(name, "file.png", "image/png", name.getBytes());

        //when
        ResultActions perform = mockMvc.perform(
                multipart(API_IMAGE)
                        .file(file)
                        .header("Content-Type", "multipart/form-data")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        perform.andExpect(status().isOk());
        assertThat(imageRepository.findAll().stream().anyMatch(
                image -> image.getImageName().contains(file.getName()))
        ).isTrue();
    }

    @Test
    void getImageByID_shouldGetImageById() throws Exception {
        //given
        AppRoleEntity role = roleRepository.save(AppRoleEntity.builder().roleName("name").permissions(Set.of(CREATE_GENRE)).build());
        AppUserEntity user = userRepository.save(UserMappers.mapToAppUserEntity("nikola", "niko", "pass", role));

        ImageEntity image = imageRepository.save(ImageEntity.builder().imageName("name").url("url").build());

        //when
        ResultActions perform = mockMvc.perform(
                get(API_IMAGE + "/" + image.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        perform.andExpect(status().isOk());
        ImageDTO response = objectMapper.readValue(perform.andReturn().getResponse().getContentAsString(), ImageDTO.class);
        assertThat(response.getId()).isEqualTo(image.getId());
        assertThat(response.getImageName()).isEqualTo(image.getImageName());
    }

    @Test
    void deleteImage_shouldDeleteImageById() throws Exception {
        //given
        AppRoleEntity role = roleRepository.save(AppRoleEntity.builder().roleName("name").permissions(Set.of(CREATE_GENRE)).build());
        AppUserEntity user = userRepository.save(UserMappers.mapToAppUserEntity("nikola", "niko", "pass", role));

        ImageEntity image = imageRepository.save(ImageEntity.builder().imageName("name").url("url").build());

        //when
        ResultActions perform = mockMvc.perform(
                delete(API_IMAGE + "/" + image.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createToken(user.getUsername(), user.getRole(), true))
        );

        //then
        perform.andExpect(status().isOk());
        assertThat(imageRepository.existsById(image.getId())).isFalse();
    }

    @TestConfiguration
    static class AmazonS3Configuration {

        @Bean
        @Primary
        public AmazonS3Client amazonS3Client(
                @Value("${aws.s3.bucket}") String bucketName,
                @Value("${cloud.aws.region.static}") String region
        ) {
            AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                            localStack.getEndpointOverride(LocalStackContainer.Service.S3).toString(),
                            region
                    ))
                    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(
                            localStack.getAccessKey(),
                            localStack.getSecretKey()
                    )))
                    .build();
            s3.createBucket(bucketName);
            return (AmazonS3Client) s3;
        }
    }

}