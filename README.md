# Edmonton Real Estate Calculator

This project is a tool designed to help Edmonton homeowners assess the potential return on investment of adding a garden suite to their property.  It leverages publicly available data from the City of Edmonton to provide a comparative analysis of existing garden suites and offers a personalized calculation based on user input.

## Features

1.  **Statistical Breakdown of Existing Garden Suites:**  This feature provides a comprehensive statistical overview of all existing garden suites in Edmonton. The data, sourced from the City of Edmonton's open datasets, includes construction costs, square footage, property value, number of rental units, and an overall grade out of 10.

2.  **Personalized ROI Calculator:**  Users can input their own projected figures for construction costs, square footage, property value, and number of rental units to receive a personalized grade out of 10. This allows for direct comparison with existing garden suites in Edmonton.

3.  **Interactive Map:** An interactive map of Edmonton displays all existing garden suites, color-coded based on their calculated grade. This allows for visual analysis of garden suite performance across the city.

## Grading Methodology

The core of the project is the calculation of a grade for each garden suite, providing a single metric for comparison. The grading system considers several factors, each with a specific weight:

*   **Construction Efficiency Score (30%):**  Penalizes high construction costs relative to floor area.
    ```
    score = 1 - (construction value / floor area)
    ```
*   **Property Value Efficiency Score (30%):** Rewards properties that add significant floor area relative to property value.
    ```
    score = floor area / property value
    ```
*   **Floor Area Score (25%):**  Normalizes the floor area based on the minimum and maximum floor areas in the dataset.
    ```
    score = (floor area - min floor area) / (max floor area - min floor area)
    ```
*   **Units Added Score (15%):**  Reflects the number of rental units added (1 unit = 0.5, 2 units = 1.0).

The individual scores are then weighted, summed, and normalized across the entire dataset to ensure comparability and improve data quality.

## How to Use

his section provides instructions for running the Edmonton Real Estate Calculator application.  The application is built using Java and JavaFX.

**1. Prerequisites:**

*   **Java Development Kit (JDK):** Ensure you have a compatible JDK installed on your system.  Java 11 or later is recommended. You can download a JDK from [https://adoptium.net/](https://adoptium.net/) (formerly AdoptOpenJDK) or [https://www.oracle.com/java/technologies/downloads/](https://www.oracle.com/java/technologies/downloads/).  Verify your Java installation by opening a terminal or command prompt and running `java -version`.
*   **JavaFX:** This project uses JavaFX for its user interface. Modern JDK distributions include JavaFX. If you are using an older JDK or a separate JavaFX installation, ensure it is correctly configured in your project.
*   **IntelliJ IDEA (Recommended):** While not strictly required, IntelliJ IDEA is the recommended IDE for this project due to its excellent Java and JavaFX support. You can download the Community Edition (free) from [https://www.jetbrains.com/idea/download/](https://www.jetbrains.com/idea/download/).
*   **Other IDEs (Eclipse, NetBeans, etc.):**  While IntelliJ IDEA is recommended, you can use other Java IDEs.  Ensure that your IDE has proper JavaFX support and that you configure the project correctly.

**2. Running the Application (IntelliJ IDEA):**

1.  **Import Project:** Open IntelliJ IDEA and select "Open or Import Project." Navigate to the project directory and select it.
2.  **Resolve Dependencies:** IntelliJ IDEA should automatically resolve project dependencies (if any). If not, you may need to configure the project's build path or dependencies manually.
3.  **Run Configuration:** Locate the `PropertyAssessmentJavaFX.java` file (this is assumed to be your main application class). Right-click on the file and select "Run 'PropertyAssessmentJavaFX'."  This will create a run configuration for your application.
4.  **Run:** Click the green "Run" button in the top right corner of IntelliJ IDEA to compile and run the application.

**3. Running the Application (Other IDEs):**

The steps for other IDEs will vary slightly.  Generally, you will need to:

1.  **Create a New Project:** Create a new Java project and import the project source code.
2.  **Configure Build Path:** Add the required JavaFX libraries to your project's build path.
3.  **Set Main Class:** Set the `PropertyAssessmentJavaFX.java` file as your main application class.
4.  **Create Run Configuration:** Create a new run configuration and specify the main class.
5.  **Run:** Run the application using the IDE's run command.

**4. Using the Application:**

1.  **Statistical Breakdown:** Upon launch, the application should display a statistical breakdown of existing garden suites.  This may be presented in a table or other visual format.
2.  **Personalized Calculator:**  Locate the section for the personalized calculator.  Enter your projected values for:
    *   Construction Costs
    *   Square Footage
    *   Property Value
    *   Number of Rental Units
3.  **Calculate Grade:** Click the "Calculate" button (or similar) to generate your personalized grade.
4.  **Interactive Map:** The interactive map should display all existing garden suites.  Use the map controls to navigate and explore the different locations.  The color-coding of the markers will indicate the grade of each suite.  Clicking on a marker may provide more detailed information about the suite.
5.  **Interpreting Results:** The calculated grade provides a comparative measure of your projected garden suite against existing ones in Edmonton. A higher grade generally indicates a potentially better return on investment.  However, remember that this is a statistical model and real-world results may vary.  Consider consulting with real estate professionals for further advice.

**5. Troubleshooting:**

*   **Compilation Errors:** Check your JDK and JavaFX installation and ensure they are correctly configured in your project.
*   **Runtime Errors:** Carefully review any error messages displayed by the application.  Double-check your input values to ensure they are valid.
*   **Map Issues:** Ensure you have a stable internet connection for the interactive map to load correctly.

## Data Sources

*   City of Edmonton Open Data Portal: https://data.edmonton.ca/Urban-Planning-Economy/General-Building-Permits-Garage-and-Garden-Suites-/nsi9-jdb5
