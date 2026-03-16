package administradorResidentes;

import dtos.ResidenteDTO;
import java.util.List;

public interface IAdministradorResidentes {
    
    void crearDatosMock();
    
    void limpiarDatosMock();

    List<ResidenteDTO> obtenerResidentesActivos();
    
    ResidenteDTO obtenerResidentePorId(String id);

}
