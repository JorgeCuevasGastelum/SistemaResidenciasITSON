package interfaz;

import dtos.HabitacionDTO;
import dtos.ResidenteDTO;
import enums.GeneroENUM;
import java.util.List;

public interface IAccesoDatos {

    List<ResidenteDTO> obtenerListadoResidentes();

    ResidenteDTO getResidentePorId(String id);

    List<HabitacionDTO> obtenerHabitacionesDisponibles();

    List<HabitacionDTO> obtenerHabitacionesDisponiblesParaResidente(String residenteId);
  
    List<HabitacionDTO> obtenerHabitacionDisponiblesPorGenero(GeneroENUM genero);

    List<HabitacionDTO> obtenerHabitacionDisponiblesPorPiso(GeneroENUM genero, int piso);

    List<ResidenteDTO> obtenerResultadoBusqueda(String textoComparable);
    
    boolean asignarHabitacion(String residenteId, Integer numeroHabitacion);
    
    void crearDatosMock();
    
    void limpiarBaseDatos();
}
