package hamdam.bookee.APIs.image;

import hamdam.bookee.APIs.image.helpers.ImageResponseDTO;
import hamdam.bookee.tools.annotations.ValidFile;
import hamdam.bookee.tools.exceptions.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

import static hamdam.bookee.tools.annotations.ValidFile.ExtensionGroup.IMAGES;
import static hamdam.bookee.tools.constants.DeletionMessage.getDeletionMessage;
import static hamdam.bookee.tools.constants.Endpoints.API_IMAGE;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_IMAGE)
@Validated
public class ImageController {
    private final ImageService imageService;

    /**
     * It takes a file, validates it, and then uploads it to the server
     *
     * @param file The file to be uploaded.
     * @return ImageEntity
     */
    @PostMapping
    public ImageEntity uploadImage(@ValidFile(
            extensionGroups = IMAGES
    ) @RequestParam MultipartFile file) throws Exception {
        return imageService.uploadImage(file);
    }

    /**
     * This function takes in a Long id, and returns an ImageDTO
     *
     * @param id The id of the image you want to get.
     * @return ImageDTO
     */
    @GetMapping("{id}")
    public ImageResponseDTO getImageByID(@PathVariable Long id) {
        return imageService.getImageByID(id);
    }

    /**
     * It deletes an image by id and returns a response entity with a status of OK
     *
     * @param id The id of the image to be deleted.
     * @return A ResponseEntity object is being returned.
     */
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long id) {
        imageService.deleteImageById(id);
        return new ResponseEntity<>(
                new ApiResponse(
                        HttpStatus.OK,
                        LocalDateTime.now(),
                        getDeletionMessage("Image", id)
                ), HttpStatus.OK
        );
    }
}
