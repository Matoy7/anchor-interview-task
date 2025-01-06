 
# Spreadsheet Manager

This project provides an API for managing spreadsheets, built using **Java** and **Spring Boot**. The API allows users to create, update, and retrieve spreadsheet data, as well as handle various spreadsheet operations like adding cell data. It also support Lookup option for a cell to referenace another cell's value.

## Features

- Create and manage spreadsheets
- Add and update cells in a spreadsheet
- Get spreadsheet data by ID
- Lookup option for a cell to referenace another cell's value.
- support api concurrency
- support error handling (enforce cycle refernce and type verification of columns)

## Prerequisites

Before running the project, make sure you have the following installed:

- **Java 11 or newer**
- **Maven** for building the project
- **Spring Boot** (this project uses Spring Boot to manage dependencies)

## Getting Started

Follow these steps to get a local copy of the project running:

### 1. Clone the repository

```bash
git clone https://github.com/Matoy7/anchor-interview-task.git
cd anchor-interview-task
```

### 2. Set up the environment

Make sure to have the port 8080 free for use since the application is using it. 


### 3. Build the project

To build the project using Maven, run:

```bash
mvn clean install
```

This will compile the code and package the application into a `.jar` file.

### 4. Run the application

To run the Spring Boot application, use the following command:

```bash
mvn spring-boot:run
```

Or, if you prefer to run the packaged `.jar` file:

```bash
java -jar anchor-interview-task-1.0-SNAPSHOT.jar
```

The application will start on `http://localhost:8080` by default.

## API Endpoints

### 1. Create a new spreadsheet

**Endpoint:** `POST /api/spreadsheets`

**Request body:**

```json
{
  "name": "Monthly Budget",
  "rows": 10,
  "columns": 5
}
```

**Response:**

```json
{
  "id": 1,
  "name": "Monthly Budget",
  "rows": 10,
  "columns": 5,
  "createdAt": "2025-01-01T00:00:00"
}
```

### 2. Get spreadsheet by ID

**Endpoint:** `GET /api/spreadsheets/{id}`

**Response:**

```json
{
  "id": 1,
  "name": "Monthly Budget",
  "rows": 10,
  "columns": 5,
  "data": [
    ["Date", "Amount", "Category", "Description", "Status"],
    ["01/01/2025", "$500", "Rent", "January Rent", "Paid"],
    ["01/02/2025", "$200", "Utilities", "Electric Bill", "Pending"]
  ]
}
```

### 3. Add row to spreadsheet

**Endpoint:** `POST /api/spreadsheets/{id}/rows`

**Request body:**

```json
{
  "values": ["01/03/2025", "$300", "Food", "Grocery Shopping", "Paid"]
}
```

**Response:**

```json
{
  "id": 1,
  "name": "Monthly Budget",
  "rows": 11,
  "columns": 5,
  "data": [
    ["Date", "Amount", "Category", "Description", "Status"],
    ["01/01/2025", "$500", "Rent", "January Rent", "Paid"],
    ["01/02/2025", "$200", "Utilities", "Electric Bill", "Pending"],
    ["01/03/2025", "$300", "Food", "Grocery Shopping", "Paid"]
  ]
}
```

### 4. Update cell in spreadsheet

**Endpoint:** `PUT /api/spreadsheets/{id}/cells`

**Request body:**

```json
{
  "row": 2,
  "column": 3,
  "value": "Utilities - Paid"
}
```

**Response:**

```json
{
  "message": "Cell updated successfully"
}
```

## Technologies Used

- **Java 11**
- **Spring Boot** (for the web framework)
- **Maven** (for project dependency management)

## Contributing

If you'd like to contribute, please follow these steps:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-name`).
3. Make your changes.
4. Commit your changes (`git commit -am 'Add new feature'`).
5. Push to the branch (`git push origin feature-name`).
6. Open a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgements

- Thanks to [Spring Boot](https://spring.io/projects/spring-boot) for the amazing framework.

```
