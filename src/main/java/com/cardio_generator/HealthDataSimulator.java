package com.cardio_generator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.cardio_generator.generators.AlertGenerator;

import com.cardio_generator.generators.BloodPressureDataGenerator;
import com.cardio_generator.generators.BloodSaturationDataGenerator;
import com.cardio_generator.generators.BloodLevelsDataGenerator;
import com.cardio_generator.generators.ECGDataGenerator;
import com.cardio_generator.outputs.ConsoleOutputStrategy;
import com.cardio_generator.outputs.fileOutputStrategy;
import com.cardio_generator.outputs.OutputStrategy;
import com.cardio_generator.outputs.TcpOutputStrategy;
import com.cardio_generator.outputs.WebSocketOutputStrategy;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class HealthDataSimulator {
    // Simulates health data for multiple patients and outputs it using various strategies.
    // This class serves as the main entry point for running the health data simulation.
    //It allows customization of the number of patients and the output strategy.


    private static int patientCount = 50; // Default number of patients
    private static ScheduledExecutorService scheduler;
    private static OutputStrategy outputStrategy = new ConsoleOutputStrategy(); // Default output strategy
    private static final Random random = new Random();

    public static void main(String[] args) throws IOException {
        //Main method to run the health data simulation.

        parseArguments(args); //args Command-line arguments passed to the program.

        scheduler = Executors.newScheduledThreadPool(patientCount * 4);

        List<Integer> patientIds = initializePatientIds(patientCount);
        Collections.shuffle(patientIds); // Randomize the order of patient IDs

        scheduleTasksForPatients(patientIds);
    }

    private static void parseArguments(String[] args) throws IOException {
        //parses the command-line arguments and configures the simulation settings accordingly.
        // Loop through each command-line argument
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-h":
                    // Display help message and exit
                    printHelp();
                    System.exit(0);
                    break;
                case "--patient-count":
                    // Parse and set the number of patients for simulation
                    if (i + 1 < args.length) {
                        try {
                            patientCount = Integer.parseInt(args[++i]);
                        } catch (NumberFormatException e) {
                            // Handle invalid input for patient count
                            System.err
                                    .println("Error: Invalid number of patients. Using default value: " + patientCount);
                        }
                    }
                    break;
                case "--output":
                    // Configure the output strategy based on the specified argument
                    if (i + 1 < args.length) {
                        String outputArg = args[++i];
                        if (outputArg.equals("console")) {
                            // Set output strategy to console
                            outputStrategy = new ConsoleOutputStrategy();
                        } else if (outputArg.startsWith("file:")) { // Set output strategy to file
                            String baseDirectory = outputArg.substring(5);
                            Path outputPath = Paths.get(baseDirectory);
                            if (!Files.exists(outputPath)) {
                                Files.createDirectories(outputPath);
                            }
                            outputStrategy = new fileOutputStrategy(baseDirectory);
                        } else if (outputArg.startsWith("websocket:")) { // Set output strategy to WebSocket
                            try {
                                int port = Integer.parseInt(outputArg.substring(10));
                                // Initialize your WebSocket output strategy here
                                outputStrategy = new WebSocketOutputStrategy(port); // Initialize WebSocket output strategy
                                System.out.println("WebSocket output will be on port: " + port);
                            } catch (NumberFormatException e) { // Handle invalid port number for WebSocket
                                System.err.println(
                                        "Invalid port for WebSocket output. Please specify a valid port number.");
                            }
                        } else if (outputArg.startsWith("tcp:")) { // Set output strategy to TCP
                            try {
                                int port = Integer.parseInt(outputArg.substring(4));

                                // Initialize your TCP socket output strategy here
                                outputStrategy = new TcpOutputStrategy(port);
                                System.out.println("TCP socket output will be on port: " + port);
                            } catch (NumberFormatException e) {
                                System.err.println("Invalid port for TCP output. Please specify a valid port number.");
                            }
                        } else { // if there is unknown output type
                            System.err.println("Unknown output type. Using default (console).");
                        }
                    }
                    break;
                default: // Unknown option
                    System.err.println("Unknown option '" + args[i] + "'");
                    printHelp();
                    System.exit(1);
            }
        }
       // @throws IOException If an I/O error occurs while configuring the output strategy.
    }

    private static void printHelp() {
        System.out.println("Usage: java HealthDataSimulator [options]");
        System.out.println("Options:");
        System.out.println("  -h                       Show help and exit.");
        System.out.println(
                "  --patient-count <count>  Specify the number of patients to simulate data for (default: 50).");
        System.out.println("  --output <type>          Define the output method. Options are:");
        System.out.println("                             'console' for console output,");
        System.out.println("                             'file:<directory>' for file output,");
        System.out.println("                             'websocket:<port>' for WebSocket output,");
        System.out.println("                             'tcp:<port>' for TCP socket output.");
        System.out.println("Example:");
        System.out.println("  java HealthDataSimulator --patient-count 100 --output websocket:8080");
        System.out.println(
                "  This command simulates data for 100 patients and sends the output to WebSocket clients connected to port 8080.");
    }

    private static List<Integer> initializePatientIds(int patientCount) {
        //Initializes a list of patient IDs from 1 to the specified patient count.
        //@param patientCount The number of patients to generate IDs for.
        //@return A list of patient IDs.
        List<Integer> patientIds = new ArrayList<>(); // Create a new list to store patient IDs
        for (int i = 1; i <= patientCount; i++) { // Loop from 1 to the specified patient count
            patientIds.add(i); // Add each patient ID to the list
        }
        return patientIds;  // Return the list of patient IDs
    }

    private static void scheduleTasksForPatients(List<Integer> patientIds) {
        //Schedules data generation tasks for each patient.
        //@param patientIds The list of patient IDs.
        //// Initialize data generators for different types of patient data
        ECGDataGenerator ecgDataGenerator = new ECGDataGenerator(patientCount);
        BloodSaturationDataGenerator bloodSaturationDataGenerator = new BloodSaturationDataGenerator(patientCount);
        BloodPressureDataGenerator bloodPressureDataGenerator = new BloodPressureDataGenerator(patientCount);
        BloodLevelsDataGenerator bloodLevelsDataGenerator = new BloodLevelsDataGenerator(patientCount);
        AlertGenerator alertGenerator = new AlertGenerator(patientCount);

        for (int patientId : patientIds) { // Schedule data generation tasks for each patient
            scheduleTask(() -> ecgDataGenerator.generate(patientId, outputStrategy), 1, TimeUnit.SECONDS);
            scheduleTask(() -> bloodSaturationDataGenerator.generate(patientId, outputStrategy), 1, TimeUnit.SECONDS);
            scheduleTask(() -> bloodPressureDataGenerator.generate(patientId, outputStrategy), 1, TimeUnit.MINUTES);
            scheduleTask(() -> bloodLevelsDataGenerator.generate(patientId, outputStrategy), 2, TimeUnit.MINUTES);
            scheduleTask(() -> alertGenerator.generate(patientId, outputStrategy), 20, TimeUnit.SECONDS);
        }
    }

    private static void scheduleTask(Runnable task, long period, TimeUnit timeUnit) {
        //Schedules a task to run at fixed intervals.
        // @param task     The task to be executed.
        //@param period   The time between each execution of the task.
        //@param timeUnit The time unit for the period.
        scheduler.scheduleAtFixedRate(task, random.nextInt(5), period, timeUnit);
    }
}
