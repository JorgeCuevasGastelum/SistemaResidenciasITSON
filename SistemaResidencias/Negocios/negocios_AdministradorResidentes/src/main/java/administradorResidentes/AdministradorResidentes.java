package administradorResidentes;

import dtos.ResidenteDTO;
import java.util.List;
import objetosnegocio.ResidenteBO;

public class AdministradorResidentes implements IAdministradorResidentes {
    
    private ResidenteBO residenteBO = ResidenteBO.getInstance();
    
    @Override
    public List<ResidenteDTO> obtenerResidentesActivos() {
        return residenteBO.getResidentesActivos();
    }
    
}
