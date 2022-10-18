package hamdam.bookee.APIs.image.file;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import hamdam.bookee.tools.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileSystemRepositoryTest {

    private final FileSystemRepository underTest = new FileSystemRepository();
    private final FileSystem fileSystem = Jimfs.newFileSystem(Configuration.unix());

    @Test
    void writeFilePath_returnValidDataWhenRequestsValid() throws IOException {
        //given
        String imageName = "godzilla.png";
        byte[] content = "godzilla".getBytes();
        Path path = fileSystem.getPath("" + imageName);

        //when
        String actual = underTest.writeFilePath(content, path);

        //then
        assertThat(actual).isEqualTo(path.toAbsolutePath().toString());
    }

    @Test
    void readFileFromPath_returnValidResponseWhenPathIsValid() {
        //given
        String imageName = "godzilla.png";
        Path path = fileSystem.getPath("" + imageName);

        //when
        FileSystemResource actual = underTest.readFileFromPath(path);

        //then
        assertThat(actual.getFilename()).isEqualTo(imageName);
    }

    @Test
    @Disabled
    void readFileFromPath_throwsExceptionWhenPathIsInValid() {
        //given
        Path invalidPath = Paths.get("C/pics/godzilla.png");
        Path path = fileSystem.getPath("");

        //when
        //then
        assertThatThrownBy(() -> underTest.readFileFromPath(path))
                .isInstanceOf(ResourceNotFoundException.class);
    }

}