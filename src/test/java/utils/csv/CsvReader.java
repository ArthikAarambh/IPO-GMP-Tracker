package utils.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import utils.TestConfig;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CsvReader {
    private static HashMap<String,CSVRecord>TEST_DATA = new HashMap<>();//For future use

    public static Object[][] data(String csvName){
        ArrayList<ArrayList<String>>IPO_DATA= new ArrayList<>();
        String csvFile= TestConfig.TEST_REPORT_PATH+"/"+csvName+".csv";
        try (Reader reader = new FileReader(csvFile);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)){
            for (CSVRecord csvRecord : csvParser){
              TEST_DATA.put(csvRecord.get(0),csvRecord);
                IPO_DATA.add((extractCellData(csvRecord)));
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        IPO_DATA.remove(0);
        int rows = IPO_DATA.size();
        int cols = IPO_DATA.get(0).size();
        String[][] array = new String[rows][cols];
        for (int i=0; i<rows;i++){
            for(int j=0;j<cols;j++){
                array[i][j]=IPO_DATA.get(i).get(j);
            }
        }

        return array;
    }

    private static ArrayList<String>extractCellData(CSVRecord csvRecord){
        ArrayList<String>cellData = new ArrayList<>();
        cellData.add(csvRecord.get(0));
//        cellData.add(csvRecord.get(1));
        cellData.add(csvRecord.get(2));
//        cellData.add(csvRecord.get(3));

        return cellData;
    }
    public static void main(String[] args){
        Object[][] ipo = data("table_InvestorGain");
        int rows = ipo.length;
        int cols = ipo[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(ipo[i][j] + "\t");
            }
            System.out.println();
        }
    }

}
