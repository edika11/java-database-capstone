package com.project.back_end.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;



@Entity
@Table(name = "doctor")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Doctor {


    // Represents the unique identifier for each doctor
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //  To expose public IDs and avoid leaking of auto-increment IDs.
    @Column(name = "uuid", unique = true, nullable = false, length = 36)
    private String uuid;

    // Represents the doctor's name
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "the name length is between 3 and 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;


    // Represents the medical specialty of the doctor
    @NotBlank(message = "Specialty is required")
    @Size(min = 3, max = 50, message = "the specialty name is between 3 and 50 characters long")
    @Column(name = "specialty", nullable = false, length = 50)
    private String specialty;

    // Represents the doctor's email address
    @NotBlank(message = "email is required")
    @Email(message = "email should have a valid format like doctor@example.com")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    // Represents the doctor's password for login authentication
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "The password must be at least 6 characters long")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false)
    private String password;

    // Represents the doctor's phone number
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{3}-[0-9]{3}-[0-9]{4}$", message = "The phone number should have this format XXX-XXX-XXXX")
    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

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

    // Represents years of experience
    @Min(value = 0, message = "Years of experience cannot be negative")
    @Max(value = 60, message = "Years of experience cannot exceed 60")
    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    // Represents clinic address
    @Size(max = 255, message = "Clinic address cannot exceed 255 characters")
    @Column(name = "clinic_address", length = 255)
    private String clinicAddress;

    //  Description:
    //      - Represents the available times for the doctor in a list of time slots
    //      - Each time slot is represented as a string (e.g., "09:00-10:00", "10:00-11:00")
    @ElementCollection
    @CollectionTable(name = "doctor_available_times", joinColumns = @JoinColumn(name = "doctor_id"))
    @Column(name = "available_times", length = 20)
    private List<String> availableTimes;

    // Relationships with other entities
    @OneToMany(mappedBy = "doctor", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    // Constructors
    public Doctor() {
        this.uuid = UUID.randomUUID().toString();
        this.isDeleted = false;
    }

    public Doctor(String name, String specialty, String email, String password, String phone) {
        this();
        this.name = name;
        this.specialty = specialty;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public Doctor(String name, String specialty, String email, String password, String phone, Integer yearsOfExperience, String clinicAddress) {
        this(name, specialty, email, password, phone);
        this.yearsOfExperience = yearsOfExperience;
        this.clinicAddress = clinicAddress;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getClinicAddress() {
        return clinicAddress;
    }

    public void setClinicAddress(String clinicAddress) {
        this.clinicAddress = clinicAddress;
    }

    public List<String> getAvailableTimes() {
        return availableTimes;
    }

    public void setAvailableTimes(List<String> availableTimes) {
        this.availableTimes = availableTimes;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

