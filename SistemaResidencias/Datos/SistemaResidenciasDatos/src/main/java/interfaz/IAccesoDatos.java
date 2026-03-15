package interfaz;

import dtos.ResidenteDTO;
import java.util.List;

public interface IAccesoDatos {
    public List<ResidenteDTO> obtenerListadoResidentes();
}
