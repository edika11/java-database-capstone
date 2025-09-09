// header.js

/**
 * Renders the header section based on user role and session status
 */
function renderHeader() {
    const headerDiv = document.getElementById("header");
    if (!headerDiv) return;

    // Check if current page is the root page
    if (window.location.pathname.endsWith("/")) {
        localStorage.removeItem("userRole");
        localStorage.removeItem("token");
        headerDiv.innerHTML = `
            <header class="header">
                <div class="logo-section">
                    <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
                    <span class="logo-title">Hospital CMS</span>
                </div>
            </header>`;
        return;
    }

    // Retrieve user role and token from localStorage
    const role = localStorage.getItem("userRole");
    const token = localStorage.getItem("token");

    // Handle session expiry or invalid login
    if ((role === "loggedPatient" || role === "admin" || role === "doctor") && !token) {
        localStorage.removeItem("userRole");
        alert("Session expired or invalid login. Please log in again.");
        window.location.href = "/";
        return;
    }

    // Initialize header content with logo section
    let headerContent = `<header class="header">
        <div class="logo-section">
            <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
            <span class="logo-title">Hospital CMS</span>
        </div>
        <nav class="nav-items">`;

    // Add role-specific header content
    if (role === "admin") {
        headerContent += `
            <button id="addDocBtn" class="adminBtn" onclick="openModal('addDoctor')">Add Doctor</button>
            <a href="#" onclick="logout()">Logout</a>`;
    } else if (role === "doctor") {
        headerContent += `
            <button class="adminBtn" onclick="selectRole('doctor')">Home</button>
            <a href="#" onclick="logout()">Logout</a>`;
    } else if (role === "patient") {
        headerContent += `
            <button id="patientLogin" class="adminBtn">Login</button>
            <button id="patientSignup" class="adminBtn">Sign Up</button>`;
    } else if (role === "loggedPatient") {
        headerContent += `
            <button id="home" class="adminBtn" onclick="window.location.href='/pages/loggedPatientDashboard.html'">Home</button>
            <button id="patientAppointments" class="adminBtn" onclick="window.location.href='/pages/patientAppointments.html'">Appointments</button>
            <a href="#" onclick="logoutPatient()">Logout</a>`;
    } else {
        // Default header for non-authenticated users
        headerContent += `
            <button id="patientLogin" class="adminBtn">Login</button>
            <button id="patientSignup" class="adminBtn">Sign Up</button>`;
    }

    // Close the header section
    headerContent += `</nav></header>`;

    // Render the header content
    headerDiv.innerHTML = headerContent;

    // Attach event listeners to header buttons
    attachHeaderButtonListeners();
}

/**
 * Attaches event listeners to header buttons
 */
function attachHeaderButtonListeners() {
    // Patient login button
    const patientLoginBtn = document.getElementById("patientLogin");
    if (patientLoginBtn) {
        patientLoginBtn.addEventListener("click", () => {
            openModal('patientLogin');
        });
    }

    // Patient signup button
    const patientSignupBtn = document.getElementById("patientSignup");
    if (patientSignupBtn) {
        patientSignupBtn.addEventListener("click", () => {
            openModal('patientSignup');
        });
    }

    // Add doctor button (for admin)
    const addDocBtn = document.getElementById("addDocBtn");
    if (addDocBtn) {
        addDocBtn.addEventListener("click", () => {
            openModal('addDoctor');
        });
    }
}

/**
 * Logs out admin and doctor users
 */
function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("userRole");
    window.location.href = "/";
}

/**
 * Logs out patient users
 */
function logoutPatient() {
    localStorage.removeItem("token");
    localStorage.setItem("userRole", "patient");
    window.location.href = "/pages/patientDashboard.html";
}

// Render the header when the page loads
document.addEventListener("DOMContentLoaded", renderHeader);