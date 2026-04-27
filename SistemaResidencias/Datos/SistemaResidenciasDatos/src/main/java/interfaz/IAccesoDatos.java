package interfaz;

import dtos.HabitacionDTO;
import dtos.ResidenteDTO;
import enums.GeneroENUM;
import java.util.List;

public interface IAccesoDatos {

    List<ResidenteDTO> obtenerListadoResidentes();
    ResidenteDTO getResidentePorId(String id);
    List<ResidenteDTO> obtenerResultadoBusqueda(String textoComparable);
    List<ResidenteDTO> buscarResidentesPorGenero(GeneroENUM genero);
    List<ResidenteDTO> obtenerResidentesConHabitacion();
    List<ResidenteDTO> obtenerResidentesSinHabitacion();

    List<HabitacionDTO> obtenerHabitacionesDisponibles();
    List<HabitacionDTO> obtenerHabitacionesDisponiblesParaResidente(String residenteId);
    List<HabitacionDTO> obtenerHabitacionDisponiblesPorGenero(GeneroENUM genero);
    List<HabitacionDTO> obtenerHabitacionDisponiblesPorPiso(GeneroENUM genero, int piso);

    boolean asignarHabitacion(String residenteId, Integer numeroHabitacion);
    boolean tieneAsignacionActiva(String residenteId);

    void crearDatosMock();
    void limpiarBaseDatos();
}
