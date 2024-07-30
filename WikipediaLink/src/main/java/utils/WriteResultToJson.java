package utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class WriteResultToJson {

    public void writeResultsToJsonFile(Set<String> links) {
        JSONObject result = new JSONObject();
        result.put("total_count", links.size());
        result.put("unique_count", links.size());

        JSONArray linkArray = new JSONArray();
        for (String link : links) {
            linkArray.put(link);
        }
        result.put("links", linkArray); // Add the array to the result JSON object

        String strFile = System.getProperty("user.dir") + "/test-output/jsonFile/";
        File jsonFolder = new File(strFile);
        if (!jsonFolder.exists()) {
            jsonFolder.mkdirs();
        }
        try (FileWriter file = new FileWriter(strFile + "wiki_links.json")) {
            file.write(result.toString(4));  // Pretty print with an indent of 4 spaces
            System.out.println("Results written to wiki_links.json"); // Ensure this message is printed
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}