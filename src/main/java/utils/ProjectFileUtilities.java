package utils;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProjectFileUtilities {

    private static String OUTPUT_FILE;

    private ProjectFileUtilities() {
        throw new UnsupportedOperationException("Initialisation of the class not allowed");

    }

    public static void setLogOutputFileTo(String fileFullPath) {
        OUTPUT_FILE = fileFullPath;
    }

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

    public static void sweepFolderFilesUntochedInLastNDays(int noofDays, String folderPath) {

        // Specify the time frame in milliseconds (1 minute = 60,000 milliseconds)
        long noofMilliSecondsInaDay = 1000L * 60 * 60 * 24; // milliseconds in a day
        long timeFrameInMillis = noofMilliSecondsInaDay * noofDays;

        // Get the current time
        long currentTime = System.currentTimeMillis();

        try {
            // Create a Path object for the folder
            Path folder = Paths.get(folderPath);

            createFolderIfNotExists(String.valueOf(folder.toAbsolutePath()));

            // Iterate through the files in the folder
            Files.walkFileTree(folder, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    // Get the last modified time of the file
                    long lastModifiedTime = attrs.lastModifiedTime().toMillis();

                    // Check if the file hasn't been modified in the last 1 minute
                    if (currentTime - lastModifiedTime > timeFrameInMillis) {
                        // Delete the file
                        Files.delete(file);
                        System.out.println("Deleted temp testData file: " + file);
                    }

                    return FileVisitResult.CONTINUE;
                }
            });
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

    public static void copyFileToTargetLocation(String srcFile, String targetPath) {
        TestConfig.createProjectDirectory(Paths.get(targetPath).getParent().toString());
        try {
            FileUtils.copyFile(new File(srcFile), new File(targetPath));
        } catch (IOException e) {

        }
    }

    public static String readDataFromFile(String filePath) {
        StringBuilder data = new StringBuilder();
        // FileReader and BufferedReader declaration
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        try {
            // Initialize FileReader and BufferedReader
            fileReader = new FileReader(filePath);
            bufferedReader = new BufferedReader(fileReader);

            // Read each line from the file
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // Process each line as needed
                data.append(line);
                data.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close resources in finally block
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data.toString();
    }

    public static void appendFileContents(String sourceFile, String destinationFile) {
        File source;
        File destination;
        try {
            source = new File(sourceFile);
            destination = new File(destinationFile);
            // Read content of source file
            String content = FileUtils.readFileToString(source, Charset.defaultCharset());

            // Append content to destination file
            FileUtils.writeStringToFile(destination, content, Charset.defaultCharset(), true);

            System.out.println("Data appended successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

    }

    public static String adjustFileSize(String filePath, int maxSize) {
        // Create a File object
        File file = new File(filePath);
        String parent = file.getParent();
        String fileName = file.getName().split("_\\d+")[0].split("\\.")[0];
        String extension = file.getName().split("\\.")[1];

        // Get file size in bytes
        long fileSizeBytes = file.length();

        // Check if file size is less than maxSize MB
        if (fileSizeBytes > (maxSize * 1024 * 1024)) {
            filePath = parent + "/" + fileName + "_" + (countFilesWithPattern(parent, fileName) + 1) + "." + extension;
        }

        return filePath;
    }

    public static int countFilesWithPattern(String directoryPath, String fileName) {
        Pattern pattern = Pattern.compile(fileName + "_\\d+\\.");
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files == null) {
            // Directory doesn't exist or is not accessible
            return 0;
        }

        int count = 0;
        for (File file : files) {
            if (file.isFile()) {
                Matcher matcher = pattern.matcher(file.getName());
                if (matcher.find()) {
                    count++;
                }
            }
        }
        return count;
    }

    public static void deleteRecursively(String fileOrFolderPath) {
        Path path = Paths.get(fileOrFolderPath);
        if (Files.notExists(path)) {
            return;
        }

        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    FileUtils.forceDelete(file.toFile());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    FileUtils.forceDelete(dir.toFile());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    FileUtils.forceDelete(file.toFile());
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
        }
    }
}
