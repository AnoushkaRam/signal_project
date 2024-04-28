package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

public class fileOutputStrategy implements OutputStrategy {
    //his class implements the OutputStrategy interface to provide functionality for outputting data to files.

    private String baseDirectory; //corrected variable's name to stick to the camelCase connvention thing
    // The base directory where output files will be stored

    // access modifier changed for the encapsulation to be exact

    private final ConcurrentHashMap<String, String> file_map = new ConcurrentHashMap<>(); // coorected for camelCase convention
    // A map to store file paths based on label names
    public fileOutputStrategy(String baseDirectory) {
        //Constructs a FileOutputStrategy with the specified base directory.
        //@param baseDirectory The base directory where output files will be stored.
        //Outputs patient data to a file.
        this.BaseDirectory = baseDirectory;

    }

    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        //@param patientId The ID of the patient.
        //@param timestamp The timestamp of the data.
        //@param label     The label associated with the data.
        //@param data      The data to be written to the file.
        try {
            // Create the directory
            Files.createDirectories(Paths.of(baseDirectory)); // cchanged Files.createDirectories to use Path.of for better readability and exception handling
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable
        // Compute the file path based on the label
        String filePath = file_map.computeIfAbsent(label, k -> Paths.get(BaseDirectory, label + ".txt").toString());  //changed the FilePath to camelCase
        // Write the data to the file
        try (PrintWriter out = new PrintWriter(  // Write the data to the file
                Files.newBufferedWriter(Paths.of(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {  //changed Files.newBufferedWriter to use Path.of for better readability and exception handling
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + FilePath + ": " + e.getMessage());
        }
    }
}
