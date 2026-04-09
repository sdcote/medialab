package coyote.commons.rtw.transform;

import coyote.commons.dataframe.DataFrame;
import coyote.commons.cfg.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class MediaConverterTest {

    @TempDir
    Path tempDir;

    private File testFile;

    @BeforeEach
    public void setUp() throws IOException {
        testFile = new File(tempDir.toFile(), "test.txt");
        Files.writeString(testFile.toPath(), "Hello World");
    }

    @Test
    public void testMissingFormatConfiguration() {
        MediaConverter converter = new MediaConverter();
        DataFrame frame = new DataFrame().set("filename", testFile.getAbsolutePath());
        
        // Should not fail, but should not convert
        DataFrame result = converter.process(frame);
        
        assertEquals(testFile.getAbsolutePath(), result.getAsString("filename"));
    }

    @Test
    public void testFfmpegNotFound() throws Exception {
        MediaConverter converter = new MediaConverter();
        // Set a non-existent ffmpeg path
        Config config = new Config();
        config.put("ffmpeg", "non-existent-ffmpeg");
        config.put("format", "mp4");
        converter.setConfiguration(config);
        
        DataFrame frame = new DataFrame().set("filename", testFile.getAbsolutePath());
        
        DataFrame result = converter.process(frame);
        
        // Filename should remain unchanged because conversion failed
        assertEquals(testFile.getAbsolutePath(), result.getAsString("filename"));
    }

    @Test
    public void testTargetExistsNoOverwrite() throws Exception {
        MediaConverter converter = new MediaConverter();
        Config config = new Config();
        config.put("format", "mp4");
        config.put("overwrite", false);
        converter.setConfiguration(config);

        // Create the target file beforehand
        File targetFile = new File(tempDir.toFile(), "test.mp4");
        assertTrue(targetFile.createNewFile());

        DataFrame frame = new DataFrame().set("filename", testFile.getAbsolutePath());
        DataFrame result = converter.process(frame);

        // Filename should remain unchanged because target exists and overwrite is false
        assertEquals(testFile.getAbsolutePath(), result.getAsString("filename"));
    }
}
