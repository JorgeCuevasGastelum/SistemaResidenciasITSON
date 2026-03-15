package administradorHabitaciones;

import dtos.HabitacionDTO;
import java.util.List;

public interface IAdministradorHabitaciones {
     List<HabitacionDTO> obtenerHabitacionDisponibles();
}
