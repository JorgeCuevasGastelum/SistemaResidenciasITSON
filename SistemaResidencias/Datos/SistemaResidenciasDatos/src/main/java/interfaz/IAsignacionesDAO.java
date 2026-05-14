package interfaz;

import dtos.AsignacionHabitacionDTO;
import dtos.AsignacionReporteDTO;
import java.util.List;

public interface IAsignacionesDAO {

    boolean asignarHabitacion(String idResidente, Integer numeroHabitacion);
    boolean tieneAsignacionActiva(String residenteId);
    AsignacionHabitacionDTO obtenerAsignacionActiva(String residenteId);
    void crearAsignacionesMock();

    List<AsignacionReporteDTO> obtenerListaAsignaciones();
    int contarHabitacionesOcupadas();
    int contarResidentesAsignados();
}
