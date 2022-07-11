package hamdam.bookee.APIs.image;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static hamdam.bookee.tools.constants.Endpoints.API_IMAGE;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_IMAGE)
public class ImageController {
    private final ImageService imageService;

    @GetMapping("{id}")
    public Image getImageByID(@PathVariable long id){
        return imageService.getImageByID(id);
    }

    @PostMapping("/upload")
    public Image uploadImage(@RequestParam MultipartFile file) throws Exception {
        return imageService.uploadImage(file);
    }

    @GetMapping(value = "/download/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public Resource downloadImage(@PathVariable String name){
        return imageService.downloadImage(name);
    }
}
