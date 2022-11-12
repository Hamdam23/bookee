package hamdam.bookee.APIs.aws.controller;

import hamdam.bookee.APIs.aws.payload.FileUpload;
import hamdam.bookee.APIs.aws.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.List;

@RestController
@RequestMapping(value = "/s3")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping("/bucket/files")
    public List<FileUpload> getBucketFiles() {
        return fileUploadService.getBucketFiles();
    }

    @PostMapping("/upload")
    public String fileUpload(MultipartFile file) {
        return fileUploadService.fileUpload(file);
    }

    @GetMapping(value = "/download/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> downloadFile(@PathVariable String filename) {
        ByteArrayOutputStream downloadInputStream = fileUploadService.downloadFile(filename);

        return ResponseEntity.ok()
                .body(downloadInputStream.toByteArray());
    }

    @DeleteMapping("/{fileName}")
    public String deleteFile(@PathVariable String fileName) {
        return fileUploadService.deleteFile(fileName);
    }
}