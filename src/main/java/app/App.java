package app;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import enums.EventType;
import enums.Urgency;
import exceptions.AppException;
import exceptions.InputException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Employee;
import model.Event;
import model.Incidence;
import model.RankingTO;
import persistence.DAO;
import tool.Tool;

/**
 *
 * @author Jonathan Cacay
 */
public class App {

    static Employee employee;
    static DAO dao;

    public static void main(String[] args) {
        dao = new DAO();
        dao.connect("127.0.0.1", 9042);
        boolean exit = false;
        while (!exit) {
            try {
                menuLogin();
                int option = Tool.askInt("Choose option: ");
                switch (option) {
                    case 1:
                        employee = login();
                        System.out.println("LOGGING IN...");
                        menuEmployee();
                        break;
                    case 2:
                        registerEmployee();
                        break;
                    case 0:
                        exit = true;
                        dao.close();
                        break;
                    default:
                        throw new InputException(InputException.INCORRECT_CHOICE);
                }
            } catch (InputException | AppException ex) {
                System.out.println(ex.getMessage());
            }
        }
        dao.close();
    }

    /**
     * Login menu
     */
    private static void menuLogin() {
        System.out.println("=======================================================\n"
                + "---- MENU ----\n"
                + "1. Login\n"
                + "2. Register\n"
                + "0. Exit\n"
                + "=======================================================");
    }

    /**
     * Employee menu
     */
    private static void menuEmployee() {
        boolean exit = false;
        while (!exit) {
            try {
                System.out.println("=======================================================\n"
                        + "---- MENU USER ----\n"
                        + "1. Create incidence\n"
                        + "2. Check all incidences\n"
                        + "3. Check incidence by user\n"
                        + "4. Check history\n"
                        + "5. Last time logged in\n"
                        + "6. Nº Urgent Incidences\n"
                        + "7. Modify profile\n"
                        + "8. Delete user\n"
                        + "0. Log out\n"
                        + "=======================================================");
                int option = Tool.askInt("Choose option: ", 0, 8);
                switch (option) {
                    case 1:
                        createIncidence();
                        break;
                    case 2:
                        checkAllIncidences();
                        break;
                    case 3:
                        checkIncidencesByUser();
                        break;
                    case 4:
                        checkHistory();
                        break;
                    case 5:
                        lastLogin();
                        break;
                    case 6:
                        numUrgentIncidences();
                        break;
                    case 7:
                        modifyProfile();
                        break;
                    case 8:
                        exit = deleteUser();
                        break;
                    case 0:
                        exit = true;
                        employee = new Employee();
                        break;
                }
            } catch (AppException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * Registers an Employee
     *
     * @throws AppException Employee exists or Differents password given
     */
    private static void registerEmployee() throws AppException {
        String username = Tool.askString("Username: ");
        String password = Tool.askString("Password: ");
        String confirm = Tool.askString("Password confirmation: ");
        if (!password.equals(confirm)) {
            throw new AppException(AppException.DIFFERENT_NEW_AND_VERIFIED_PASSWORD);
        }
        String name = Tool.askString("Name: ");
        String surname = Tool.askString("Surname: ");
        String phone = Tool.askString("Phone: ");
        Employee regEmployee = new Employee(username, password, name, surname, phone);
        dao.insertEmployee(regEmployee);
        System.out.println("Employee registered!");
    }

    /**
     *
     * Logins an employee
     *
     * @return if exists employee if not null
     * @throws AppException Employee exists
     */
    private static Employee login() throws AppException {
        String username = Tool.askString("Username: ");
        String password = Tool.askString("Password: ");
        employee = dao.loginEmployee(username, password);
        Date date = new Date();
        dao.insertEvent(new Event(EventType.I, date, employee));
        return employee;
    }

    /**
     * Creates an Incidence
     *
     * @throws AppException There are no users created except the one logged in
     */
    private static void createIncidence() throws AppException {
        Employee destination = selectEmployee();
        String message = Tool.askString("Message: ");
        Urgency urgency = selectUrgency();
        Date dateCreated = new Date();
        Incidence incidence = new Incidence(employee, destination, message, urgency, dateCreated);
        dao.insertIncidence(incidence);
        if (incidence.getUrgency().equals(Urgency.URGENT)) {
            dao.insertEvent(new Event(EventType.U, dateCreated, employee));
        }
        System.out.println("Incidence added!");

    }

    /**
     * Gets all incidences created and shows in console
     *
     * @throws AppException No incidences created
     */
    private static void checkAllIncidences() throws AppException {
        List<Incidence> incidences = dao.selectAllIncidences();
        incidences = Tool.orderIncidences(incidences);
        for (Incidence i : incidences) {
            System.out.println(i.toString());
        }
    }

    /**
     * Gets all incidences from a user
     *
     * @throws AppException No users found
     */
    private static void checkIncidencesByUser() throws AppException {
        String you = Tool.askString("Do you want to check your incidences(Y/N)? ", "Y", "N");
        Employee e;
        if (you.equalsIgnoreCase("Y")) {
            e = employee;
        } else {
            e = selectEmployee();
        }
        String option = Tool.askString("Incidences FROM or DESTINATION:", "FROM", "DESTINATION");
        List<Incidence> incidences = new ArrayList<>();
        switch (option.toUpperCase()) {
            case "FROM":
                incidences = dao.getIncidenceByFrom(e);
                break;
            case "DESTINATION":
                incidences = dao.getIncidenceByDestination(e);
                break;
        }
       incidences = Tool.orderIncidences(incidences);
        for (Incidence i : incidences) {
            System.out.println(i.toString());
        }
        dao.insertEvent(new Event(EventType.C, new Date(), e));
    }

    /**
     * Gets all the events done grouped by the existing events and ordered by
     * datecreated desc
     *
     * @throws AppException No events created
     */
    private static void checkHistory() throws AppException {
        List<Event> events = dao.getEventsByEmployee(employee);
        for (Event event : events) {
            System.out.println(event);
        }
    }

    /**
     *
     * Get the last time logged in
     *
     * @throws AppException No event logged in
     */
    private static void lastLogin() throws AppException {
        Event event = dao.getLastLogIn(employee);
        System.out.println("Last time LOGGED IN: " + event.getFormatDateCreated());
    }

    /**
     *
     * Num Urgent incidences from each user created
     *
     * @throws AppException No incidences found
     */
    private static void numUrgentIncidences() throws AppException {
        List<RankingTO> numIncidences = dao.getRankingEmployees();
       
        for (RankingTO incidence : numIncidences) {
            System.out.println(incidence);
        }
    }

    /**
     *
     * Modifies user's profile.
     *
     * @throws AppException Mismatched password from database OR Differents
     * passwords
     */
    private static void modifyProfile() throws AppException {
        String currentPassword = Tool.askString("Current Password: ");
        dao.loginEmployee(employee.getUsername(), currentPassword);
        String password = Tool.askString("New Password: ");
        String confirm = Tool.askString("New Password confirmation: ");
        if (!password.equals(confirm)) {
            throw new AppException(AppException.DIFFERENT_NEW_AND_VERIFIED_PASSWORD);
        }
        String name = Tool.askString("Name: ");
        String surname = Tool.askString("Surname: ");
        String phone = Tool.askString("Phone: ");
        employee.setName(name);
        employee.setPass(password);
        employee.setSurname(surname);
        employee.setPhone(phone);
        dao.updateEmployee(employee);
        System.out.println("Employee updated");
    }

    /**
     * Deletes a user and all its data from other tables
     *
     * @return true if its user logged in
     * @throws AppException employee doesn't exists
     */
    public static boolean deleteUser() throws AppException {
        String you = Tool.askString("Do you want to delete your account(Y/N)? ", "Y", "N");
        if (you.equalsIgnoreCase("Y")) {
            dao.removeEmployee(employee);
            System.out.println("Employee REMOVED");
            return true;
        } else {
            Employee e = selectEmployee();
            dao.removeEmployee(e);
            System.out.println("Employee REMOVED");
            return false;
        }
    }

    /**
     * Shows all users createde except the logged one
     *
     * @return employee
     * @throws AppException No employees found
     */
    private static Employee selectEmployee() throws AppException {
        int cont = 0;
        List<Employee> employees = dao.getEmployees();
        while (cont < employees.size()) {
            if (employee.getUsername().equals(employees.get(cont).getUsername())) {
                employees.remove(cont);
            } else {
                System.out.println(cont + 1 + " - " + employees.get(cont).getName() + " " + employees.get(cont).getSurname());
                cont += 1;
            }
        }
        int selection = Tool.askInt("Select an user:", 1, employees.size());
        return employees.get(selection - 1);
    }

    /**
     * Shows all urgencies
     *
     * @return urgency selected
     */
    private static Urgency selectUrgency() {
        int x = 1;
        System.out.println("Urgency: ");
        for (Urgency urgency : Urgency.values()) {
            System.out.println(x + " - " + urgency.name());
            x++;
        }
        int urgency = Tool.askInt("SELECT URGENCY: ", 1, Urgency.values().length);
        return Urgency.valueOf(urgency);
    }
}
