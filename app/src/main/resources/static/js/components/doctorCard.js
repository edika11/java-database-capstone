// doctorCard.js

// Import necessary functions
import { deleteDoctor } from '../services/doctorServices.js';
import { getPatientData } from '../services/patientServices.js';
import { showBookingOverlay } from './loggedPatient.js';

/**
 * Function to create and return a DOM element for a single doctor card
 * @param {Object} doctor - Doctor object containing information
 * @returns {HTMLElement} Doctor card element
 */
export function createDoctorCard(doctor) {
    // Create the main container for the doctor card
    const card = document.createElement("div");
    card.classList.add("doctor-card");

    // Retrieve the current user role from localStorage
    const role = localStorage.getItem("userRole");
    const token = localStorage.getItem("token");

    // Create a div to hold doctor information
    const infoDiv = document.createElement("div");
    infoDiv.classList.add("doctor-info");

    // Create and set the doctor's name
    const name = document.createElement("h3");
    name.textContent = doctor.name || "Doctor Name";
    name.classList.add("doctor-name");

    // Create and set the doctor's specialization
    const specialization = document.createElement("p");
    specialization.textContent = `Specialty: ${doctor.specialty || "Not specified"}`;
    specialization.classList.add("doctor-specialty");

    // Create and set the doctor's email
    const email = document.createElement("p");
    email.textContent = `Email: ${doctor.email || "No email available"}`;
    email.classList.add("doctor-email");

    // Create and list available appointment times
    const availability = document.createElement("p");
    availability.textContent = `Available: ${doctor.availability ? doctor.availability.join(", ") : "Not specified"}`;
    availability.classList.add("doctor-availability");

    // Append all info elements to the doctor info container
    infoDiv.appendChild(name);
    infoDiv.appendChild(specialization);
    infoDiv.appendChild(email);
    infoDiv.appendChild(availability);

    // Create a container for card action buttons
    const actionsDiv = document.createElement("div");
    actionsDiv.classList.add("card-actions");

    // === ADMIN ROLE ACTIONS ===
    if (role === "admin") {
        // Create a delete button
        const deleteBtn = document.createElement("button");
        deleteBtn.textContent = "Delete";
        deleteBtn.classList.add("delete-btn");

        // Add click handler for delete button
        deleteBtn.addEventListener("click", async () => {
            // Get the admin token from localStorage
            const adminToken = localStorage.getItem("token");

            if (!adminToken) {
                alert("Admin authentication required. Please log in again.");
                return;
            }

            // Confirm deletion
            const isConfirmed = confirm(`Are you sure you want to delete Dr. ${doctor.name}?`);

            if (isConfirmed) {
                try {
                    // Call API to delete the doctor
                    const result = await deleteDoctor(doctor.id, adminToken);

                    // Show result and remove card if successful
                    if (result.success) {
                        alert(`Doctor ${doctor.name} has been deleted successfully.`);
                        card.remove();
                    } else {
                        alert(`Failed to delete doctor: ${result.message}`);
                    }
                } catch (error) {
                    console.error("Error deleting doctor:", error);
                    alert("An error occurred while deleting the doctor.");
                }
            }
        });

        // Add delete button to actions container
        actionsDiv.appendChild(deleteBtn);
    }

    // === PATIENT (NOT LOGGED-IN) ROLE ACTIONS ===
    else if (role === "patient") {
        // Create a book now button
        const bookBtn = document.createElement("button");
        bookBtn.textContent = "Book Now";
        bookBtn.classList.add("book-btn");

        // Alert patient to log in before booking
        bookBtn.addEventListener("click", () => {
            alert("Please log in to book an appointment.");
            // Optionally redirect to login page or open login modal
            window.location.href = "/pages/patientDashboard.html";
        });

        // Add button to actions container
        actionsDiv.appendChild(bookBtn);
    }

    // === LOGGED-IN PATIENT ROLE ACTIONS ===
    else if (role === "loggedPatient") {
        // Create a book now button
        const bookBtn = document.createElement("button");
        bookBtn.textContent = "Book Now";
        bookBtn.classList.add("book-btn");

        // Handle booking logic for logged-in patient
        bookBtn.addEventListener("click", async (e) => {
            // Redirect if token not available
            if (!token) {
                alert("Session expired. Please log in again.");
                localStorage.removeItem("userRole");
                window.location.href = "/pages/patientDashboard.html";
                return;
            }

            try {
                // Fetch patient data with token
                const patientData = await getPatientData(token);

                // Show booking overlay UI with doctor and patient info
                showBookingOverlay(e, doctor, patientData);
            } catch (error) {
                console.error("Error fetching patient data:", error);
                alert("Failed to load patient information. Please try again.");
            }
        });

        // Add button to actions container
        actionsDiv.appendChild(bookBtn);
    }

    // Append doctor info and action buttons to the card
    card.appendChild(infoDiv);
    card.appendChild(actionsDiv);

    // Return the complete doctor card element
    return card;
}