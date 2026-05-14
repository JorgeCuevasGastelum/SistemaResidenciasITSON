package administradorAsignaciones;

import dtos.AsignacionHabitacionDTO;
import dtos.AsignacionReporteDTO;
import java.util.List;

public interface IAdministradorAsignaciones {
    boolean asignarHabitacion(String residenteId, Integer numeroHabitacion);
    boolean tieneAsignacionActiva(String residenteId);
    AsignacionHabitacionDTO obtenerAsignacionActiva(String residenteId);
    List<AsignacionReporteDTO> obtenerListaAsignaciones();
    int contarHabitacionesOcupadas();
    int contarResidentesAsignados();
}
