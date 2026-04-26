package administradorResidentes;

import dtos.ResidenteDTO;
import enums.GeneroENUM;
import java.util.List;

public interface IAdministradorResidentes {
    
    void crearDatosMock();
    
    void limpiarDatosMock();

    List<ResidenteDTO> obtenerResidentesActivos();
    
    ResidenteDTO obtenerResidentePorId(String id);
    
    List<ResidenteDTO> obtenerResidentesBusqueda(String textoComparable);
    
    List<ResidenteDTO> obtenerResidentePorGenero(GeneroENUM genero);

}
