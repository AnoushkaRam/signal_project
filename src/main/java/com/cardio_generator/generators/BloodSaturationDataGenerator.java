package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

public class BloodSaturationDataGenerator implements PatientDataGenerator {
    //This class implements the PatientDataGenerator interface to generate blood saturation data for patients.

    private static final Random random = new Random();
    // Random number generator for simulating data variations
    private int[] lastSaturationValues; // Array to store the last saturation values for each patient

    public BloodSaturationDataGenerator(int patientCount) {
        //Constructs a BloodSaturationDataGenerator with the specified number of patients.
        // @param patientCount The number of patients for whom data will be generated.
        lastSaturationValues = new int[patientCount + 1];

        // Initialize with baseline saturation values for each patient
        for (int i = 1; i <= patientCount; i++) {
            lastSaturationValues[i] = 95 + random.nextInt(6); // Initializes with a value between 95 and 100
        }
    }

    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        //Generates blood saturation data for the specified patient and outputs it using the provided OutputStrategy.
        // @param patientId      The ID of the patient for whom data will be generated.
        // @param outputStrategy The OutputStrategy used to output the generated data.
        try {
            // Simulate blood saturation values
            int variation = random.nextInt(3) - 1; // -1, 0, or 1 to simulate small fluctuations
            int newSaturationValue = lastSaturationValues[patientId] + variation;

            // Ensure the saturation stays within a realistic and healthy range
            newSaturationValue = Math.min(Math.max(newSaturationValue, 90), 100);
            lastSaturationValues[patientId] = newSaturationValue;
            outputStrategy.output(patientId, System.currentTimeMillis(), "Saturation", // Output the generated data
                    Double.toString(newSaturationValue) + "%");
        } catch (Exception e) { // Print an error message if an exception occurs during data generation
            System.err.println("An error occurred while generating blood saturation data for patient " + patientId);
            e.printStackTrace(); // This will print the stack trace to help identify where the error occurred.
        }
    }
}
