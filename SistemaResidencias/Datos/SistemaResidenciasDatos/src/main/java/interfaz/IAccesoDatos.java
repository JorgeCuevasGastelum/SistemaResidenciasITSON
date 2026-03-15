package interfaz;

import dtos.HabitacionDTO;
import dtos.ResidenteDTO;
import java.util.List;

public interface IAccesoDatos {
    public List<ResidenteDTO> obtenerListadoResidentes();
    
    public ResidenteDTO getResidentePorId(String id);
    
    public List<HabitacionDTO> obtenerHabitacionesDisponibles();
}
