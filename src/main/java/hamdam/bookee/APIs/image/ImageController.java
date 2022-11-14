package hamdam.bookee.APIs.image;

import hamdam.bookee.APIs.image.helpers.ImageDTO;
import hamdam.bookee.tools.annotations.ValidFile;
import hamdam.bookee.tools.exceptions.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static hamdam.bookee.tools.constants.Endpoints.API_IMAGE;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_IMAGE)
@Validated
public class ImageController {
    private final ImageService imageService;

    @PostMapping
    public ImageEntity uploadImage(@ValidFile @RequestParam MultipartFile file) throws Exception {
        return imageService.uploadImage(file);
    }

    @GetMapping("{id}")
    public ImageDTO getImageByID(@PathVariable Long id) {
        return imageService.getImageByID(id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long id) {
        return new ResponseEntity<>(imageService.deleteImageById(id), HttpStatus.OK);
    }
}
