 
# Spreadsheet Manager

This project provides an API for managing spreadsheets, built using **Java** and **Spring Boot**. The API allows users to create, update, and retrieve spreadsheet data. It also supports Lookup (a cell value which is a referenace to another cell's value).

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
```
via cmd go to the compiled traget folder (where the jar is) and run:
java -jar anchor-interview-task-1.0-SNAPSHOT.jar

Or, if you prefer to run it via some IDE (Intellij or other)
```

The application will start on `http://localhost:8080` by default.

## API Endpoints

### 1. Create a new spreadsheet

**Endpoint:** `POST http://localhost:8080/sheet/`

**Request body:**

```json
{
    "columns": [
        {
            "name": "A",
            "type": "boolean"
        },
        {
            "name": "B",
            "type": "int"
        },
        {
            "name": "C",
            "type": "double"
        },
        {
            "name": "D",
            "type": "string"
        }
    ]
}
```

**Response:**
```json
{
    "key": "id",
    "value": 2
}
```
### 2. Get spreadsheet by ID

**Endpoint:** `GET http://localhost:8080/sheet/{sheetId}`

**Response:**

```json
{
    "key": "sheet id:  1",
    "value": [
        {
            "rowNumber": 33,
            "content": 111,
            "colName": "B"
        },
        {
            "rowNumber": 11,
            "content": 333,
            "colName": "B"
        },
        {
            "rowNumber": 12,
            "content": 222,
            "colName": "B"
        }
    ]
}
```

### 3. Add Cell to spreadsheet with plain value

**Endpoint:** `POST http://localhost:8080/sheet/{sheetId}/col/{columnName}/row/{rowId}`

**Request body:**

```json
{
    "content": "Some content here"
}
```

**Response:**

200 OK
### 4. Add Cell to spreadsheet with lookup value

**Endpoint:** `POST http://localhost:8080/sheet/{sheetId}/col/{columnName}/row/{rowId}`

**Request body:**

```json
{
    "content": "lookup(B,33)"
}
```

**Response:**

200 OK
## Test coverage

- the project is covered by:
    - unit tests (which cover most of the code and run as part of the mvn lifecycle.
    - intergration tests (sprint mvc library) - test the controller end point.

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
