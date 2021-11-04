/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

import exceptions.AppException;
import java.util.List;
import model.Employee;
import model.Event;
import model.Incidence;
import model.RankingTO;

/**
 *
 * @author User
 */
public interface DAOInterface {

    // Método para insertar un nuevo empleado.
    public void insertEmployee(Employee e) throws AppException;

    // Método para validar el login de un empleado.
    public Employee loginEmployee(String user, String pass) throws AppException;

    // Método para modificar el perfil de un empleado.
    public void updateEmployee(Employee e) throws AppException;

    // Método para eliminar un empleado.
    public void removeEmployee(Employee e) throws AppException;

    // Obtener una Incidencia a partir de su Id.
    public Incidence getIncidenceById(String id);

    // Obtener una lista de todas las incidencias
    public List<Incidence> selectAllIncidences() throws AppException;

    // Insertar una incidencia a partir de un objeto incidencia
    public void insertIncidence(Incidence i);

    // Obtener la lista de incidencias con destino un determinado
    // empleado, a partir de un objeto empleado.
    public List<Incidence> getIncidenceByFrom(Employee e) throws AppException;

    // Obtener la lista de incidencias con origen un determinado
    // empleado, a partir de un objeto empleado.
    public List<Incidence> getIncidenceByDestination(Employee e) throws AppException;

    // Método para insertar un evento en la tabla historial.
    // Pasaremos como parámetro un objeto tipo evento, y no devolverá nada.
    // Llamaremos a este método desde los métodos
    // que producen los eventos, que son 3:
    // 1) Cuando un usuario hace login 
    // 2) Cuando un usuario crea una incidencia de tipo urgente 
    // 3) Cuando se consultan las incidencias destinadas a un usuario 
    public void insertEvent(Event e);

    // Obtener la fecha-hora del último inicio de sesión para un empleado.
    public Event getLastLogIn(Employee e) throws AppException;

    // Obtener el ranking de los empleados por cantidad de incidencias
    // urgentes creadas d(más incidencias urgentes primero).
    public List<RankingTO> getRankingEmployees() throws AppException;
}
