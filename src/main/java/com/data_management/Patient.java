package com.data_management;

import java.util.ArrayList;
import java.util.List;

public class Patient {
    private int patientId;  // Unique identifier for the patient
    private List<PatientRecord> patientRecords;  // List to store patient records

    /**
     * Constructor to initialize Patient object with patientId.
     * Initializes an empty list for patient records.
     *
     * @param patientId the unique identifier for the patient
     */
    public Patient(int patientId) {
        this.patientId = patientId;
        this.patientRecords = new ArrayList<>();
    }

    /**
     * Adds a new record to the patient's record list.
     *
     * @param measurementValue the value of the measurement
     * @param recordType       the type of record (e.g., blood pressure, heart rate)
     * @param timestamp        the timestamp of the record
     */
    public void addRecord(double measurementValue, String recordType, long timestamp) {
        this.patientRecords.add(new PatientRecord(this.patientId, measurementValue, recordType, timestamp));
    }

    /**
     * Retrieves patient records within a specified time range.
     *
     * @param startTime the start time of the range (inclusive)
     * @param endTime   the end time of the range (inclusive)
     * @return a list of PatientRecord objects within the specified time range
     */
    public List<PatientRecord> getRecords(long startTime, long endTime) {
        List<PatientRecord> recordsInRange = new ArrayList<>();
        for (PatientRecord record : this.patientRecords) {
            if (isRecordInRange(record, startTime, endTime)) {
                recordsInRange.add(record);
            }
        }
        return recordsInRange;
    }

    /**
     * Checks if a record's timestamp is within the specified time range.
     *
     * @param record    the PatientRecord object to check
     * @param startTime the start time of the range (inclusive)
     * @param endTime   the end time of the range (inclusive)
     * @return true if the record's timestamp is within the range, false otherwise
     */
    private boolean isRecordInRange(PatientRecord record, long startTime, long endTime) {
        return record.getTimestamp() >= startTime && record.getTimestamp() <= endTime;
    }

    /**
     * Retrieves the unique identifier of the patient.
     *
     * @return the patientId
     */
    public int getPatientId() {
        return this.patientId;
    }
}


