package interfaz;

import dtos.HabitacionDTO;
import dtos.ResidenteDTO;
import java.util.List;

public interface IAccesoDatos {

    List<ResidenteDTO> obtenerListadoResidentes();

    ResidenteDTO getResidentePorId(String id);

    List<HabitacionDTO> obtenerHabitacionesDisponibles();

    List<HabitacionDTO> obtenerHabitacionesDisponiblesParaResidente(String residenteId);
}
