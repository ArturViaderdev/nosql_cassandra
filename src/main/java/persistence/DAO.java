/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import enums.EventType;
import enums.Urgency;
import exceptions.AppException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Employee;
import model.Event;
import model.Incidence;
import model.RankingTO;
import tool.Tool;

/**
 *
 * @author Artur Viader
 */
public class DAO implements DAOInterface {

    private Cluster cluster;
    private Session session;

    /**
     * Connects to database
     *
     * @param node
     * @param port
     */
    public void connect(final String node, final Integer port) {
        Cluster.Builder b = Cluster.builder().addContactPoint(node);
        if (port != null) {
            b.withPort(port);

            cluster = b.build();
            session = cluster.connect();
            createKeyspace("IncidencesDB", "SimpleStrategy", 1);
            useKeyspace("IncidencesDB");
            initData();
        }
    }

    /**
     * Session connected
     *
     * @return Session connected
     */
    public Session getSession() {
        return this.session;
    }

    /**
     * Close connection to database
     */
    public void close() {
        session.close();
        cluster.close();
    }

    /**
     * Info from keyspace using
     */
    public void getInfoKeyspace() {
        Metadata metadata = cluster.getMetadata();
        System.out.println("Cluster name: " + metadata.getClusterName());
        for (Host host : metadata.getAllHosts()) {
            System.out.println("Datacenter: " + host.getDatacenter() + " Host: " + host.getAddress() + " Rack: " + host.getRack());
        }
    }

    /**
     * Init data's tables
     */
    public void initData() {
        initTables();
    }

    /**
     * Init all necessary tables
     */
    public void initTables() {
        Map<String, String> employeesProperties = new HashMap<>();
        employeesProperties.put("username", "text");
        employeesProperties.put("pass", "text");
        employeesProperties.put("name", "text");
        employeesProperties.put("surname", "text");
        employeesProperties.put("phone", "text");
        String[] employeesPK = {"username"};
        String[] employeesOrder = {};
        createTable("Employee", employeesProperties, employeesPK, -1, employeesOrder);

        Map<String, String> eventProperties = new HashMap<>();
        eventProperties.put("dateCreated", "text");
        eventProperties.put("type", "text");
        eventProperties.put("employee", "text");
        String[] eventPK = {"employee", "type", "dateCreated"};
        String[] eventOrder = {"dateCreated"};
        createTable("Event", eventProperties, eventPK, 2, eventOrder);

        Map<String, String> incidencesProperties = new HashMap<>();
        incidencesProperties.put("id", "uuid");
        incidencesProperties.put("employeeFrom", "text");
        incidencesProperties.put("employeeDestination", "text");
        incidencesProperties.put("urgency", "text");
        incidencesProperties.put("dateCreated", "text");
        incidencesProperties.put("message", "text");
        String[] incidencesK = {"employeeFrom", "employeeDestination", "dateCreated", "id"};
        String[] incidencesOrder = {"dateCreated", "id"};
        createTable("Incidence", incidencesProperties, incidencesK, 2, incidencesOrder);
    }

    /**
     * Creates keyspace
     *
     * @param keyspaceName keyspace's name
     * @param replicationStrategy replcation type
     * @param numberOfReplicas number of replicas
     */
    public void createKeyspace(String keyspaceName, String replicationStrategy, int numberOfReplicas) {
        String query = "CREATE KEYSPACE IF NOT EXISTS " + keyspaceName
                + " WITH replication = {" + "'class':'" + replicationStrategy + "','replication_factor':" + numberOfReplicas + "};";
        session.execute(query);
    }

    /**
     * Uses a Keyspace
     *
     * @param keyspace
     */
    public void useKeyspace(String keyspace) {
        session.execute("USE " + keyspace);
    }

    /**
     * Deletes a Keyspace
     *
     * @param keyspaceName
     */
    public void deleteKeyspace(String keyspaceName) {
        String query = "DROP KEYSPACE " + keyspaceName;
        session.execute(query);
    }

    /**
     * Creates a table
     *
     * @param tableName
     * @param properties
     * @param primaryKeys
     * @param joinedPK
     * @param order
     */
    public void createTable(String tableName, Map<String, String> properties, String[] primaryKeys, int joinedPK, String[] order) {
        StringBuilder query = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + "(");
        for (Map.Entry<String, String> property : properties.entrySet()) {
            String key = property.getKey();
            String val = property.getValue();
            query.append(key).append(" ").append(val).append(", ");
        }
        query.append("PRIMARY KEY(");
        for (int i = 0; i < primaryKeys.length; i++) {
            if (joinedPK - 1 > 0) {
                if (i == 0) {
                    query.append("(");
                }
                query.append(primaryKeys[i]);
                if (i == joinedPK - 1) {
                    query.append(")");
                }
            } else {
                query.append(primaryKeys[i]);
            }
            if (i != (primaryKeys.length - 1)) {
                query.append(", ");
            }
        }
        query.append("))");
        if (order.length > 0) {
            if (order.length == 1) {
                query.append("WITH CLUSTERING ORDER BY (").append(order[0]).append(" DESC)");
            } else {
                query.append("WITH CLUSTERING ORDER BY (");
                for (int i = 0; i < order.length; i++) {
                    query.append(order[i]).append(" DESC");
                    if (i < order.length - 1) {
                        query.append(",");
                    }
                }
                query.append(")");
            }
        }
        query.append(";");
        session.execute(query.toString());
    }

    /**
     * Get a employee
     *
     * @param username by username to found
     * @return employee
     */
    public Employee getEmployee(String username) {
        String query = "SELECT * FROM Employee WHERE username='" + username + "'";
        ResultSet employee = session.execute(query);
        Row row = employee.one();
        if (row != null) {
            Employee e = new Employee(row.getString("username"), row.getString("pass"), row.getString("name"), row.getString("surname"), row.getString("phone"));
            return e;
        }
        return null;
    }

    /**
     * Logins an employee with username and password given
     *
     * @param username Username to found
     * @param pass password to found
     * @return employee if found, null if not
     * @throws AppException employee not found
     */
    @Override
    public Employee loginEmployee(String username, String pass) throws AppException {
        Employee e = getEmployee(username);
        if (e == null || !e.getPass().equals(pass)) {
            throw new AppException(AppException.USER_NOT_FOUND);
        }
        return e;
    }

    /**
     * Inserts an employee
     *
     * @param e employee to be inserted
     * @throws AppException employee found
     */
    @Override
    public void insertEmployee(Employee e) throws AppException {
        if (getEmployee(e.getUsername()) != null) {
            throw new AppException(AppException.EXISTING_USER);
        }
        String query = "INSERT INTO Employee(username, pass, name, surname, phone) VALUES ('"
                + e.getUsername() + "','" + e.getPass() + "','" + e.getName() + "','" + e.getSurname() + "','" + e.getPhone() + "');";
        session.execute(query);
    }

    /**
     * Updates an employee
     *
     * @param e employee to be updated
     * @throws AppException employee not found
     */
    @Override
    public void updateEmployee(Employee e) throws AppException {
        if (getEmployee(e.getUsername()) == null) {
            throw new AppException(AppException.USER_NOT_FOUND);
        }
        String query = "UPDATE Employee SET pass='" + e.getPass() + "', name='" + e.getName() + "', surname='" + e.getSurname() + "', phone='" + e.getPhone() + "' WHERE username='" + e.getUsername() + "'";
        session.execute(query);
    }

    /**
     * Removes an employee and all its data from the other tables
     *
     * @param e
     * @throws AppException Employee not found
     */
    @Override
    public void removeEmployee(Employee e) throws AppException {

        try {
            removeIncidencesByEmployee(e);
        } catch (AppException ex) {
        }

        try {
            removeEventsByEmployee(e);
        } catch (AppException ex) {
        }
        if (getEmployee(e.getUsername()) == null) {
            throw new AppException(AppException.USER_NOT_FOUND);
        }
        String query = "DELETE FROM Employee WHERE username='" + e.getUsername() + "'";
        session.execute(query);
    }

    /**
     * Gets an incidence by its id
     *
     * @param id id to found
     * @return incidence
     */
    @Override
    public Incidence getIncidenceById(String id) {
        String query = "SELECT * FROM Incidence WHERE id=" + id;
        ResultSet incidence = session.execute(query);
        Row row = incidence.one();
        if (row != null) {
            try {
                Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(row.getDate("dateCreated").toString());
                Incidence i = new Incidence(getEmployee(row.getString("employeeFrom")), getEmployee(row.getString("employeeDestination")), row.getString("message"), Urgency.valueOf(row.getString("urgency")), date);
                return i;
            } catch (ParseException ex) {

            }
        }
        return null;
    }

    /**
     * Selects all incidences from the table
     *
     * @return all incidences created
     * @throws AppException Incidences not found
     */
    @Override
    public List<Incidence> selectAllIncidences() throws AppException {
        String query = "SELECT * FROM Incidence";
        ResultSet i = session.execute(query);
        List<Incidence> incidences = new ArrayList<>();
        for (Row row : i) {
            try {
                Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(row.getString("dateCreated"));
                Incidence incidence = new Incidence(row.getUUID("id").toString(), getEmployee(row.getString("employeeFrom")), getEmployee(row.getString("employeeDestination")), row.getString("message"), Urgency.valueOf(row.getString("urgency")), date);
                incidences.add(incidence);
            } catch (ParseException ex) {

            }
        }
        if (incidences.isEmpty()) {
            throw new AppException(AppException.NO_INCIDENCES_FOUND);
        }
        return incidences;
    }

    /**
     * Inserts an incidence
     *
     * @param i incidence to insert
     */
    @Override
    public void insertIncidence(Incidence i) {
        String query = "INSERT INTO Incidence(id, employeeFrom, employeeDestination, urgency, dateCreated, message) VALUES (uuid(),'" + i.getFrom().getUsername() + "','" + i.getDestination().getUsername() + "','" + i.getUrgency().toString() + "','" + i.getFormatDateCreated() + "','" + i.getMessage() + "');";
        session.execute(query);
    }

    /**
     * Gets all incidence from employee created
     *
     * @param e employee from
     * @return all incidences found
     * @throws AppException no incidences found
     */
    @Override
    public List<Incidence> getIncidenceByFrom(Employee e) throws AppException {
        String query = "SELECT * FROM Incidence WHERE employeeFrom='" + e.getUsername() + "' ALLOW FILTERING";
        ResultSet i = session.execute(query);
        List<Incidence> incidences = new ArrayList<>();
        for (Row row : i) {
            try {
                Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(row.getString("dateCreated"));
                Incidence incidence = new Incidence(getEmployee(row.getString("employeeFrom")), getEmployee(row.getString("employeeDestination")), row.getString("message"), Urgency.valueOf(row.getString("urgency")), date);
                incidences.add(incidence);
            } catch (ParseException ex) {

            }
        }
        if (incidences.isEmpty()) {
            throw new AppException(AppException.NO_INCIDENCES_FOUND);
        }
        return incidences;
    }

    /**
     * Gets all incidences from employee destination
     *
     * @param e employee destination
     * @return all incidences found
     * @throws AppException no incidences found
     */
    @Override
    public List<Incidence> getIncidenceByDestination(Employee e) throws AppException {
        String query = "SELECT * FROM Incidence WHERE employeeDestination='" + e.getUsername() + "' ALLOW FILTERING";
        ResultSet i = session.execute(query);
        List<Incidence> incidences = new ArrayList<>();
        for (Row row : i) {
            try {
                Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(row.getString("dateCreated"));
                Incidence incidence = new Incidence(getEmployee(row.getString("employeeFrom")), getEmployee(row.getString("employeeDestination")), row.getString("message"), Urgency.valueOf(row.getString("urgency")), date);
                incidences.add(incidence);
            } catch (ParseException ex) {

            }
        }
        if (incidences.isEmpty()) {
            throw new AppException(AppException.NO_INCIDENCES_FOUND);
        }
        return incidences;
    }

    /**
     * Removes all incidences from an employee
     *
     * @param e employee's incidence to delete
     * @throws AppException no incidences found
     */
    public void removeIncidencesByEmployee(Employee e) throws AppException {
        List<Incidence> incidences = selectAllIncidences();
        for (Incidence incidence : incidences) {
            if (incidence.getFrom().getUsername().equals(e.getUsername()) || incidence.getDestination().getUsername().equals(e.getUsername())) {
                String query = "DELETE FROM Incidence WHERE id=" + incidence.getId() + " AND employeeFrom='" + incidence.getFrom().getUsername() + "' AND employeeDestination='" + incidence.getDestination().getUsername() + "' AND dateCreated='" + incidence.getFormatDateCreated() + "'";
                session.execute(query);
            }
        }
    }

    /**
     * Inserts an event
     *
     * @param e event to insert
     */
    @Override
    public void insertEvent(Event e) {
        String query = "INSERT INTO Event(employee, type, dateCreated) VALUES ('" + e.getEmployee().getUsername() + "','" + e.getType().toString() + "','" + e.getFormatDateCreated() + "');";
        session.execute(query);
    }

    /**
     * Removes all events from employee
     *
     * @param e employee's event to delete
     * @throws AppException User noy found
     */
    public void removeEventsByEmployee(Employee e) throws AppException {
        if (getEmployee(e.getUsername()) == null) {
            throw new AppException(AppException.USER_NOT_FOUND);
        }
        List<Event> events = getEventsByEmployee(e);
        for (Event event : events) {
            String query = "DELETE FROM Event WHERE employee='" + e.getUsername() + "' AND type='" + event.getType().toString() + "' AND dateCreated='" + event.getFormatDateCreated() + "'";
            session.execute(query);
        }

    }

    /**
     * Gets the last time an employee has logged in
     *
     * @param e employee logged in
     * @return event log in
     * @throws AppException no logins' event created
     */
    @Override
    public Event getLastLogIn(Employee e) throws AppException {
        String query = "SELECT * FROM Event WHERE type='I' AND employee='" + e.getUsername() + "'";
        ResultSet result = session.execute(query);
        List<Event> events = new ArrayList<>();
        for (Row row : result) {
            if (row != null) {
                try {
                    Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(row.getString("datecreated"));
                    Event event = new Event(EventType.valueOf(row.getString("type")), date, getEmployee(row.getString("employee")));
                    events.add(event);
                } catch (ParseException ex) {
                }
            }
        }
        if (events.isEmpty()) {
            throw new AppException(AppException.NO_LOGIN_REGISTERED);
        }
        events = Tool.orderEvents(events);
        return events.get(0);
    }

    /**
     * Gets ranking urgent's incidences ordered by desc
     *
     * @return ranking
     * @throws AppException No incidences found
     */
    @Override
    public List<RankingTO> getRankingEmployees() throws AppException {
        String query = "SELECT employeeFrom, COUNT(*) as count FROM Incidence WHERE urgency='URGENT' GROUP BY employeeFrom, employeeDestination ALLOW FILTERING";
        ResultSet result = session.execute(query);
        List<RankingTO> ranking = new ArrayList<>();
        for (Row row : result) {
            RankingTO rankingTo = new RankingTO(row.getString("employeeFrom"), (int) row.getLong("count"));
            ranking.add(rankingTo);
        }
        if (ranking.isEmpty()) {
            throw new AppException(AppException.NO_INCIDENCES_FOUND);
        }
        Collections.sort(ranking);
        return ranking;
    }

    /**
     * Gets all the events grouped by and ordered by date
     *
     * @param employee employee's event
     * @return all events found
     * @throws AppException no events found
     */
    public List<Event> getEventsByEmployee(Employee employee) throws AppException {
        String query = "SELECT * FROM Event WHERE employee='" + employee.getUsername() + "' ALLOW FILTERING;";
        ResultSet i = session.execute(query);
        List<Event> events = new ArrayList<>();
        for (Row row : i) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Date date = format.parse(row.getString("dateCreated"));
                Event event = new Event(EventType.valueOf(row.getString("type")), date, getEmployee(row.getString("employee")));
                events.add(event);
            } catch (ParseException ex) {

            }
        }
        if (events.isEmpty()) {
            throw new AppException(AppException.NO_EVENTS_FOUND);
        }
        return events;
    }

    /**
     * Gets all employees from the database
     *
     * @return all employee
     * @throws AppException no employees found
     */
    public List<Employee> getEmployees() throws AppException {
        String query = "SELECT * FROM employee;";
        ResultSet i = session.execute(query);
        List<Employee> employees = new ArrayList<>();
        for (Row row : i) {
            Employee employee = new Employee(row.getString("username"), "", row.getString("name"), row.getString("surname"), row.getString("phone"));
            employees.add(employee);
        }
        if (employees.isEmpty()) {
            throw new AppException(AppException.NO_USERS_FOUND);
        }
        return employees;
    }

}
