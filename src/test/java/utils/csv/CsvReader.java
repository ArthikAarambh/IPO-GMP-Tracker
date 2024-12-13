package utils.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import utils.TestConfig;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CsvReader {
    private static HashMap<String,CSVRecord>TEST_DATA = new HashMap<>();//For future use
//    public static ArrayList<ArrayList<String>>IPO_DATA= new ArrayList<>();


    public static Object[][] data(String csvName){
        ArrayList<ArrayList<String>>IPO_Data= new ArrayList<>();
        String csvFile= TestConfig.TEST_REPORT_PATH+"/"+csvName+".csv";
        try (Reader reader = new FileReader(csvFile);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)){
            for (CSVRecord csvRecord : csvParser){
                  TEST_DATA.put(csvRecord.get(0),csvRecord);
              if(csvName.equalsIgnoreCase("table_InvestorGain")){
                  IPO_Data.add((extractCellData(csvRecord)));
              } else if (csvName.equalsIgnoreCase("table_IpoPremium")) {
                  IPO_Data.add((extractCellDataPremium(csvRecord)));
              } else if (csvName.equalsIgnoreCase("table_IpoWatch")) {
                IPO_Data.add((extractCellDataWatch(csvRecord)));
            }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        IPO_Data.remove(0);
        int rows = IPO_Data.size();
        int cols = IPO_Data.get(0).size();
        String[][] array = new String[rows][cols];
        for (int i=0; i<rows;i++){
            for(int j=0;j<cols;j++){
                array[i][j]=IPO_Data.get(i).get(j);
            }
        }

        return array;
    }

    private static ArrayList<String>extractCellData(CSVRecord csvRecord){
        ArrayList<String>cellData = new ArrayList<>();
        String columnValue = csvRecord.get(0);
        String[] splitParts = columnValue.split("SME|IPO");
        String processedValue = (splitParts.length > 0) ? splitParts[0].trim() : columnValue.trim();
        cellData.add(processedValue);
//        cellData.add(csvRecord.get(0).split("SME|IPO")[0]);
        cellData.add(csvRecord.get(2));
//        cellData.add(csvRecord.get(3));

        return cellData;
    }


    private static ArrayList<String>extractCellDataPremium(CSVRecord csvRecord){
        ArrayList<String>cellData = new ArrayList<>();
        String columnValue = csvRecord.get(0);
        String[] splitParts = columnValue.split("\\(");
        String processedValue = (splitParts.length > 0) ? splitParts[0].trim() : columnValue.trim();
        cellData.add(processedValue);
        cellData.add(csvRecord.get(1));
//        cellData.add(csvRecord.get(3));

        return cellData;
    }

    private static ArrayList<String>extractCellDataWatch(CSVRecord csvRecord){
        ArrayList<String>cellData = new ArrayList<>();
        String columnValue = csvRecord.get(0);
        String[] splitParts = columnValue.split("\\d+");
        String processedValue = (splitParts.length > 0) ? splitParts[0].trim() : columnValue.trim();
        cellData.add(processedValue);
        cellData.add(csvRecord.get(1));
//        cellData.add(csvRecord.get(3));

        return cellData;
    }



//    public static void main(String[] args){
//       printCSVData("table_InvestorGain");
//       printCSVData("table_IpoPremium");
//       printCSVData("table_IpoWatch");
//    }
    public static void printCSVData(String csvName){
        Object[][] ipo = data(csvName);
        int rows = ipo.length;
        int cols = ipo[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(ipo[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println("\n");
    }

}
