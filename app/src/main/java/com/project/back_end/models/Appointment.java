package com.project.back_end.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;


@Entity
@Table(name = "appointments")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Appointment {

    // Enum for the status
    public enum AppointmentStatus {
        SCHEDULED,
        COMPLETED,
        CANCELLED
    }

    // Represents the unique identifier for each appointment.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //  To expose public IDs and avoid leaking of auto-increment IDs.
    @Column(name = "uuid", unique = true, nullable = false, length = 36)
    private String uuid;

    // Represents the doctor assigned to this appointment.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    @NotNull(message = "An appointment should have a doctor")
    private Doctor doctor;


    // Represents the patient assigned to this appointment.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @NotNull(message = "An appointment should have a patient")
    private Patient patient;


    //      - Represents the date and time when the appointment is scheduled to occur.
    //      - The @Future annotation ensures that the appointment time is always in the future when the appointment is created.
    @NotNull(message = "Appointment time is required")
    @Future
    @Column(name = "appointment_time")
    private LocalDateTime appointmentTime;


    //      - Represents the current status of the appointment. It is an integer where:
    //        - 0 means the appointment is scheduled.
    //        - 1 means the appointment has been completed.
    //        - 2 means the appointment has been cancelled.
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @NotNull(message = "status of an appointment cannot be null")
    private AppointmentStatus status;

    // Cancel reason
    @Column(name = "cancel_reason", columnDefinition = "TEXT")
    private String cancelReason;

    // For a soft delete
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    // Timestamp fields
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //    Description:
    //      - It calculates the end time of the appointment by adding one hour to the start time (appointmentTime).
    //      - It is used to get an estimated appointment end time for display purposes.
    @Transient
    private LocalDateTime getEndTime() {
        return appointmentTime.plusHours(1);
    }

    //    Description:
    //      - This method extracts only the date part from the appointmentTime field.
    //      - It returns a LocalDate object representing just the date (without the time) of the scheduled appointment.
    @Transient
    private LocalDate getAppointmentDate() {
        return appointmentTime.toLocalDate();
    }

    //    Description:
    //      - This method extracts only the time part from the appointmentTime field.
    //      - It returns a LocalTime object representing just the time (without the date) of the scheduled appointment.
    @Transient
    private LocalTime getAppointmentTimeOnly() {
        return appointmentTime.toLocalTime();
    }

    // Constructors
    public Appointment() {
        this.uuid = UUID.randomUUID().toString();
        this.status = AppointmentStatus.SCHEDULED;
        this.isDeleted = false;
    }

    public Appointment(Doctor doctor, Patient patient, LocalDateTime appointmentTime, AppointmentStatus status) {
        this();
        this.doctor = doctor;
        this.patient = patient;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

