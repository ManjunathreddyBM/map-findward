package com.bbmp.map.service;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

@Service
public class GeoLocationFinder {
    private String readGeoJsonFile(String fileName) throws IOException {
        StringBuilder content = new StringBuilder();

        try (InputStream inputStream = getClass().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }

        return content.toString();
    }
    public HashMap<String , String> findWard(double latitude, double longitude) {
        try {
            // Load GeoJSON data from file
           // String geoJsonData = readGeoJsonFile("Latitude And Logitude BBMP.txt");

            // Parse GeoJSON data
            String filePath = "E:\\bbmp-projects\\Latitude And Logitude BBMP .txt"; // Path to your GeoJSON file
            String geoJsonData = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONObject geoJson = new JSONObject(geoJsonData);

            // Find the ward name
            return findWardName(latitude, longitude, geoJson);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HashMap<String,String> findWardName(double latitude, double longitude, JSONObject geoJson) {
        JSONArray features = geoJson.getJSONArray("features");
        HashMap<String,String> map =new HashMap<>();

        for (int i = 0; i < features.length(); i++) {
            JSONObject feature = features.getJSONObject(i);
            JSONObject geometry = feature.getJSONObject("geometry");

            if (geometry.getString("type").equals("MultiPolygon")) {
                JSONArray coordinates = geometry.getJSONArray("coordinates");
                for (int j = 0; j < coordinates.length(); j++) {
                    JSONArray polygon = coordinates.getJSONArray(j).getJSONArray(0);
                    if (pointInPolygon(latitude, longitude, polygon)) {
                        //System.out.println("WARD NO: " + feature.getJSONObject("properties").getString("WARD_NO"));
                        String wardName =  feature.getJSONObject("properties").getString("WARD_NAME");
                        String wardNo = feature.getJSONObject("properties").getDouble("WARD_NO") + "";
                        map.put("wardName",wardName);
                        map.put("wardNumber",wardNo);
                        return  map;

                    }
                }
            }
        }
        map.put("wardName","No ward found");
        map.put("wardNumber", "No ward Id");
        return map;
    }

    private boolean pointInPolygon(double latitude, double longitude, JSONArray polygon) {
        int n = polygon.length();
        boolean inside = false;
        double[] x = new double[n];
        double[] y = new double[n];

        for (int i = 0; i < n; i++) {
            JSONArray coord = polygon.getJSONArray(i);
            x[i] = coord.getDouble(0);
            y[i] = coord.getDouble(1);
        }

        for (int i = 0, j = n - 1; i < n; j = i++) {
            if ((y[i] > latitude) != (y[j] > latitude) &&
                    (longitude < (x[j] - x[i]) * (latitude - y[i]) / (y[j] - y[i]) + x[i])) {
                inside = !inside;
            }
        }

        return inside;
    }
}
