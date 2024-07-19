// Source code is decompiled from a .class file using FernFlower decompiler.
package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.json.JSONObject;

import static utils.TestLogger.info;


public class JsonReader {
    private JsonReader() {
        throw new IllegalStateException("Device class");
    }

    public static JSONObject getJsonData(String fileFullPath) {
//        info("File path: " + fileFullPath);
        JSONObject jsonData = new JSONObject();

        try {
            Path path = Paths.get(fileFullPath);
            String fileContent = new String(Files.readAllBytes(path));
            jsonData = new JSONObject(fileContent);
        } catch (IOException var7) {
            info(var7.getMessage());
        }

        return jsonData;
    }

    public static void saveDataToJsonFile(String filePath, JSONObject jsonObject) {
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(filePath);
            fileWriter.write(jsonObject.toString(4));
            info("JSON data has been written to " + filePath);

            fileWriter.close();

        } catch (IOException e) {
            // Exception occurred during write
            e.printStackTrace();
        }
    }
}
