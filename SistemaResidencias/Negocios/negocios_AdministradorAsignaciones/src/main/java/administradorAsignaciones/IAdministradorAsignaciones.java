package administradorAsignaciones;

import dtos.AsignacionHabitacionDTO;

public interface IAdministradorAsignaciones {
    boolean asignarHabitacion(String residenteId, Integer numeroHabitacion);
    boolean tieneAsignacionActiva(String residenteId);
    AsignacionHabitacionDTO obtenerAsignacionActiva(String residenteId);
}
