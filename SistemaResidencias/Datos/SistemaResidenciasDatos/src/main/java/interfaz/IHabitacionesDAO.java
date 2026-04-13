package interfaz;

import dtos.HabitacionDTO;
import enums.GeneroENUM;
import java.util.List;

public interface IHabitacionesDAO {
    List<HabitacionDTO> obtenerHabitacionesDisponibles();
    List<HabitacionDTO> obtenerHabitacionesDisponiblesParaResidente(String residenteId);
    List<HabitacionDTO> obtenerHabitacionDisponiblesPorGenero(GeneroENUM genero);
    List<HabitacionDTO> obtenerHabitacionDisponiblesPorPiso(GeneroENUM genero, int piso);
    void crearHabitacionesMock();
}
