//An interface representing a strategy for outputting patient data in health monitoring simulations.
package com.cardio_generator.outputs;

public interface OutputStrategy {
    //Outputs patient data to the specified destination.
    void output(int patientId, long timestamp, String label, String data);
    //param patientId  = The ID of the patient associated with the data.
    //@param timestamp  = The timestamp indicating when the data was recorded.
    //@param label =  The label or type of data being output.
    //@param data  =  The actual data to be output.
}
