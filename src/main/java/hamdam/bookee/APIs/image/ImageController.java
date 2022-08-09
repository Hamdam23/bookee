package hamdam.bookee.APIs.image;

import hamdam.bookee.tools.annotations.MyValidFile;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static hamdam.bookee.tools.constants.Endpoints.API_IMAGE;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_IMAGE)
@Validated
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/upload")
    public Image uploadImage(@MyValidFile @RequestParam MultipartFile file) throws Exception {
        return imageService.uploadImage(file);
    }

    @GetMapping(value = "/download/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public Resource downloadImage(@PathVariable String name) {
        return imageService.downloadImage(name);
    }

    @GetMapping("{id}")
    public Image getImageByID(@PathVariable long id) {
        return imageService.getImageByID(id);
    }

    @GetMapping
    public List<ImageDTO> getAllImages() {
        return imageService.getAllImages();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteImage(@PathVariable long id) {
        imageService.deleteImageById(id);
        return ResponseEntity.ok().body("Image with id: " + id + " successfully deleted!");
    }
}
