**Architecture summary**

This Spring Boot application follows a layered architecture combining both MVC and REST approaches. The AdminDashboard and DoctorDashboard interfaces use Thymeleaf controllers to render views, while other modules like Appointments, PatientDashboard, and PatientRecord communicate via REST controllers through a JSON API. Regardless of whether a request originates from a Thymeleaf or REST controller, it is routed through a central Service Layer, which contains the core business logic of the application.

The service layer interacts with two different persistence mechanisms. For relational data such as patients, doctors, appointments, and admin users, it leverages MySQL Repositories backed by JPA entities. These repositories access the MySQL database, with each entity mapped to its corresponding model (Patient, Doctor, Appointment, Admin). For non-relational data, specifically prescriptions, the application uses a MongoDB Repository connected to a MongoDB database, with data modeled as documents. This hybrid approach allows the system to efficiently handle structured relational data alongside flexible document-based data storage.



-------------------------------------------------------------------

**Numbered flow of data and control**

1. A user accesses either a dashboard (AdminDashboard or DoctorDashboard) through the UI or a REST module (Appointments, PatientDashboard, PatientRecord) via a JSON API.
2. The request is routed to the appropriate controller: Thymeleaf Controllers handle dashboard pages, while REST Controllers handle API calls.
3. The chosen controller delegates the request to the Service Layer, which contains the business logic of the application.
4. The Service Layer interacts with the persistence layer by using either MySQL Repositories or MongoDB Repositories, depending on the type of data being requested or modified.
5. MySQL Repositories access the MySQL Database, retrieving or updating data tied to relational entities such as patients, doctors, appointments, and admins.
6. Retrieved relational data is mapped into MySQL Models (JPA Entities), which represent objects like Patient, Doctor, Appointment, and Admin in the application.
7. For prescription-related operations, the Service Layer uses the MongoDB Repository, which queries the MongoDB Database and maps results into MongoDB Models (Documents), such as Prescription.
