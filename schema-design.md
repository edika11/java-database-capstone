## MySQL Database Design
MySQL will store entities where structure and relational integrity matter:
- admins
- patients
- doctors
- specialities
- clinic_locations
- doctor_availabilities
- appointment_slots
- appointements
- payments



### Table: admins (system users that manage the platform)
- `id` INT PRIMARY KEY AUTO_INCREMENT
- `username` VARCHAR(100) UNIQUE NOT NULL
- `email` VARCHAR(255) UNIQUE NOT NULL
- `password_hash` VARCHAR(255) NOT NULL
- `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP


### Table: patients
- `id` INT PRIMARY KEY AUTO_INCREMENT
- `uuid` CHAR(36) UNIQUE NOT NULL
- `first_name` VARCHAR(100) NOT NULL
- `last_name` VARCHAR(100) NOT NULL
- `email` VARCHAR(255) UNIQUE NOT NULL
- `phone` VARCHAR(30) NULL
- `date_of_birth` DATE NULL
- `gender` ENUM('male','female','other') NULL
- `is_deleted` TINYINT(1) DEFAULT 0 NOT NULL
- `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
- `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

**Notes**
- `uuid` is used to expose public IDs and avoid leaking of auto-increment IDs.
- We use `is_deleted` for soft delete, to preserve history of appointments. We do **not** cascade-delete appointments on patient deletion and keep them for auditing.


### Table: doctors
- `id` INT PRIMARY KEY AUTO_INCREMENT
- `uuid` CHAR(36) UNIQUE NOT NULL
- `first_name` VARCHAR(100) NOT NULL
- `last_name` VARCHAR(100) NOT NULL
- `email` VARCHAR(255) UNIQUE NOT NULL
- `phone` VARCHAR(30) NULL
- `specialty_id` INT NULL
- `bio` TEXT NULL
- `is_deleted` TINYINT(1) DEFAULT 1 NOT NULL
- `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
- `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

**Notes**
- `uuid` is used to expose public IDs and avoid leaking of auto-increment IDs.
- `specialty_id` is a foreign key based on specialties(id).
- `is_deleted` for soft delete, to preserve history of appointments. We do **not** cascade-delete appointments on doctor deletion and keep them for auditing.


### Table: specialties
- `id` INT PRIMARY KEY AUTO_INCREMENT
- `code` VARCHAR(50) UNIQUE NOT NULL
- `name` VARCHAR(150) NOT NULL
- `description` TEXT NULL

**Notes**
- Specialties are normalized and selectable in doctor profiles.


### Table: clinic_locations
- `id` INT PRIMARY KEY AUTO_INCREMENT
- `name` VARCHAR(150) NOT NULL
- `address` VARCHAR(300) NULL
- `timezone` VARCHAR(50) NOT NULL DEFAULT 'UTC'
- `phone` VARCHAR(30) NULL

**Notes**
- `timezone` is useful when the clinic has multiple locations and appointment slots vary by location/timezone.


### Table: doctor_availabilities
- `id` INT PRIMARY KEY AUTO_INCREMENT
- `doctor_id` INT NOT NULL
- `location_id` INT NULL
- `start_time` DATETIME NOT NULL
- `end_time` DATETIME NOT NULL
- `recurrence` VARCHAR(255) NULL
- `is_exception` TINYINT(1) DEFAULT 0
- `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP

**Notes**
- `doctor_id` is a foreign key based on doctors(id).
- `location_id` is a foreign key based on clinic_locations(id)
- `recurrence` is a simple text ("WEEKLY Mon,Wed") who allows to express repeated schedules. Application logic expands recurrences into slots.
- `is_exception` is true for one-off blocks


### Table: appointment_slots
- `id` INT PRIMARY KEY AUTO_INCREMENT
- `doctor_id` INT NOT NULL
- `location_id` INT NULL
- `slot_start` DATETIME NOT NULL
- `slot_end` DATETIME NOT NULL
- `is_booked` TINYINT(1) DEFAULT 0
- `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
- INDEX (`doctor_id`, `slot_start`)

**Notes**
- INDEX (`doctor_id`, `slot_start`) helps to speed up searches.


### Table: appointments
- `id` INT PRIMARY KEY AUTO_INCREMENT
- `uuid` CHAR(36) UNIQUE NOT NULL
- `patient_id` INT NOT NULL
- `doctor_id` INT NOT NULL
- `location_id` INT NULL
- `slot_id` INT NULL
- `appointment_start` DATETIME NOT NULL
- `appointment_end` DATETIME NOT NULL
- `status` ENUM('scheduled','completed','cancelled') DEFAULT 'scheduled'
- `cancel_reason` TEXT NULL
- `is_deleted` TINYINT(1) DEFAULT 0
- `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
- `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

**Notes**
- `uuid` is used to expose public IDs and avoid leaking of auto-increment IDs.
- `patient_id` is a foreign key based on patients(id).
- `doctor_id` is a foreign key based on doctors(id).
- `location_id` is a foreign key based on clinic_locations(id)
- `slot_id` is a foreign key based on appointment_slots(id).
- We use `is_deleted` for soft delete. Appointments are keep for audit.


### Table: payments
- `id` INT PRIMARY KEY AUTO_INCREMENT
- `appointment_id` INT NULL
- `patient_id` INT NOT NULL
- `amount` DECIMAL(10,2) NOT NULL
- `currency` CHAR(3) DEFAULT 'USD'
- `method` ENUM('card','cash','insurance','other') DEFAULT 'card'
- `status` ENUM('pending','completed','failed','refunded') DEFAULT 'pending'
- `transaction_ref` VARCHAR(255) NULL
- `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP

**Notes**
- `appointment_id` is a foreign key based on appointments(id).
- `appointment_id` is NULL to allow more flexible payment
- `patient_id` is a foreign key based on patients(id).


## MongoDB Collection Design
We store the following collections:
- prescriptions
- messages
- feedback


### Collection prescriptions
```json
{
  "_id": "ObjectId('64def456789')",
  "appointment_id": 23,
  "patient_snapshot": {
    "id": 123,
    "first_name": "John",
    "last_name": "Smith",
    "dob": "1980-05-15",
    "gender": "male"
  },
  "doctor_snapshot": {
    "id": 77,
    "first_name": "Bob",
    "last_name": "Moreno",
    "specialty": "Cardiology"
  },
  "medications": [
    {
      "name": "Paracetamol",
      "dosage": "500 mg",
      "frequency": "1 tablet every 6 hours",
      "duration_days": 5,
      "notes": "After meals"
    }
  ],
  "instructions": "Drink plenty of water.",
  "refill_allowed": 2,
  "pharmacy": {
    "name": "Central Pharmacy",
    "phone": "(438) 555-1212",
    "address": "19 Pops St"
  },
  "signed_by": "Dr Bob Moreno",
  "signed_at": "2025-09-01T10:12:00Z",
  "created_at": "2025-09-01T10:10:00Z",
  "updated_at": "2025-09-01T10:12:00Z"
}

```
**Notes**
- `appointment_id`, `id` of patient and doctor keep relational integrity by referencing SQL IDs.
- We use `patient_snapshot` and `doctor_snapshot` to preserve the context at time of issue


### Collection messages
```json
{
  "_id": "ObjectId('64def456789')",
  "participants": [
    {
      "id": 123,
      "type": "patient",
      "first_name": "John",
      "last_name": "Smith"
    },
    {
      "id": 77,
      "type": "doctor",
      "first_name": "Bob",
      "last_name": "Moreno"
    }
  ],
  "messages": [
    {
      "sender_id": 123,
      "sender_type": "patient",
      "body": "Hi, I have chest pain...",
      "sent_at": "2025-09-10T08:12:00Z"
    }
  ],
  "created_at": "2025-09-10T08:12:00Z"
}

```
**Notes**
- `id` for participants keep relational integrity by referencing SQL IDs.


### Collection feedback
```json
{
  "_id": "ObjectId('64def456789')",
  "appointment_id": 51,
  "patient_snapshot": {
    "id": 125,
    "first_name": "Alice",
    "last_name": "Brown"
  },
  "doctor_snapshot": {
    "id": 12,
    "first_name": "Mark",
    "last_name": "Lopez"
  },
  "rating": 4,
  "comments": "The doctor was very professional and explained everything clearly.",
  "tags": ["professional", "clear communication"],
  "submitted_at": "2025-08-29T12:00:00Z"
}

```
**Notes**
- We use `patient_snapshot` and `doctor_snapshot` to preserve the context at time of issue.
- `appointment_id`, `id` of patient and doctor keep relational integrity by referencing SQL IDs.
- `rating` is an integer 1–5 scale.