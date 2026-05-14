package interfaz;

import dtos.AsignacionHabitacionDTO;
import dtos.AsignacionReporteDTO;
import dtos.HabitacionDTO;
import dtos.ReferenciasPagoDTO;
import dtos.ResidenteDTO;
import enums.GeneroENUM;
import java.util.List;

public interface IAccesoDatos {

    List<ResidenteDTO> obtenerListadoResidentes();
    List<ResidenteDTO> obtenerTodosResidentes();
    ResidenteDTO getResidentePorId(String id);
    List<ResidenteDTO> obtenerResultadoBusqueda(String textoComparable);
    List<ResidenteDTO> buscarResidentesPorGenero(GeneroENUM genero);
    List<ResidenteDTO> obtenerResidentesConHabitacion();
    List<ResidenteDTO> obtenerResidentesSinHabitacion();

    void guardarResidente(ResidenteDTO dto);
    void actualizarResidente(ResidenteDTO dto);
    void desactivarResidente(String id);

    List<HabitacionDTO> obtenerHabitacionesDisponibles();
    List<HabitacionDTO> obtenerHabitacionesDisponiblesParaResidente(String residenteId);
    List<HabitacionDTO> obtenerHabitacionDisponiblesPorGenero(GeneroENUM genero);
    List<HabitacionDTO> obtenerHabitacionDisponiblesPorPiso(GeneroENUM genero, int piso);

    boolean asignarHabitacion(String residenteId, Integer numeroHabitacion);
    boolean tieneAsignacionActiva(String residenteId);
    AsignacionHabitacionDTO obtenerAsignacionActiva(String residenteId);
    List<AsignacionReporteDTO> obtenerListaAsignaciones();
    int contarHabitacionesOcupadas();
    int contarResidentesAsignados();

    Long guardarReferenciaPago(ReferenciasPagoDTO dto);

    void crearDatosMock();
    void limpiarBaseDatos();
}
