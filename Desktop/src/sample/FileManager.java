package sample;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileManager {
    public static String getStringFromFile(String fileName){
        String jsonString ="";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("src/sample/sample_data/" + fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            StringBuilder sb = new StringBuilder();
            String line = "";
            line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
             jsonString = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonString;
    }
}
