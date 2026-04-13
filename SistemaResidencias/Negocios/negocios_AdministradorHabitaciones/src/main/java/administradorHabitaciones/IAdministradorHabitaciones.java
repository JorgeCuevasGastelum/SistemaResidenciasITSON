package administradorHabitaciones;

import dtos.HabitacionDTO;
import enums.GeneroENUM;
import java.util.List;

public interface IAdministradorHabitaciones {
     List<HabitacionDTO> obtenerHabitacionDisponibles();
     List<HabitacionDTO> obtenerHabitacionDisponiblesParaResidente(String id);
     List<HabitacionDTO> obtenerHabitacionDisponiblesPorGenero(GeneroENUM genero);
     List<HabitacionDTO> obtenerHabitacionDisponiblesPorPiso(GeneroENUM genero, int piso);
}
