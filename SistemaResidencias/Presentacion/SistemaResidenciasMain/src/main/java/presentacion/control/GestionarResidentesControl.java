package presentacion.control;

import administradorAsignaciones.IAdministradorAsignaciones;
import administradorResidentes.IAdministradorResidentes;
import dtos.AsignacionHabitacionDTO;
import dtos.ResidenteDTO;
import java.util.List;
import presentacion.vistas.PantallaGestionarResidentes;

public class GestionarResidentesControl {

    private final IAdministradorResidentes adminResidentes;
    private final IAdministradorAsignaciones adminAsignaciones;

    private PantallaGestionarResidentes vista;

    public GestionarResidentesControl(IAdministradorResidentes adminResidentes,
            IAdministradorAsignaciones adminAsignaciones) {
        this.adminResidentes = adminResidentes;
        this.adminAsignaciones = adminAsignaciones;
    }

    public void setVista(PantallaGestionarResidentes vista) {
        this.vista = vista;
    }

    public void cargarResidentes() {
        List<ResidenteDTO> residentes = adminResidentes.obtenerTodosResidentes();
        enriquecerConHabitacion(residentes);
        vista.mostrarResidentes(residentes);
    }

    public void guardarResidente(ResidenteDTO dto) {
        adminResidentes.guardarResidente(dto);
        cargarResidentes();
    }

    public void actualizarResidente(ResidenteDTO dto) {
        adminResidentes.actualizarResidente(dto);
        cargarResidentes();
    }

    public void eliminarResidente(String id) {
        adminResidentes.desactivarResidente(id);
        cargarResidentes();
    }

    public ResidenteDTO obtenerDetalle(String id) {
        ResidenteDTO dto = adminResidentes.obtenerResidentePorId(id);
        if (dto != null) {
            AsignacionHabitacionDTO asignacion = adminAsignaciones.obtenerAsignacionActiva(id);
            if (asignacion != null && asignacion.getNumeroHabitacion() != null) {
                dto.setNumeroHabitacion(String.valueOf(asignacion.getNumeroHabitacion()));
            }
        }
        return dto;
    }

    private void enriquecerConHabitacion(List<ResidenteDTO> residentes) {
        for (ResidenteDTO r : residentes) {
            AsignacionHabitacionDTO asignacion = adminAsignaciones.obtenerAsignacionActiva(r.getId());
            if (asignacion != null && asignacion.getNumeroHabitacion() != null) {
                r.setNumeroHabitacion(String.valueOf(asignacion.getNumeroHabitacion()));
            }
        }
    }
}
