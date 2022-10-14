package hamdam.bookee.APIs.image.file;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class FileSystemRepositoryTest {

    private final FileSystemRepository underTest = new FileSystemRepository();
    private final FileSystem fileSystem = Jimfs.newFileSystem(Configuration.unix());

//    private static String path;

//    @BeforeAll
//    static void buildUp() {
//        FileSystem fileSystem = Jimfs.newFileSystem(Configuration.unix());
//        path = fileSystem.getPath("").toString();
//        ReflectionTestUtils.setField(underTest, "path", path);
//    }

    @Test
    void writeFileToPath_returnValidDataWhenRequestsValid() throws IOException {
        //given
        String imageName = "godzilla.png";
        byte[] content = "godzilla".getBytes();
        Path path = fileSystem.getPath("" + imageName);

        //when
        String actual = underTest.writeFileToPath(content, path);

        //then
        assertThat(actual).isEqualTo(path.toAbsolutePath().toString());
    }

}