//An interface for generating patient data in health monitoring simulations.

package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

public interface PatientDataGenerator {
    //Generates patient data based on the provided patient ID and output strategy.
    void generate(int patientId, OutputStrategy outputStrategy);
    //@param patientId      The ID of the patient for which data is generated.
    //@param outputStrategy The strategy for outputting the generated data.
}
