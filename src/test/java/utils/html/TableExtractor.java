package utils.html;

import io.restassured.response.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;

import static utils.TestConfig.TEST_REPORT_PATH;

public class TableExtractor {
    public static void ext(Response htmlResponse,String csvName) {

        // Parse the HTML
        Document doc = Jsoup.parse(htmlResponse.getBody().asString());
//        System.out.println(doc);
        // Select the table
        // Select all tables
        Elements tables = doc.select("table");
        String fileName = TEST_REPORT_PATH+"/table_"+csvName+".csv";

        if (!tables.isEmpty()) {
            try (FileWriter writer = new FileWriter(fileName)) {
                int tableIndex = 1;
                for (Element table : tables) {
                    writeTableToCSV(table, writer);

                    // Add a blank line and message after each table
                    writer.append("\n");
                    writer.append(">>>Table ").append(String.valueOf(tableIndex)).append(" data ended<<<\n");
                    writer.append("\n");

                    System.out.println("Table " + tableIndex + " appended to CSV.");
                    tableIndex++;
                }
                System.out.println("All tables appended to " + fileName);
            } catch (IOException e) {
                System.err.println("Error writing to CSV: " + e.getMessage());
            }
        } else {
            System.out.println("No tables found in the HTML.");
        }
    }
    public static void writeTableToCSV(Element table, FileWriter writer) throws IOException {
        Elements rows = table.select("tr");
        for (Element row : rows) {
            Elements cols = row.select("th, td");
            for (int i = 0; i < cols.size(); i++) {
                writer.append(cols.get(i).text());
                if (i < cols.size() - 1) {
                    writer.append(","); // Add comma separator
                }
            }
            writer.append("\n"); // New line after each row
        }
    }
}

