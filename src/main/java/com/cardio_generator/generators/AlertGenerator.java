package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

public class AlertGenerator implements PatientDataGenerator {
    //This class implements the PatientDataGenerator interface to generate alert data for patients.


    private static final Random randomGenerator = new Random(); // is camelCase and madde the variable name private for correct encapsulation.
    // Random number generator for simulating data variations
    private boolean[] alertStates; // false = resolved, true = pressed   //changed the variable name to camelCase
    // Array to store the alert states for each patient (true = pressed, false = resolved)

    public AlertGenerator(int patientCount) {
        //Constructs an AlertGenerator with the specified number of patients.
        //@param patientCount The number of patients for whom data will be generated.

        alertStates = new boolean[patientCount + 1];
    } //changed the variable name to camelCase

    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        //Generates alert data for the specified patient and outputs it using the provided OutputStrategy.
        //@param patientId      The ID of the patient for whom data will be generated.
        //@param outputStrategy The OutputStrategy used to output the generated data.
        try { // // Check if the alert for the patient is currently pressed
            if (AlertStates[patientId]) {
                if (randomGenerator.nextDouble() < 0.9) { // 90% chance to resolve
                    AlertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                double Lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
                double p = -Math.expm1(-Lambda); // Probability of at least one alert in the period
                boolean alertTriggered = randomGenerator.nextDouble() < p;

                if (alertTriggered) {
                    AlertStates[patientId] = true;  //Mark the alert as triggered
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) { // Print an error message if an exception occurs during data generation
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace(); // Print the stack trace to help identify where the error occurred
        }
    }
}
