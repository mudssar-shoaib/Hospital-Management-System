import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    static final String DATA_FOLDER = "data";

    static void ensureDataFolder() {
        File folder = new File(DATA_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    static void writeLines(String fileName, List<String> lines) {
        ensureDataFolder();
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_FOLDER + "/" + fileName))) {
            for (String line : lines) {
                pw.println(line);
            }
        } catch (IOException e) {
            System.out.println("  [File Error] Could not save " + fileName + " (" + e.getMessage() + ")");
        }
    }

    static List<String> readLines(String fileName) {
        List<String> lines = new ArrayList<>();
        File f = new File(DATA_FOLDER + "/" + fileName);
        if (!f.exists()) {
            return lines;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("  [File Error] Could not read " + fileName + " (" + e.getMessage() + ")");
        }
        return lines;
    }
}