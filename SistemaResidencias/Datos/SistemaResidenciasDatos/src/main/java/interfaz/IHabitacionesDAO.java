package interfaz;

import dtos.HabitacionDTO;
import java.util.List;

public interface IHabitacionesDAO {
    public List<HabitacionDTO> obtenerHabitacionesDisponibles();
}
