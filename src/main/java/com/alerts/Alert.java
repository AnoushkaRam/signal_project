package com.alerts;

public class Alert implements AlertInterface {
    private int patientId; // Unique identifier for the patient
    private String condition; // Condition that triggered the alert
    private long timestamp; // Timestamp when the alert was created
    private String priority; // Priority level of the alert

    /**
     * Constructor to initialize an Alert object with patient ID, condition, timestamp, and priority.
     *
     * @param patientId the unique identifier for the patient
     * @param condition the condition that triggered the alert
     * @param timestamp the timestamp when the alert was created
     * @param priority  the priority level of the alert
     */
    public Alert(int patientId, String condition, long timestamp, String priority) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
        this.priority = priority;
    }

    /**
     * Retrieves the priority level of the alert.
     *
     * @return the priority level as a String
     */
    @Override
    public String getPriority() {
        return priority;
    }

    /**
     * Retrieves the timestamp when the alert was created.
     *
     * @return the timestamp as a long
     */
    @Override
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Retrieves the condition that triggered the alert.
     *
     * @return the condition as a String
     */
    @Override
    public String getCondition() {
        return condition;
    }

    /**
     * Retrieves the unique identifier of the patient.
     *
     * @return the patient ID as a String
     */
    @Override
    public String getPatientId() {
        return String.valueOf(patientId);
    }

    /**
     * Returns a string representation of the alert.
     *
     * @return a simplified string representation of the condition
     */
    @Override
    public String toString() {
        return condition; // Simplified for decorators to add their string
    }
}

