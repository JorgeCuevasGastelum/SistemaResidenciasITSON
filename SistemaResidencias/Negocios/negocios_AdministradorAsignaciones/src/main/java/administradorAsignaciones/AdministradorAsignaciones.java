package administradorAsignaciones;

import dtos.AsignacionHabitacionDTO;
import dtos.AsignacionReporteDTO;
import java.util.List;
import objetosnegocio.AsignarHabitacionBO;

public class AdministradorAsignaciones implements IAdministradorAsignaciones {

    private AsignarHabitacionBO asignacionesBO = AsignarHabitacionBO.getInstance();

    @Override
    public boolean asignarHabitacion(String residenteId, Integer numeroHabitacion) {
        return this.asignacionesBO.asignarHabitacion(residenteId, numeroHabitacion);
    }

    @Override
    public boolean tieneAsignacionActiva(String residenteId) {
        return this.asignacionesBO.tieneAsignacionActiva(residenteId);
    }

    @Override
    public AsignacionHabitacionDTO obtenerAsignacionActiva(String residenteId) {
        return this.asignacionesBO.obtenerAsignacionActiva(residenteId);
    }

    @Override
    public List<AsignacionReporteDTO> obtenerListaAsignaciones() {
        return this.asignacionesBO.obtenerListaAsignaciones();
    }

    @Override
    public int contarHabitacionesOcupadas() {
        return this.asignacionesBO.contarHabitacionesOcupadas();
    }

    @Override
    public int contarResidentesAsignados() {
        return this.asignacionesBO.contarResidentesAsignados();
    }
}
