package interfaz;

import dtos.ResidenteDTO;
import java.util.List;

public interface IResidentesDAO {
    public List<ResidenteDTO> obtenerListadoResidentesActivos();
}
