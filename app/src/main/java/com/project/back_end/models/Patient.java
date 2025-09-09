package com.project.back_end.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "patient")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Patient {

    // Enum for gender
    public enum Gender {
        MALE,
        FEMALE,
        OTHER
    }

    // Represents the unique identifier for each patient.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //  To expose public IDs and avoid leaking of auto-increment IDs.
    @Column(name = "uuid", unique = true, nullable = false, length = 36)
    private String uuid;

    // Represents the patient's full name
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "the name length is between 3 and 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;


    // Represents the patient's email address
    @NotBlank(message = "email is required")
    @Email(message = "email should have a valid format like patient@example.com")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    // Represents the patient's password for login authentication
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "The password must be at least 6 characters long")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false)
    private String password;

    // Represents the patient's phone number
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{3}-[0-9]{3}-[0-9]{4}$", message = "The phone number should have this format XXX-XXX-XXXX")
    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    // Represents the patient's address
    @NotBlank(message = "Adress is required")
    @Size(max = 255, message = "Address should not exceed 255 characters in length")
    @Column(name = "address", nullable = false, length = 255)
    private String address;

    // Represents date of birth
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    // Represents gender
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 10)
    @NotNull(message = "Gender is required")
    private Gender gender;

    // Relationships with other entities
    @OneToMany(mappedBy = "patient", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    // Timestamp fields
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Patient() {
        this.uuid = UUID.randomUUID().toString();
    }

    public Patient(String name, String email, String password, String phone, String address, LocalDate dateOfBirth, Gender gender) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
