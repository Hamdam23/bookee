package hamdam.bookee.APIs.aws.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileUpload {

	private String fileName;
	private Long fileSize;
	private String filePath;
	private byte[] file;

	public FileUpload(String fileName, Long fileSize, String filePath) {
		super();
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.filePath = filePath;
	}
}
