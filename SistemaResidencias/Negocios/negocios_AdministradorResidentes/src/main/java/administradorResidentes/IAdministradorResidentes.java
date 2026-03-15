package administradorResidentes;

import dtos.ResidenteDTO;
import java.util.List;

public interface IAdministradorResidentes {

    List<ResidenteDTO> obtenerResidentesActivos();

}
