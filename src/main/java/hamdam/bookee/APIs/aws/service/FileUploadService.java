package hamdam.bookee.APIs.aws.service;

import hamdam.bookee.APIs.aws.payload.FileUpload;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface FileUploadService {

	String fileUpload(MultipartFile file);

	List<FileUpload> getBucketFiles();

	String deleteFile(String fileName);

	ByteArrayOutputStream downloadFile(String keyName);
}
