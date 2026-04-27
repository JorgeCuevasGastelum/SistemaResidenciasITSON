package presentacion.control;

import administradorAsignaciones.IAdministradorAsignaciones;
import administradorHabitaciones.IAdministradorHabitaciones;
import administradorResidentes.IAdministradorResidentes;
import dtos.HabitacionDTO;
import dtos.ResidenteDTO;
import enums.GeneroENUM;
import java.util.List;
import presentacion.vistas.VistaMain;

public class AsignarHabitacionesControl {

    private final IAdministradorResidentes adminResidentes;
    private final IAdministradorHabitaciones adminHabitaciones;
    private final IAdministradorAsignaciones adminAsignaciones;

    private VistaMain vista;

    private ResidenteDTO residenteSeleccionado;
    private HabitacionDTO habitacionSeleccionada;

    public AsignarHabitacionesControl(IAdministradorResidentes adminResidentes,
            IAdministradorHabitaciones adminHabitaciones,
            IAdministradorAsignaciones adminAsignaciones) {
        this.adminResidentes = adminResidentes;
        this.adminHabitaciones = adminHabitaciones;
        this.adminAsignaciones = adminAsignaciones;
    }

    public void setVista(VistaMain vista) {
        this.vista = vista;
    }

    public void cargarResidentes() {
        List<ResidenteDTO> residentes = adminResidentes.obtenerResidentesActivos();
        vista.mostrarResidentes(residentes);
    }

    public void filtrarResidentesConHabitacion() {
        List<ResidenteDTO> residentes = adminResidentes.obtenerResidentesConHabitacion();
        vista.mostrarResidentes(residentes);
    }

    public void filtrarResidentesSinHabitacion() {
        List<ResidenteDTO> residentes = adminResidentes.obtenerResidentesSinHabitacion();
        vista.mostrarResidentes(residentes);
    }

    public void filtrarHabitacion(int piso) {
        if (residenteSeleccionado == null) return;
        List<HabitacionDTO> habitaciones;
        if (piso == 0) {
            habitaciones = adminHabitaciones.obtenerHabitacionDisponiblesParaResidente(
                    residenteSeleccionado.getId());
        } else {
            habitaciones = adminHabitaciones.obtenerHabitacionDisponiblesPorPiso(
                    residenteSeleccionado.getGenero(), piso);
        }
        vista.mostrarHabitaciones(habitaciones);
    }

    public void seleccionarResidente(ResidenteDTO residente) {
        this.residenteSeleccionado = residente;
        this.habitacionSeleccionada = null;

        vista.marcarResidenteSeleccionado(residente);

        List<HabitacionDTO> habitaciones
                = adminHabitaciones.obtenerHabitacionDisponiblesParaResidente(residente.getId());
        vista.mostrarHabitaciones(habitaciones);

        boolean esReasignacion = adminAsignaciones.tieneAsignacionActiva(residente.getId());
        vista.actualizarEstadoReasignacion(esReasignacion);
        vista.limpiarConfirmacion();
    }
    
    public void filtrarResidente(GeneroENUM genero){
           List<ResidenteDTO> residentes = adminResidentes.obtenerResidentePorGenero(genero);
           vista.mostrarResidentes(residentes);         
    }
    
    public void seleccionarHabitacion(HabitacionDTO habitacion) {
        if (residenteSeleccionado == null || !habitacion.tieneCapacidad()) {
            return;
        }

        this.habitacionSeleccionada = habitacion;

        boolean esReasignacion = adminAsignaciones.tieneAsignacionActiva(
                residenteSeleccionado.getId());

        vista.marcarHabitacionSeleccionada(habitacion);
        vista.mostrarConfirmacion(residenteSeleccionado, habitacion, esReasignacion);
    }

    public void confirmarAsignacion() {
        if (residenteSeleccionado == null || habitacionSeleccionada == null) {
            return;
        }

        boolean exito = adminAsignaciones.asignarHabitacion(
                residenteSeleccionado.getId(),
                habitacionSeleccionada.getNumero_habitacion());

        if (exito) {
            vista.mostrarAsignacionExitosa(residenteSeleccionado, habitacionSeleccionada);
            residenteSeleccionado = null;
            habitacionSeleccionada = null;
        } else {
            vista.mostrarErrorAsignacion("No se pudo completar la asignación. Intente de nuevo.");
        }
    }

    public void reiniciarFlujo() {
        residenteSeleccionado = null;
        habitacionSeleccionada = null;
        cargarResidentes();
        vista.limpiarTodo();
    }

    public ResidenteDTO obtenerResidenteDetalle(String id) {
        return adminResidentes.obtenerResidentePorId(id);
    }

    public ResidenteDTO getResidenteSeleccionado() {
        return residenteSeleccionado;
    }

    public HabitacionDTO getHabitacionSeleccionada() {
        return habitacionSeleccionada;
    }
    
}
