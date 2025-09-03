package com.project.back_end.models;


import jakarta.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "prescriptions")
public class Prescription {

    // Represents the unique identifier for each prescription
    @Id
    private String id;


    // Represents the name of the patient receiving the prescription
    @NotBlank(message = "The patient name is required")
    @Size(min=3, max=100, message = "The name length should be between 3 and 100 characters")
    @Field("patient_name")
    private String patientName;

    // Represents the name of the Doctor giving the prescription
    @NotBlank(message = "The doctor name is required")
    @Size(min=3, max=100, message = "The name length should be between 3 and 100 characters")
    @Field("doctor_name")
    private String doctorName;

    // Represents the ID of the associated appointment where the prescription was given
    @NotNull(message = "The appointment ID is required")
    @Field("appointment_id")
    private Long appointmentId;


    // Represents the medication prescribed to the patient
    @NotBlank(message = "The medication name is required")
    @Size(min = 3, max = 100, message = "The medication name is between 3 and 100 characters")
    @Field("medication")
    private String medication;


    // Represents the dosage information for the prescribed medication
    @NotBlank(message = "The dosage should be provided")
    @Field("dosage")
    private String dosage;


    // Represents any additional notes or instructions from the doctor regarding the prescription
    @Size(max = 500, message = "The doctor's notes should not exceed 200 characters")
    @Field("doctor_notes")
    private String doctorNotes;

    // Represents the refill count
    @Min(value = 0, message = "Refill count cannot be negative")
    @Max(value = 10, message = "Refill count cannot exceed 10")
    @Field("refill_count")
    private Integer refillCount;

    // Represents the pharmacy name
    @Size(max = 100, message = "Pharmacy name should not exceed 100 characters")
    @Field("pharmacy_name")
    private String pharmacyName;

    // Constructors
    public Prescription() {}

    public Prescription(String patientName, String doctorName, Long appointmentId, String medication, String dosage, String doctorNotes, Integer refillCount, String pharmacyName) {
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.appointmentId = appointmentId;
        this.medication = medication;
        this.dosage = dosage;
        this.doctorNotes = doctorNotes;
        this.refillCount = refillCount;
        this.pharmacyName = pharmacyName;
    }

    public Prescription(String patientName, String doctorName, Long appointmentId, String medication, String dosage, String doctorNotes) {
        this(patientName, doctorName, appointmentId, medication, dosage, doctorNotes, 0, null);
    }


    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDoctorNotes() {
        return doctorNotes;
    }

    public void setDoctorNotes(String doctorNotes) {
        this.doctorNotes = doctorNotes;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public Integer getRefillCount() {
        return refillCount;
    }

    public void setRefillCount(Integer refillCount) {
        this.refillCount = refillCount;
    }

    public String getPharmacyName() {
        return pharmacyName;
    }

    public void setPharmacyName(String pharmacyName) {
        this.pharmacyName = pharmacyName;
    }
}
