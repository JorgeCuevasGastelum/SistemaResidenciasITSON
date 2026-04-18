package presentacion.control;

import administradorAsignaciones.IAdministradorAsignaciones;
import administradorHabitaciones.IAdministradorHabitaciones;
import administradorResidentes.IAdministradorResidentes;
import dtos.HabitacionDTO;
import dtos.ResidenteDTO;
import enums.GeneroENUM;
import java.util.List;
import presentacion.vistas.VistaMain;

/**
 * Controlador de la pantalla "Asignar Habitaciones".
 *
 * Responsabilidades: - Mediar entre la vista y los subsistemas de negocio. -
 * Las vistas NO se llaman entre sí; todo pasa por aquí. - Mantiene el estado de
 * selección actual.
 */
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

    public void seleccionarResidente(ResidenteDTO residente) {
        this.residenteSeleccionado = residente;
        this.habitacionSeleccionada = null;

        vista.marcarResidenteSeleccionado(residente);

        List<HabitacionDTO> habitaciones
                = adminHabitaciones.obtenerHabitacionDisponiblesParaResidente(residente.getId());
        vista.mostrarHabitaciones(habitaciones);

        vista.limpiarConfirmacion();
    }
    
    public void filtrarResidente(GeneroENUM genero){
           List<ResidenteDTO> residentes = adminResidentes.obtenerResidentePorGenero(genero);
           vista.mostrarResidentes(residentes);         
    }
    
    public void filtrarHabitacion(int piso){
        ResidenteDTO residenteSeleccionado = this.getResidenteSeleccionado();
        GeneroENUM genero = residenteSeleccionado.getGenero();
        List<HabitacionDTO> habitaciones = adminHabitaciones.obtenerHabitacionDisponiblesPorPiso(genero, piso);
        vista.mostrarHabitaciones(habitaciones);
    }

    public void seleccionarHabitacion(HabitacionDTO habitacion) {
        if (residenteSeleccionado == null) {
            return;
        }

        this.habitacionSeleccionada = habitacion;

        vista.marcarHabitacionSeleccionada(habitacion);
        vista.mostrarConfirmacion(residenteSeleccionado, habitacion);
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

    public ResidenteDTO getResidenteSeleccionado() {
        return residenteSeleccionado;
    }

    public HabitacionDTO getHabitacionSeleccionada() {
        return habitacionSeleccionada;
    }
    
}
