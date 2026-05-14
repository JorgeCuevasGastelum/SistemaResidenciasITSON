package interfaz;

import dtos.AsignacionHabitacionDTO;

public interface IAsignacionesDAO {

    boolean asignarHabitacion(String idResidente, Integer numeroHabitacion);
    boolean tieneAsignacionActiva(String residenteId);
    AsignacionHabitacionDTO obtenerAsignacionActiva(String residenteId);
    void crearAsignacionesMock();
}
