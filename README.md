# SafetyNet API

## Description
The SafetyNet API provides a RESTful interface to retrieve information related to public safety, fire stations, medical alerts, and personal details in a given city. The API is used to provide critical data for emergency services.

## Features
The API allows users to retrieve information about fire stations, people covered by each station, child alerts by address, emergency phone numbers, medical records for people, and community emails for a given city.

## Project Structure

- **`com.safetynet.config`**: Contains configuration files for the project, such as Swagger configuration.
- **`com.safetynet.controller`**: Contains the REST controllers that expose the API endpoints.
- **`com.safetynet.dto`**: Contains Data Transfer Objects (DTOs) used in the API responses.
- **`com.safetynet.exception`**: Contains custom exceptions like `ResourceNotFoundException`.
- **`com.safetynet.mapper`**: Uses MapStruct to perform entity-to-DTO and DTO-to-entity transformations.
- **`com.safetynet.model`**: Defines the data models (e.g., Person, Firestation, MedicalRecord).
- **`com.safetynet.repository`**: Manages data access (e.g., from a JSON file or database).
- **`com.safetynet.service`**: Contains the business logic and services to handle the requests.

## Tests

### Unit Tests
Unit tests verify the business logic of the services and exception handling. These tests are located in the following packages:
- **Controller Tests**: `com.safetynet.controller` (verifies the correctness of endpoint logic)
- **Repository Tests**: `com.safetynet.repository` (verifies data access logic)
- **Service Tests**: `com.safetynet.service` (verifies business logic)

### Integration Tests
- **Integration Tests**: `com.safetynet.inegration` (verifies end-to-end features)

---

## API

### Endpoints
1. **`/firestationCoverage`**
    - **Method**: `GET`
    - **Parameter**: `stationNumber` (station number)
    - **Response**: Number of adults and children covered by the specified station.

2. **`/childAlert`**
    - **Method**: `GET`
    - **Parameter**: `address` (address)
    - **Response**: List of children living at the specified address, including their information.

3. **`/phoneAlert`**
    - **Method**: `GET`
    - **Parameter**: `stationNumber` (station number)
    - **Response**: List of emergency phone numbers for people covered by the specified station.

4. **`/fire`**
    - **Method**: `GET`
    - **Parameter**: `address` (address)
    - **Response**: List of people and fire stations associated with the specified address.

5. **`/flood/stations`**
    - **Method**: `GET`
    - **Parameter**: `stationNumbers` (comma-separated list of station numbers)
    - **Response**: List of people covered by the specified stations, including medical records.

6. **`/personInfoLastName={lastName}`**
    - **Method**: `GET`
    - **Parameter**: `lastName` (last name)
    - **Response**: List of people with their corresponding medical records, filtered by last name.

7. **`/communityEmail`**
    - **Method**: `GET`
    - **Parameter**: `city` (city)
    - **Response**: List of emails for residents of the specified city.

### Additional Endpoints (Get, Add, Update & Delete)

#### `http://localhost:8080/person`

#### `http://localhost:8080/firestation`

#### `http://localhost:8080/medicalRecord`

## Versions
- JDK 21
- Spring Boot 3.4.1