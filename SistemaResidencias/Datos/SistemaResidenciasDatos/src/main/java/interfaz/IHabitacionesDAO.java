package interfaz;

import dtos.HabitacionDTO;
import java.util.List;

public interface IHabitacionesDAO {
    List<HabitacionDTO> obtenerHabitacionesDisponibles();
    List<HabitacionDTO> obtenerHabitacionesDisponiblesParaResidente(String residenteId);
    void crearHabitacionesMock();
}
