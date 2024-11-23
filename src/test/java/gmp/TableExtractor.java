package gmp;

import io.restassured.response.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
                System.out.println("Table " + tableIndex + ":");
                tableIndex++;

                // Extract rows
                Elements rows = table.select("tr");
                for (Element row : rows) {
                    // Extract columns (th or td)
                    Elements cols = row.select("th, td");
                    for (Element col : cols) {
                        System.out.print(col.text() + "\t\t");
                    }
                    System.out.println(); // New line after each row
                }
                System.out.println("<<<<<<<<<<<<<<<"); // New line after each table
            }
        } else {
            System.out.println("No tables found in the HTML.");
        }
    }
}

