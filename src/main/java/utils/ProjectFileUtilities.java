package utils;
import java.io.*;
import java.nio.file.*;

public class ProjectFileUtilities {

    private static String OUTPUT_FILE="test-report/master.txt";
    public static String writeLogsToFile(String logMsg) {
        writeLogsToFile(OUTPUT_FILE, logMsg);
        return logMsg;
    }

    public static void writeLogsToFile(String filePath, String logMsg) {
        Path path = FileSystems.getDefault().getPath(filePath);
        FileWriter writer = null;
        try {
            // Create directories if they don't exist
            createFolderIfNotExists(String.valueOf(path.getParent()));
            createFileIfNotExists(filePath);

            // Append text to the file
            writer = new FileWriter(filePath, true);
            writer.append(logMsg);
            writer.append("\n");
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createFolderIfNotExists(String absolutePath) {
        if (!new File(absolutePath).exists()) {
            try {
                Files.createDirectories(Path.of(absolutePath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean createFileIfNotExists(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                System.out.println("creating file: " + filePath);
                file.createNewFile();
                return true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
