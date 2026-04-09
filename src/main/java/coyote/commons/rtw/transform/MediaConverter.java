package coyote.commons.rtw.transform;

import coyote.commons.dataframe.DataFrame;
import coyote.commons.log.Log;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * MediaConverter transform reads a filename from the data frame and calls ffmpeg 
 * to convert the file to a configured format.
 */
public class MediaConverter extends AbstractFrameTransform {

    public static final String FFMPEG = "ffmpeg";
    public static final String FORMAT = "format";
    public static final String OVERWRITE = "overwrite";
    public static final String FILENAME = "filename";

    @Override
    public DataFrame process(DataFrame frame) {
        return performTransform(frame);
    }

    protected DataFrame performTransform(DataFrame frame) {
        if (frame != null && frame.contains(FILENAME)) {
            String sourcePath = frame.getAsString(FILENAME);
            if (sourcePath != null && !sourcePath.trim().isEmpty()) {
                File sourceFile = new File(sourcePath);
                if (sourceFile.exists() && sourceFile.isFile()) {
                    convert(sourceFile, frame);
                } else {
                    Log.warn("MediaConverter: Source file does not exist or is not a file: " + sourcePath);
                }
            }
        }
        return frame;
    }

    private void convert(File sourceFile, DataFrame frame) {
        String format = getString(FORMAT);
        if (format == null || format.trim().isEmpty()) {
            Log.warn("MediaConverter: No conversion format specified in configuration.");
            return;
        }

        String ffmpegPath = getString(FFMPEG);
        if (ffmpegPath == null || ffmpegPath.trim().isEmpty()) {
            ffmpegPath = "ffmpeg";
        }

        boolean overwrite = getBoolean(OVERWRITE);

        String baseName = getBaseName(sourceFile.getName());
        File targetFile = new File(sourceFile.getParentFile(), baseName + "." + format);

        if (targetFile.exists() && !overwrite) {
            Log.warn("MediaConverter: Target file already exists and overwrite is set to false: " + targetFile.getAbsolutePath());
            return;
        }

        List<String> command = new ArrayList<>();
        command.add(ffmpegPath);
        if (overwrite) {
            command.add("-y");
        } else {
            command.add("-n");
        }
        command.add("-i");
        command.add(sourceFile.getAbsolutePath());
        command.add(targetFile.getAbsolutePath());

        ProcessBuilder pb = new ProcessBuilder(command);
        try {
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                frame.put(FILENAME, targetFile.getAbsolutePath());
            } else {
                Log.warn("MediaConverter: ffmpeg failed with exit code " + exitCode + " for file " + sourceFile.getAbsolutePath());
            }
        } catch (IOException | InterruptedException e) {
            Log.warn("MediaConverter: Error executing ffmpeg for file " + sourceFile.getAbsolutePath() + ": " + e.getMessage());
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private String getBaseName(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index == -1) {
            return fileName;
        } else {
            return fileName.substring(0, index);
        }
    }
}
