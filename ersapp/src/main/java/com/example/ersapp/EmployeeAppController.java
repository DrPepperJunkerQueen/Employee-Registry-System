package com.example.ersapp;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.text.Text;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.Callback;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.text.DecimalFormat;
import jakarta.persistence.*;
import javafx.util.converter.LocalDateStringConverter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class EmployeeAppController implements Initializable
{
    private ClassContainer appClassContainer;

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("EmployeePU");
    EntityManager em = emf.createEntityManager();

    // Metoda do wyświetlania komunikatów o błędach
    private void showErrorMessage(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Metoda do wyświetlania komunikatów o sukcesie
    private void showSuccessMessage(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private TableView<Employee> employeeTable;

    @FXML
    private TableColumn<Employee, Integer> idColumn;

    @FXML
    private TableColumn<Employee, String> firstNameColumn;

    @FXML
    private TableColumn<Employee, String> lastNameColumn;

    @FXML
    private TableColumn<Employee, EmployeeCondition> conditionColumn;

    @FXML
    private TableColumn<Employee, Integer> birthYearColumn;

    @FXML
    private TableColumn<Employee, Double> salaryColumn;

    @FXML
    private TableColumn<Employee, Void> removeColumn;

    private List<Employee> employees = em.createQuery("FROM Employee", Employee.class).getResultList();

    @FXML
    private MenuButton showEmployeeGroup;

    @FXML
    private Text fillPercentage;

    @FXML
    private TextField searchEmployeeByLastNameTextField;

    @FXML
    private Button sortByNameButton;

    @FXML
    private Button sortByEmployeeNumberButton;

    @FXML
    private Button sortByFillPercentageButton;

    @FXML
    private Text averageGradeText;

    @FXML
    private TableView<Rate> rateTable;

    @FXML
    private TableColumn<Rate, String> groupNameColumn;

    @FXML
    private TableColumn<Rate, Integer> rateIDColumn;

    @FXML
    private TableColumn<Rate, Integer> gradeColumn;

    @FXML
    private TableColumn<Rate, LocalDate> dateColumn;

    @FXML
    private TableColumn<Rate, String> commentColumn;

    @FXML
    private TableColumn<Rate, Void> removeRateColumn;

    @FXML
    private Button addRateButton;

    @FXML
    private TableView<GroupRatingStats> statsTable;

    @FXML
    private TableColumn<GroupRatingStats, String> groupNameColumnStats;

    @FXML
    private TableColumn<GroupRatingStats, Long> countColumn;

    @FXML
    private TableColumn<GroupRatingStats, Double> averageColumn;

    @FXML
    private Button sendToSpringBootButton;

    // Lista wszystkich grup pracowników
    private List<ClassEmployee> groups = em.createQuery("FROM ClassEmployee", ClassEmployee.class).getResultList();

    public int findMaxID(ObservableList<ClassEmployee> groups) {
        int maxID = 0;

        // Safety check for null groups
        if (groups == null) {
            return maxID;
        }

        for (ClassEmployee group : groups) {
            if (group != null) {
                int groupMaxID = group.getMaxID();
                if (groupMaxID > maxID) {
                    maxID = groupMaxID;
                }
            }
        }

        return maxID;
    }

    public int findMaxRateID(List<ClassEmployee> groups) {
        int maxID = 0;

        // Safety check for null groups
        if (groups == null) {
            return maxID;
        }

        for (ClassEmployee group : groups) {
            if (group != null) {
                int groupMaxRateID = group.getMaxRateID();
                if (groupMaxRateID > maxID) {
                    maxID = groupMaxRateID;
                }
            }
        }

        return maxID;
    }

    // Referencja do aktualnie wyświetlanej grupy pracowników
    private ClassEmployee currentEmployeeGroup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Powiązanie kolumn z właściwościami klasy Employee
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        conditionColumn.setCellValueFactory(new PropertyValueFactory<>("condition"));
        birthYearColumn.setCellValueFactory(new PropertyValueFactory<>("birthYear"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));

        // Ustawienie edytowalnych komórek
        setupEditableColumnsEmployee();

        // Konfiguracja kolumny z przyciskiem usuwania
        setupRemoveColumn();

        // Dodawanie danych do tabeli
        employeeTable.setItems(FXCollections.observableArrayList(employees));

        // Przykładowe dane do testów
        loadSampleData();

        setupAddEmployeeGroupButton();

        // Skonfiguruj menu grup
        setupEmployeeGroupMenu();

        // Domyślnie pokaż wszystkich pracowników
        showAllEmployees();

        setupAddEmployeeButton();

        setupRemoveEmployeeGroupButton();

        setupFillPercentage();

        setupSearchEmployeeByLastNameTextField();

        setUpSortByNameButton();

        setUpSortByEmployeeNumberButton();

        setUpSortByFillPercentageButton();

        // Fix any issues with rate associations
        fixRateAssociations();

        // Add this line to your initialize method to correctly set up the average grade text at startup
        setupAverageGradeText();

        // Set up the rate table to recalculate average grade when selection changes
        rateTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            setupAverageGradeText();
        });

        // Możesz teraz np. pobrać pracowników z bazy:
        List<Employee> employees = em
                .createQuery("SELECT e FROM Employee e", Employee.class)
                .getResultList();

        // I dodać ich do GUI
        this.employees.addAll(employees);

        setupExportToCSVButton();

        // Powiązanie kolumn z właściwościami klasy Rate
        groupNameColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(
                        cellData.getValue().getGroup() != null
                                ? cellData.getValue().getGroup().getEmployeeGroupName()
                                : "No group found"
                )
        );
        rateIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));

        setupEditableColumnsRate();

        showAllRates();

        setupAverageGradeText();

        setupAddRateButton();

        setupRemoveRateColumn();

        groupNameColumnStats.setCellValueFactory(new PropertyValueFactory<>("groupName"));
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
        averageColumn.setCellValueFactory(new PropertyValueFactory<>("average"));

        ObservableList<GroupRatingStats> data = FXCollections.observableArrayList(getRatingStatsByGroup());
        statsTable.setItems(data);

        setupSendToSpringBootButton();
    }

    public void shutdown() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    private void setupRemoveColumn() {
        Callback<TableColumn<Employee, Void>, TableCell<Employee, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Employee, Void> call(final TableColumn<Employee, Void> param) {
                final TableCell<Employee, Void> cell = new TableCell<>() {
                    private final Button removeButton = new Button("Remove");

                    {
                        removeButton.setOnAction(event -> {
                            Employee employee = getTableView().getItems().get(getIndex());

                            if (currentEmployeeGroup != null) {
                                // Usuwanie z konkretnej grupy
                                boolean removed = currentEmployeeGroup.removeEmployee(employee);

                                em.getTransaction().begin();
                                Employee toRemove = em.find(Employee.class, employee.getId());
                                if (toRemove != null) em.remove(toRemove);
                                em.getTransaction().commit();


                                if (removed) {
                                    showEmployeeGroup(currentEmployeeGroup);
                                    showSuccessMessage("Employee removed successfully.");
                                } else {
                                    showErrorMessage("Failed to remove employee.");
                                }
                            } else {
                                // Usuwanie z widoku wszystkich pracowników - znajdź grupę, która zawiera tego pracownika
                                boolean removed = false;
                                for (ClassEmployee group : groups) {
                                    if (group.removeEmployee(employee)) {
                                        em.getTransaction().begin();
                                        Employee toRemove = em.find(Employee.class, employee.getId());
                                        if (toRemove != null) em.remove(toRemove);
                                        em.getTransaction().commit();

                                        removed = true;
                                        break;
                                    }
                                }

                                if (removed) {
                                    showAllEmployees();
                                    showSuccessMessage("Employee removed successfully.");
                                } else {
                                    showErrorMessage("Failed to remove employee.");
                                }
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(removeButton);
                        }
                    }
                };
                return cell;
            }
        };

        removeColumn.setCellFactory(cellFactory);
    }

    private void setupEditableColumnsEmployee() {
        // Edytor dla kolumny ID (liczby całkowite)
        idColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        idColumn.setOnEditCommit(event -> {
            Employee employee = event.getRowValue();
            employee.setId(event.getNewValue());
        });

        // Edytor dla kolumny oceny (liczby całkowite)
        firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        firstNameColumn.setOnEditCommit(event -> {
            Employee employee = event.getRowValue();
            employee.setFirstName(event.getNewValue());
        });

        // Edytor dla nazwiska (tekst)
        lastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameColumn.setOnEditCommit(event -> {
            Employee employee = event.getRowValue();
            employee.setLastName(event.getNewValue());
        });

        // Edytor dla statusu (enum)
        conditionColumn.setCellFactory(ComboBoxTableCell.forTableColumn(
                FXCollections.observableArrayList(EmployeeCondition.values())
        ));
        conditionColumn.setOnEditCommit(event -> {
            Employee employee = event.getRowValue();
            employee.setCondition(event.getNewValue());
        });

        // Edytor dla roku urodzenia (liczby całkowite)
        birthYearColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        birthYearColumn.setOnEditCommit(event -> {
            Employee employee = event.getRowValue();
            employee.setBirthYear(event.getNewValue());
        });

        // Edytor dla pensji (liczby zmiennoprzecinkowe)
        salaryColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        salaryColumn.setOnEditCommit(event -> {
            Employee employee = event.getRowValue();
            employee.setSalary(event.getNewValue());
        });
    }

    private void setupRemoveRateColumn() {
        Callback<TableColumn<Rate, Void>, TableCell<Rate, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Rate, Void> call(final TableColumn<Rate, Void> param) {
                final TableCell<Rate, Void> cell = new TableCell<>() {
                    private final Button removeButton = new Button("Remove");

                    {
                        removeButton.setOnAction(event -> {
                            Rate rate = getTableView().getItems().get(getIndex());

                            if (currentEmployeeGroup != null) {
                                // Usuwanie z konkretnej grupy
                                boolean removed = currentEmployeeGroup.removeRate(rate);

                                em.getTransaction().begin();
                                Rate toRemove = em.find(Rate.class, rate.getId());
                                if (toRemove != null) em.remove(toRemove);
                                em.getTransaction().commit();


                                if (removed) {
                                    showEmployeeGroup(currentEmployeeGroup);
                                    showSuccessMessage("Rate removed successfully.");
                                } else {
                                    showErrorMessage("Failed to remove rate.");
                                }
                            } else {
                                // Usuwanie z widoku wszystkich pracowników - znajdź grupę, która zawiera tego pracownika
                                boolean removed = false;
                                for (ClassEmployee group : groups) {
                                    if (group.removeRate(rate)) {
                                        em.getTransaction().begin();
                                        Rate toRemove = em.find(Rate.class, rate.getId());
                                        if (toRemove != null) em.remove(toRemove);
                                        em.getTransaction().commit();

                                        removed = true;
                                        break;
                                    }
                                }

                                if (removed) {
                                    showAllEmployees();
                                    showSuccessMessage("Rate removed successfully.");
                                } else {
                                    showErrorMessage("Failed to remove rate.");
                                }
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(removeButton);
                        }
                    }
                };
                return cell;
            }
        };

        removeRateColumn.setCellFactory(cellFactory);
    }

    private void setupEditableColumnsRate() {
        // Edytor dla kolumny nazwy grupy (rozwijane menu)
        groupNameColumn.setCellFactory(ComboBoxTableCell.forTableColumn(
                FXCollections.observableArrayList(
                        groups.stream()
                                .map(ClassEmployee::getEmployeeGroupName)
                                .toList()
                )
        ));

        groupNameColumn.setOnEditCommit(event -> {
            Rate rate = event.getRowValue();
            String newGroupName = event.getNewValue();

            // Znajdź obiekt ClassEmployee na podstawie nazwy
            ClassEmployee selectedGroup = groups.stream()
                    .filter(group -> group.getEmployeeGroupName().equals(newGroupName))
                    .findFirst()
                    .orElse(null);

            if (selectedGroup != null) {
                em.getTransaction().begin();
                rate.setGroup(selectedGroup);
                em.merge(rate); // zaktualizuj w bazie
                em.getTransaction().commit();
                showSuccessMessage("Group assignment updated.");
            } else {
                showErrorMessage("Group not found: " + newGroupName);
            }
        });

        // Edytor dla kolumny ID (liczby całkowite)
        rateIDColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        rateIDColumn.setOnEditCommit(event -> {
            Rate rate = event.getRowValue();
            rate.setId(event.getNewValue());
        });

        // Edytor dla kolumny oceny (liczby całkowite)
        gradeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(
                new IntegerStringConverter(),
                FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6)
        ));
        gradeColumn.setOnEditCommit(event -> {
            Rate rate = event.getRowValue();
            rate.setGrade(event.getNewValue());

            em.getTransaction().begin();
            em.merge(rate); // aktualizacja w bazie
            em.getTransaction().commit();
        });

        // Edytor dla kolumny daty (data lokalna)
        dateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        dateColumn.setOnEditCommit(event -> {
            Rate rate = event.getRowValue();
            rate.setDate(event.getNewValue());
        });

        // Edytor dla komentarza (tekst)
        commentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        commentColumn.setOnEditCommit(event -> {
            Rate rate = event.getRowValue();
            rate.setComment(event.getNewValue());
        });
    }

    @FXML
    private TextField addEmployeeGroupNameTextField;

    @FXML
    private TextField addEmployeeGroupCapacityTextField;

    @FXML
    private Button addEmployeeGroupButton;

    @FXML
    private Button removeEmployeeGroupButton;

    @FXML
    private Button addEmployeeButton;

    @FXML Button exportToCSVButton;

    // Obsługa przycisku dodawania grupy
    private void setupAddEmployeeGroupButton() {
        addEmployeeGroupButton.setOnAction(event -> {
            String groupName = addEmployeeGroupNameTextField.getText().trim();
            String maxEmployeesText = addEmployeeGroupCapacityTextField.getText().trim();

            if (!groupName.isEmpty() && !maxEmployeesText.isEmpty()) {
                try {
                    int maxEmployees = Integer.parseInt(maxEmployeesText);
                    // Dodaj nową grupę
                    List<Employee> employeeList = new ArrayList<>(maxEmployees);
                    addEmployeeGroup(groupName, maxEmployees, employeeList);

                    // Wyczyść pole tekstowe po dodaniu
                    addEmployeeGroupNameTextField.clear();
                } catch (NumberFormatException e) {
                    showErrorMessage("The max number of Employees is incorrect.");
                }
            }
            else
            {
                showErrorMessage("Enter information about the group first.");
            }
        });
    }

    private void setupAddEmployeeButton() {
        addEmployeeButton.setOnAction(event -> {
            if (currentEmployeeGroup != null) {
                // Create a new Employee with null ID (let Hibernate generate it)
                Employee newEmployee = new Employee();
                newEmployee.setFirstName("New");
                newEmployee.setLastName("Employee");
                newEmployee.setCondition(EmployeeCondition.PRESENT);
                newEmployee.setBirthYear(0);
                newEmployee.setSalary(0.0);

                // First persist to database to get ID assigned
                em.getTransaction().begin();
                em.persist(newEmployee);
                em.getTransaction().commit();

                // Now add to the group
                currentEmployeeGroup.addEmployee(newEmployee);
                showEmployeeGroup(currentEmployeeGroup);

                showSuccessMessage("New employee added successfully.");
            } else {
                showErrorMessage("Please select a group first to add an employee.");
            }
        });
    }

    private void setupAddRateButton() {
        addRateButton.setOnAction(event -> {
            if (currentEmployeeGroup != null) {
                // Create a new Rate WITHOUT setting the ID (let Hibernate generate it)
                Rate newRate = new Rate();
                newRate.setGroup(currentEmployeeGroup);
                // Remove this line: newRate.setId(currentEmployeeGroup.getMaxRateID());
                newRate.setGrade(0);
                newRate.setDate(LocalDate.now());
                newRate.setComment("");

                // First persist to database to get ID assigned
                em.getTransaction().begin();
                try {
                    em.persist(newRate);
                    em.getTransaction().commit();

                    // Now add to the group
                    currentEmployeeGroup.addRate(newRate);
                    showGroupRates(currentEmployeeGroup);

                    showSuccessMessage("New rate added successfully.");
                } catch (Exception e) {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    showErrorMessage("Error adding rate: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                showErrorMessage("Please select a group first to add a rate.");
            }
        });
    }

    private void setupRemoveEmployeeGroupButton()
    {
        removeEmployeeGroupButton.setOnAction(event -> {
            if(currentEmployeeGroup != null)
            {
                groups.remove(currentEmployeeGroup);
                currentEmployeeGroup = null;
                setupEmployeeGroupMenu();
                showAllEmployees();
            }
        });
    }

    private void setupEmployeeGroupMenu() {
        // Wyczyść istniejące elementy menu
        showEmployeeGroup.getItems().clear();

        // Dodaj opcję "Wszystkie grupy"
        MenuItem allGroupsItem = new MenuItem("All Groups");
        allGroupsItem.setOnAction(event -> {
            showAllEmployees();
            showEmployeeGroup.setText("All Groups");
            setupFillPercentage();
            setupAverageGradeText();
        });
        showEmployeeGroup.getItems().add(allGroupsItem);

        // Dodaj po jednym elemencie menu dla każdej grupy
        for (ClassEmployee group : groups) {
            MenuItem groupItem = new MenuItem(group.getEmployeeGroupName());
            groupItem.setOnAction(event -> {
                showEmployeeGroup(group);
                showEmployeeGroup.setText(group.getEmployeeGroupName());
            });
            showEmployeeGroup.getItems().add(groupItem);
        }
    }

    private void setupFillPercentage() {
        if (fillPercentage == null) {
            // Pole nie zostało zainicjalizowane w FXML
            System.out.println("Warning: fillPercentage Text field not initialized");
            return;
        }

        if (currentEmployeeGroup == null) {
            fillPercentage.setText("Fill Percentage: N/A");
        } else {
            double percentage = currentEmployeeGroup.getFillPercentage();
            DecimalFormat df = new DecimalFormat("#.##");
            fillPercentage.setText("Fill Percentage: " + df.format(percentage) + "%");
        }
    }

    private void setupAverageGradeText() {
        if (averageGradeText == null) {
            System.out.println("Warning: Average Grade Text field not initialized");
            return;
        }

        try {
            // Format for display
            DecimalFormat df = new DecimalFormat("#.##");

            if (currentEmployeeGroup == null) {
                // Calculate average across all rates in the database
                TypedQuery<Double> query = em.createQuery(
                        "SELECT AVG(r.grade) FROM Rate r", Double.class);

                Double avgResult = query.getSingleResult();

                if (avgResult == null) {
                    System.out.println("No rates found - average is null");
                    averageGradeText.setText("Average Grade: N/A");
                } else {
                    double avg = avgResult;
                    System.out.println("Overall average grade from database: " + avg);
                    averageGradeText.setText("Average Grade: " + df.format(avg));
                }
            } else {
                // Calculate average for the current group only using direct SQL query
                TypedQuery<Double> query = em.createQuery(
                        "SELECT AVG(r.grade) FROM Rate r WHERE r.group.id = :groupId",
                        Double.class);
                query.setParameter("groupId", currentEmployeeGroup.getId());

                Double avgResult = query.getSingleResult();

                if (avgResult == null) {
                    System.out.println("No rates found for group " + currentEmployeeGroup.getEmployeeGroupName());
                    averageGradeText.setText("Average Grade: N/A");
                } else {
                    double avg = avgResult;
                    System.out.println("Average grade for group " + currentEmployeeGroup.getEmployeeGroupName() + ": " + avg);
                    averageGradeText.setText("Average Grade: " + df.format(avg));
                }
            }
        } catch (Exception e) {
            System.err.println("Error calculating average grade: " + e.getMessage());
            e.printStackTrace();
            averageGradeText.setText("Average Grade: Error");
        }
    }

    private void showAllEmployees() {
        // Kolekcja do przechowywania wszystkich pracowników
        ObservableList<Employee> allEmployees = FXCollections.observableArrayList();

        // Zbierz pracowników ze wszystkich grup
        for (ClassEmployee group : groups) {
            List<Employee> employees = group.getEmployeeList();
            if (employees != null) {
                for (Employee employee : employees) {
                    if (employee != null) {
                        allEmployees.add(employee);
                    }
                }
            }
        }

        // Ustaw wszystkich pracowników w tabeli
        employeeTable.setItems(allEmployees);
        employeeTable.refresh(); // Force refresh to update all cells correctly
        currentEmployeeGroup = null;

        // Clear search field to maintain consistency
        if (searchEmployeeByLastNameTextField != null) {
            searchEmployeeByLastNameTextField.clear();
        }

        setupAverageGradeText();

        showAllRates();
    }

    private void setUpSortByNameButton() {
        sortByNameButton.setOnAction(event -> {
            // Sortowanie grup według nazwy (alfabetycznie)
            groups.sort(Comparator.comparing(ClassEmployee::getEmployeeGroupName));

            // Odświeżenie menu po sortowaniu
            setupEmployeeGroupMenu();

            // Informacja o sukcesie
            showSuccessMessage("Groups sorted alphabetically by name.");
        });
    }

    private void setUpSortByEmployeeNumberButton() {
        sortByEmployeeNumberButton.setOnAction(event -> {
            // Sortowanie grup według liczby pracowników (malejąco)
            FXCollections.sort(FXCollections.observableArrayList(groups), (group1, group2) -> {
                Query query = em.createQuery("SELECT COUNT(e) FROM Employee e WHERE e.group.id = :groupId");
                query.setParameter("groupId", group1.getClass());
                Long count1 = (Long) query.getSingleResult();
                Long count2 = (Long) query.getSingleResult();
                // Sortuj malejąco, więc odwracamy porównanie
                return Long.compare(count2, count1);
            });

            // Odświeżenie menu po sortowaniu
            setupEmployeeGroupMenu();

            // Informacja o sukcesie
            showSuccessMessage("Groups sorted by number of employees (descending).");
        });
    }

    private void setUpSortByFillPercentageButton() {
        sortByFillPercentageButton.setOnAction(event -> {
            // Sortowanie grup według procentu wypełnienia (malejąco)
            FXCollections.sort(FXCollections.observableArrayList(groups), (group1, group2) -> {
                // Zapytanie o liczbę pracowników w grupie
                Query query1 = em.createQuery("SELECT COUNT(e) FROM Employee e WHERE e.group.id = :groupId");
                query1.setParameter("groupId", group1.getId());
                Long count1 = (Long) query1.getSingleResult();

                Query query2 = em.createQuery("SELECT COUNT(e) FROM Employee e WHERE e.group.id = :groupId");
                query2.setParameter("groupId", group2.getId());
                Long count2 = (Long) query2.getSingleResult();

                // Obliczanie procentu wypełnienia
                double fill1 = group1.getFillPercentage();
                double fill2 = group2.getFillPercentage();

                // Sortuj malejąco, więc odwracamy porównanie
                return Double.compare(fill2, fill1);
            });

            // Odświeżenie menu po sortowaniu
            setupEmployeeGroupMenu();

            // Informacja o sukcesie
            showSuccessMessage("Groups sorted by fill percentage (descending).");
        });
    }

    private void setupSearchEmployeeByLastNameTextField() {
        // Add listener for text changes
        searchEmployeeByLastNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            performSearch(newValue);
        });
    }

    private void setupExportToCSVButton() {
        exportToCSVButton.setOnAction(event -> {
            exportToCSV("exports/employees.csv");
        });
    }

    public void exportToCSV(String filename) {
        try {
            // Utwórz folder, jeśli nie istnieje
            File file = new File(filename);
            File parentDir = file.getParentFile();

            if (parentDir != null && !parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    showErrorMessage("Failed to create directory: " + parentDir.getAbsolutePath());
                    return;
                }
            }

            // Zapisz dane do pliku
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("Group;ID;First Name;Last Name;Condition;Birth Year;Salary\n");

                for (ClassEmployee group : groups) {
                    String groupName = group.getEmployeeGroupName();
                    List<Employee> employees = group.getEmployeeList();

                    for (Employee e : employees) {
                        if (e != null) {
                            writer.write(String.format("%s;%d;%s;%s;%s;%d;%.2f\n",
                                    groupName,
                                    e.getId(),
                                    e.getFirstName(),
                                    e.getLastName(),
                                    e.getCondition(),
                                    e.getBirthYear(),
                                    e.getSalary()));
                        }
                    }
                }
            }

            showSuccessMessage("Export completed successfully: " + filename);

        } catch (IOException e) {
            showErrorMessage("Failed to export CSV: " + e.getMessage());
        }
    }

    private void performSearch(String searchText) {
        // If search text is empty, show all employees from the current view
        if (searchText == null || searchText.trim().isEmpty()) {
            if (currentEmployeeGroup != null) {
                showEmployeeGroup(currentEmployeeGroup);
            } else {
                showAllEmployees();
            }
            return;
        }

        // Create a list for filtered employees
        ObservableList<Employee> filteredEmployees = FXCollections.observableArrayList();

        // If we're viewing a specific group, search only within that group
        if (currentEmployeeGroup != null) {
            List<Employee> employees = currentEmployeeGroup.getEmployeeList();
            if (employees != null) {
                for (Employee employee : employees) {
                    if (employee != null && employee.getLastName().toLowerCase().contains(searchText.toLowerCase())) {
                        filteredEmployees.add(employee);
                    }
                }
            }
        } else {
            // Search across all groups
            for (ClassEmployee group : groups) {
                List<Employee> employees = group.getEmployeeList();
                if (employees != null) {
                    for (Employee employee : employees) {
                        if (employee != null && employee.getLastName().toLowerCase().contains(searchText.toLowerCase())) {
                            filteredEmployees.add(employee);
                        }
                    }
                }
            }
        }

        // Update the table with filtered results
        employeeTable.setItems(filteredEmployees);

        // Force table refresh to ensure all cells are properly updated
        employeeTable.refresh();
    }

    private void showEmployeeGroup(ClassEmployee group) {
        // Przefiltruj pracowników tylko dla wybranej grupy
        ObservableList<Employee> groupEmployees = FXCollections.observableArrayList();

        List<Employee> employees = group.getEmployeeList();
        if (employees != null) {
            for (Employee employee : employees) {
                if (employee != null) {
                    groupEmployees.add(employee);
                }
            }
        }

        // Ustaw pracowników wybranej grupy w tabeli
        employeeTable.setItems(groupEmployees);
        employeeTable.refresh(); // Force refresh to update all cells correctly
        currentEmployeeGroup = group;

        setupFillPercentage();

        // Clear search field to maintain consistency
        if (searchEmployeeByLastNameTextField != null) {
            searchEmployeeByLastNameTextField.clear();
        }

        showGroupRates(group);
        setupAverageGradeText();

        // Pokaż oceny dla wybranej grupy w tabeli statystyk
        if (group != null) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
            Root<Rate> rateRoot = cq.from(Rate.class);

            cq.multiselect(
                            rateRoot.get("group").get("employeeGroupName"),
                            cb.avg(rateRoot.get("grade")),
                            cb.count(rateRoot)
                    ).where(cb.equal(rateRoot.get("group"), group))
                    .groupBy(rateRoot.get("group").get("employeeGroupName"));

            List<Object[]> results = em.createQuery(cq).getResultList();

            List<GroupRatingStats> stats = new ArrayList<>();
            for (Object[] row : results) {
                String groupName = (String) row[0];
                Double avg = (Double) row[1];
                Long count = (Long) row[2];
                stats.add(new GroupRatingStats(groupName, count, avg));
            }

            statsTable.setItems(FXCollections.observableArrayList(stats));
        }
    }

    private void ensureRateAssociation(Rate rate, ClassEmployee group) {
        if (rate == null || group == null) return;

        // Set the association in the database
        em.getTransaction().begin();

        // Set the group for this rate
        rate.setGroup(group);

        // Make sure everything is persisted correctly
        em.merge(rate);
        em.merge(group);

        em.getTransaction().commit();
    }

    // Method to fix all existing rate associations
    private void fixRateAssociations() {
        System.out.println("Fixing rate associations...");

        try {
            em.getTransaction().begin();

            // Get all rates
            List<Rate> allRates = em.createQuery("SELECT r FROM Rate r", Rate.class).getResultList();
            System.out.println("Found " + allRates.size() + " total rates to check");

            for (Rate rate : allRates) {
                ClassEmployee group = rate.getGroup();

                if (group != null) {
                    System.out.println("Rate " + rate.getId() + " is associated with group: " + group.getEmployeeGroupName());

                    // Ensure the group has this rate in its collection (if we can access it)
                    // This would typically be handled by the addRate method in ClassEmployee

                    // Ensure the rate points to the group
                    rate.setGroup(group);
                    em.merge(rate);
                } else {
                    System.out.println("Rate " + rate.getId() + " has no group association!");
                    // We can't fix rates with no group association
                }
            }

            em.getTransaction().commit();
            System.out.println("Rate associations fixed.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error fixing rate associations: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showGroupRates(ClassEmployee group) {
        if(group != null) {
            try {
                // Direct database query for rates associated with this group
                List<Rate> databaseRates = em.createQuery(
                                "SELECT r FROM Rate r WHERE r.group.id = :groupId",
                                Rate.class)
                        .setParameter("groupId", group.getId())
                        .getResultList();

                System.out.println("Found " + databaseRates.size() + " rates for group: " + group.getEmployeeGroupName());

                // Convert to observable list for table display
                ObservableList<Rate> observableRates = FXCollections.observableArrayList(databaseRates);

                // Update the table
                rateTable.setItems(observableRates);
                rateTable.refresh();
            } catch (Exception e) {
                System.err.println("Error loading rates: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            // Clear the table if no group is selected
            rateTable.setItems(FXCollections.observableArrayList());
            rateTable.refresh();
        }
    }

    private void showAllRates()
    {
        // Kolekcja do przechowywania wszystkich pracowników
        ObservableList<Rate> allRates = FXCollections.observableArrayList();

        // Zbierz pracowników ze wszystkich grup
        for (ClassEmployee group : groups) {
            List<Rate> rates = group.getRates();
            if (employees != null) {
                for (Rate rate : rates) {
                    if (rate != null) {
                        allRates.add(rate);
                    }
                }
            }
        }

        // Ustaw wszystkich pracowników w tabeli
        rateTable.setItems(allRates);
        rateTable.refresh(); // Force refresh to update all cells correctly

        setupAverageGradeText();
    }

    // Metoda do dodawania nowej grupy
    public void addEmployeeGroup(String groupName, int maxEmployees, List<Employee> employeeList) {
        // Sprawdź czy grupa o takiej nazwie już istnieje
        for (ClassEmployee group : groups) {
            if (group.getEmployeeGroupName().equals(groupName)) {
                // Grupa już istnieje
                showErrorMessage("Group \"" + groupName + "\" already exists.");
                return;
            }
        }

        // Stwórz nową grupę
        ClassEmployee newGroup = new ClassEmployee(groupName, maxEmployees, employeeList);
        groups.add(newGroup);

        // Odśwież menu
        setupEmployeeGroupMenu();

        // Pokaż komunikat o sukcesie
        showSuccessMessage("Group \"" + groupName + "\" was added successfully.");
    }

    // Metoda do dodawania nowego pracownika do grupy
    public void addEmployeeToGroup(Employee employee, ClassEmployee group) {
        // Sprawdź czy jest jeszcze miejsce w grupie
        List<Employee> employees = group.getEmployeeList();
        int count = 0;

        if (employees != null) {
            count = employees.size();
        }

        if (count >= group.getMaxEmployeeNumber()) {
            showErrorMessage("Group \"" + group.getEmployeeGroupName() + "\" is full.");
            return;
        }

        // Dodaj pracownika do listy
        group.addEmployee(employee);

        // Jeśli aktualnie wyświetlana jest ta grupa lub wszystkie grupy, odśwież widok
        if (currentEmployeeGroup == null || currentEmployeeGroup == group) {
            showEmployeeGroup(group);
        } else if (showEmployeeGroup.getText().equals("All groups")) {
            showAllEmployees();
        }
    }

    public List<GroupRatingStats> getRatingStatsByGroup() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<Rate> rate = query.from(Rate.class);

        query.multiselect(
                rate.get("group").get("employeeGroupName"),
                cb.count(rate),
                cb.avg(rate.get("grade"))
        ).groupBy(rate.get("group").get("employeeGroupName"));

        List<Object[]> resultList = em.createQuery(query).getResultList();

        List<GroupRatingStats> stats = new ArrayList<>();
        for (Object[] row : resultList) {
            String name = (String) row[0];
            Long count = (Long) row[1];
            Double avg = (Double) row[2];
            stats.add(new GroupRatingStats(name, count, avg));
        }

        return stats;
    }

   private void  setupSendToSpringBootButton()
   {
       sendToSpringBootButton.setOnAction(event -> {
           new ApiClient().sendCompleteData(groups);
       });

   }

    private void loadSampleData() {
        // Create a temporary list for initial data
        List<Employee> allEmployees = new ArrayList<>();

        // Create employees and add them to the list
        em.getTransaction().begin();

        Employee emp1 = new Employee("John", "Smith", EmployeeCondition.PRESENT, 1985, 5000.0);
        Employee emp2 = new Employee("Emily", "Johnson", EmployeeCondition.DELEGATION, 1990, 6000.0);
        Employee emp3 = new Employee("Michael", "Williams", EmployeeCondition.SICK_LEAVE, 1988, 5500.0);
        Employee emp4 = new Employee("Sarah", "Brown", EmployeeCondition.PRESENT, 1982, 8000.0);
        Employee emp5 = new Employee("David", "Miller", EmployeeCondition.PRESENT, 1979, 8500.0);
        Employee emp6 = new Employee("Jessica", "Davis", EmployeeCondition.PRESENT, 1992, 4500.0);
        Employee emp7 = new Employee("Ryan", "Wilson", EmployeeCondition.DELEGATION, 1991, 4700.0);
        Employee emp8 = new Employee("Jennifer", "Taylor", EmployeeCondition.SICK_LEAVE, 1993, 4300.0);
        Employee emp9 = new Employee("Christopher", "Anderson", EmployeeCondition.PRESENT, 1989, 6200.0);
        Employee emp10 = new Employee("Ashley", "Thomas", EmployeeCondition.PRESENT, 1994, 5900.0);
        Employee emp11 = new Employee("Kevin", "Jackson", EmployeeCondition.PRESENT, 1987, 7000.0);
        Employee emp12 = new Employee("Nicole", "White", EmployeeCondition.DELEGATION, 1990, 7200.0);

        // Add employees to the list first before persisting
        allEmployees.add(emp1);
        allEmployees.add(emp2);
        allEmployees.add(emp3);
        allEmployees.add(emp4);
        allEmployees.add(emp5);
        allEmployees.add(emp6);
        allEmployees.add(emp7);
        allEmployees.add(emp8);
        allEmployees.add(emp9);
        allEmployees.add(emp10);
        allEmployees.add(emp11);
        allEmployees.add(emp12);

        // Persist all employees to the database
        em.persist(emp1);
        em.persist(emp2);
        em.persist(emp3);
        em.persist(emp4);
        em.persist(emp5);
        em.persist(emp6);
        em.persist(emp7);
        em.persist(emp8);
        em.persist(emp9);
        em.persist(emp10);
        em.persist(emp11);
        em.persist(emp12);

        em.getTransaction().commit();

        // Create groups and assign employees safely
        // Software Engineers group
        List<Employee> softwareEngineers = new ArrayList<>();
        softwareEngineers.add(allEmployees.get(0)); // Jan Kowalski
        softwareEngineers.add(allEmployees.get(1)); // Anna Nowak
        softwareEngineers.add(allEmployees.get(2)); // Piotr Wiśniewski
        ClassEmployee softwareGroup = new ClassEmployee("Software Engineers", 10, softwareEngineers);
        groups.add(softwareGroup);

        // Project Managers group
        List<Employee> projectManagers = new ArrayList<>();
        projectManagers.add(allEmployees.get(3)); // Alicja Dąbrowska
        projectManagers.add(allEmployees.get(4)); // Tomasz Lewandowski
        ClassEmployee managersGroup = new ClassEmployee("Project Managers", 5, projectManagers);
        groups.add(managersGroup);

        // QA Engineers group
        List<Employee> qaEngineers = new ArrayList<>();
        qaEngineers.add(allEmployees.get(5)); // Monika Kamińska
        qaEngineers.add(allEmployees.get(6)); // Michał Zieliński
        qaEngineers.add(allEmployees.get(7)); // Agnieszka Szymańska
        ClassEmployee qaGroup = new ClassEmployee("QA Engineers", 8, qaEngineers);
        groups.add(qaGroup);

        // UX Designers group
        List<Employee> uxDesigners = new ArrayList<>();
        uxDesigners.add(allEmployees.get(8)); // Marcin Woźniak
        uxDesigners.add(allEmployees.get(9)); // Karolina Kozłowska
        ClassEmployee uxGroup = new ClassEmployee("UX Designers", 5, uxDesigners);
        groups.add(uxGroup);

        // DevOps group
        List<Employee> devOps = new ArrayList<>();
        devOps.add(allEmployees.get(10)); // Adam Jankowski
        devOps.add(allEmployees.get(11)); // Marta Mazur
        ClassEmployee devOpsGroup = new ClassEmployee("DevOps", 6, devOps);
        groups.add(devOpsGroup);

        em.getTransaction().begin();

        em.persist(softwareGroup);
        em.persist(managersGroup);
        em.persist(qaGroup);
        em.persist(uxGroup);
        em.persist(devOpsGroup);

        Rate rate1 = new Rate(5, softwareGroup, LocalDate.of(2024, 6, 1), "Very good team");
        softwareGroup.addRate(rate1);

        Rate rate2 = new Rate(4, managersGroup, LocalDate.of(2024, 6, 2), "Good, but could improve planning");
        managersGroup.addRate(rate2);

        Rate rate3 = new Rate(3, qaGroup, LocalDate.of(2024, 6, 3), "Sometimes skips edge case testing");
        qaGroup.addRate(rate3);

        Rate rate4 = new Rate(6, uxGroup, LocalDate.of(2024, 6, 4), "Excellent aesthetics and usability");
        uxGroup.addRate(rate4);

        Rate rate5 = new Rate(5, devOpsGroup, LocalDate.of(2024, 6, 5), "Fast deployment and zero errors");
        devOpsGroup.addRate(rate5);

        // Adding more sample rates with English comments
        Rate rate6 = new Rate(4, softwareGroup, LocalDate.of(2024, 6, 15), "Good code quality, needs better documentation");
        softwareGroup.addRate(rate6);

        Rate rate7 = new Rate(5, managersGroup, LocalDate.of(2024, 6, 16), "Excellent communication with stakeholders");
        managersGroup.addRate(rate7);

        Rate rate8 = new Rate(3, qaGroup, LocalDate.of(2024, 6, 17), "Testing coverage needs improvement");
        qaGroup.addRate(rate8);

        Rate rate9 = new Rate(6, uxGroup, LocalDate.of(2024, 6, 18), "Innovative designs that users love");
        uxGroup.addRate(rate9);

        Rate rate10 = new Rate(4, devOpsGroup, LocalDate.of(2024, 6, 19), "Good infrastructure management, occasional monitoring issues");
        devOpsGroup.addRate(rate10);

        Rate rate11 = new Rate(5, softwareGroup, LocalDate.of(2024, 6, 25), "Reliable delivery and high-quality implementations");
        softwareGroup.addRate(rate11);

        Rate rate12 = new Rate(3, managersGroup, LocalDate.of(2024, 6, 26), "Budget control issues, otherwise good performance");
        managersGroup.addRate(rate12);

        Rate rate13 = new Rate(4, qaGroup, LocalDate.of(2024, 6, 27), "Improved automation testing framework");
        qaGroup.addRate(rate13);

        Rate rate14 = new Rate(5, uxGroup, LocalDate.of(2024, 6, 28), "Great attention to accessibility features");
        uxGroup.addRate(rate14);

        Rate rate15 = new Rate(6, devOpsGroup, LocalDate.of(2024, 6, 29), "Outstanding CI/CD pipeline improvements");
        devOpsGroup.addRate(rate15);

        // Persist all rates
        em.persist(rate1);
        em.persist(rate2);
        em.persist(rate3);
        em.persist(rate4);
        em.persist(rate5);
        em.persist(rate6);
        em.persist(rate7);
        em.persist(rate8);
        em.persist(rate9);
        em.persist(rate10);
        em.persist(rate11);
        em.persist(rate12);
        em.persist(rate13);
        em.persist(rate14);
        em.persist(rate15);

        em.getTransaction().commit();

        // Set initial data to show all employees
        employees.addAll(allEmployees);
    }
}