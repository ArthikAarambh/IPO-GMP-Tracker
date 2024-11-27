package htmlutils;

import io.restassured.response.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;

import static utils.TestConfig.TEST_REPORT_PATH;

public class TableExtractor {
    public static void ext(Response htmlResponse) {

        // Parse the HTML
        Document doc = Jsoup.parse(htmlResponse.getBody().asString());
//        System.out.println(doc);
        // Select the table
        // Select all tables
        Elements tables = doc.select("table");

        if (!tables.isEmpty()) {
            int tableIndex = 1;
            for (Element table : tables) {
                String fileName = TEST_REPORT_PATH+"/table_" + tableIndex + ".csv";
                try {
                    writeTableToCSV(table, fileName); // Call method to save table
                    System.out.println("Table " + tableIndex + " saved to " + fileName);
                } catch (IOException e) {
                    System.err.println("Error saving Table " + tableIndex + ": " + e.getMessage());
                }
                tableIndex++;
            }
        } else {
            System.out.println("No tables found in the HTML.");
        }
    }
    public static void writeTableToCSV(Element table, String fileName) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            // Extract rows from the table
            Elements rows = table.select("tr");
            for (Element row : rows) {
                // Extract columns (th or td) from the row
                Elements cols = row.select("th, td");
                for (int i = 0; i < cols.size(); i++) {
                    writer.append(cols.get(i).text());
                    if (i < cols.size() - 1) {
                        writer.append(","); // Add a comma separator
                    }
                }
                writer.append("\n"); // New line after each row
            }
        }
    }
}

