
# BattleShip TDD 🚢

Project developed for the **Software Testing and Quality** course (Test i Qualitat del Software - 2025-2026). Implementation of the classic "Battleship" game using strict **TDD (Test Driven Development)** methodology and a decoupled architecture.

## 👥 Authors

* **Levon Kesoyan Galstyan** (NIU: 1668018)
* **Luis Martinez Zamora** (NIU: 1668180)

## 📋 Project Description

This project is a Java console application that simulates a Battleship game against the computer. The main focus has been on test-driven development, ensuring high code coverage and robustness through the use of **JUnit 5** and  **Mockito** .

### Key Features

* **PvE Match:** Player vs. Computer with an intelligent random AI (avoids shooting at already attacked cells).
* **Standard Board:** 10x10 grid with boundary validation.
* **Full Fleet:** 5 types of ships (Carrier, Battleship, Cruiser, Submarine, Destroyer).
* **Console Interface (CLI):** Clear visualization of the game state and shots.

## 🏗️ Architecture and Design

The project follows the **MVC (Model-View-Controller)** architectural pattern to ensure separation of concerns and facilitate isolated unit testing.

* **Model (`es.uab.tqs.battleship.model`):** Contains pure business logic (`Board`, `Ship`, `Coordinate`, `Game`). It has no dependencies on the view or external I/O libraries.
* **View (`es.uab.tqs.battleship.view`):** Passive interface responsible for Input/Output. Implemented in `ConsoleView`.
* **Controller (`es.uab.tqs.battleship.controller`):** Orchestrates the game flow. Uses **Dependency Injection** to receive the View and Model, facilitating the use of Mocks during tests.

## 🛠️ Tech Stack

* **Language:** Java 17 (OpenJDK)
* **Dependency Manager:** Maven
* **Testing:** JUnit 5 (Jupiter)
* **Mocking:** Mockito 5
* **Quality Analysis:** Jacoco (Coverage) & Checkstyle (Code Style)
* **CI/CD:** GitHub Actions

## 🧪 Testing Strategy

The project has been developed following the **Red-Green-Refactor** cycle. The following testing techniques have been applied, as detailed in the project report:

1. **Unit Testing:** Comprehensive coverage of domain logic (Model and Controller).
2. **Mocking:** Isolation of the controller (`GameControllerTest`) by simulating View and Model behavior using Mockito.
3. **Boundary Value Analysis:** Rigorous testing of boundary values in coordinates (e.g., 0, 9, 10, -1) to ensure grid integrity.
4. **Pairwise Testing:** Applied in `ShipTest` to validate meaningful combinations of positioning and orientation without exhaustive redundancy.
5. **Parameterized Tests:** Utilization of `@ParameterizedTest` and `@CsvSource` to implement Data-Driven Testing for coordinates and ship sinking logic.

## 🚀 Installation and Execution

### 1. Clone the Repository

Start by cloning the project to your local machine using Git:

```
git clone [https://github.com/luismz14/BattleShipTDD.git](https://github.com/luismz14/BattleShipTDD.git)
cd BattleShipTDD

```

### 2. Build the Project

Navigate to the source directory (where the `pom.xml` is located) and build the project using Maven. This command will compile the code and download all necessary dependencies.

```
cd battleship
mvn clean install

```

### 3. Run the Game

Once the build is successful, you can launch the application directly from the command line:

```
mvn exec:java -Dexec.mainClass="es.uab.tqs.battleship.Main"

```

## ✅ Running Tests and Quality Checks

To execute the full suite of unit tests:

```
mvn test

```

### Generating Coverage Report (Jacoco)

After running the tests, you can find the coverage report at:

target/site/jacoco/index.html

### Style Verification (Checkstyle)

The project adheres to strict style rules defined in `checkstyle.xml`. To verify compliance:

```
mvn checkstyle:check

```

## 🔄 Continuous Integration (CI)

The repository includes a **GitHub Actions** workflow (`.github/workflows/test.yml`) that triggers on every `push` or `pull_request` to the `main` branch. This pipeline handles:

1. Setting up the Java 17 environment.
2. Compiling the project.
3. Running all automatic tests to ensure no regressions.
