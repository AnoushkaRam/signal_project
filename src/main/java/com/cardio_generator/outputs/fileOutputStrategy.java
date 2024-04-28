package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

public class fileOutputStrategy implements OutputStrategy {

    private String baseDirectory; //corrected variable's name to stick to the camelCase connvention thing

    // access modifier changed for the encapsulation to be exact
    private final ConcurrentHashMap<String, String> file_map = new ConcurrentHashMap<>(); // coorected for camelCase convention

    public fileOutputStrategy(String baseDirectory) {

        this.BaseDirectory = baseDirectory;
    }

    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.of(baseDirectory)); // cchanged Files.createDirectories to use Path.of for better readability and exception handling
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable
        String filePath = file_map.computeIfAbsent(label, k -> Paths.get(BaseDirectory, label + ".txt").toString());  //changed the FilePath to camelCase
        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.of(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {  //changed Files.newBufferedWriter to use Path.of for better readability and exception handling
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + FilePath + ": " + e.getMessage());
        }
    }
}