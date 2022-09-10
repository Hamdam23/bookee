package hamdam.bookee.APIs.image;

import hamdam.bookee.tools.annotations.MyValidFile;
import hamdam.bookee.tools.exeptions.ResponseSettings;
import hamdam.bookee.tools.paging.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

    // TODO: 9/2/22 why additional "/upload" ?
    @PostMapping
    public ImagEntity uploadImage(@MyValidFile @RequestParam MultipartFile file) throws Exception {
        return imageService.uploadImage(file);
    }

    @GetMapping(value = "/download/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public Resource downloadImage(@PathVariable String name) {
        return imageService.downloadImage(name);
    }

    @GetMapping("{id}")
    public ImageDTO getImageByID(@PathVariable long id) {
        return imageService.getImageByID(id);
    }

    @GetMapping
    public PagedResponse<ImageDTO> getAllImages(Pageable pageable) {
        return new PagedResponse<>(imageService.getAllImages(pageable));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ResponseSettings> deleteImage(@PathVariable long id) {
        // TODO: 9/2/22 return full json response
        return new ResponseEntity<>(imageService.deleteImageById(id), HttpStatus.OK);
    }
}
