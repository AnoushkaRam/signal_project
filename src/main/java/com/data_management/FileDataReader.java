package com.data_management;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileDataReader implements DataReader {
    private final Path directoryPath; // Path to the directory containing data files

    /**
     * Constructor to initialize FileDataReader with the specified directory path.
     *
     * @param directoryPath the path to the directory containing data files
     */
    public FileDataReader(Path directoryPath) {
        if (!Files.isDirectory(directoryPath)) {
            throw new IllegalArgumentException("Specified path is not a directory: " + directoryPath);
        }
        this.directoryPath = directoryPath;
    }

    /**
     * Reads data from files in the directory and stores it in DataStorage.
     *
     * @param dataStorage the storage object to store the read data
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        try (Stream<Path> stream = Files.list(directoryPath)) {
            stream.filter(Files::isRegularFile).forEach(file -> {
                try {
                    parseAndStoreData(file, dataStorage);
                } catch (IOException e) {
                    handleFileError(file, e);
                }
            });
        }
    }

    /**
     * Parses data from a file and stores it in DataStorage.
     *
     * @param filePath    the path to the file to be read
     * @param dataStorage the storage object to store the parsed data
     * @throws IOException if an I/O error occurs
     */
    private void parseAndStoreData(Path filePath, DataStorage dataStorage) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int patientId = Integer.parseInt(parts[0].trim());
                    double measurementValue = Double.parseDouble(parts[1].trim());
                    String recordType = parts[2].trim();
                    long timestamp = Long.parseLong(parts[3].trim());
                    dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
                }
            }
        }
    }

    /**
     * Handles errors encountered while reading a file.
     *
     * @param filePath the path to the file that caused the error
     * @param e        the IOException encountered
     */
    private void handleFileError(Path filePath, IOException e) {
        System.err.println("Error reading file: " + filePath);
        e.printStackTrace();
    }
}
