package administradorResidentes;

import dtos.ResidenteDTO;
import enums.GeneroENUM;
import java.util.List;
import objetosnegocio.ResidenteBO;

public class AdministradorResidentes implements IAdministradorResidentes {

    private ResidenteBO residenteBO = ResidenteBO.getInstance();

    @Override
    public void crearDatosMock() {
        residenteBO.crearDatosMock();
    }
    
    @Override
    public void limpiarDatosMock() {
        residenteBO.LimpiarDatosMock();
    }

    @Override
    public List<ResidenteDTO> obtenerResidentesActivos() {
        return residenteBO.getResidentesActivos();
    }

    @Override
    public ResidenteDTO obtenerResidentePorId(String id) {
        return residenteBO.getResidentePorId(id);
    }

    @Override
    public List<ResidenteDTO> obtenerResidentesBusqueda(String textoComparable) {
        return residenteBO.getResidentesBusqueda(textoComparable);
        
    }

    @Override
    public List<ResidenteDTO> obtenerResidentePorGenero(GeneroENUM genero) {
        return residenteBO.getResidentesGenero(genero);
    }

}
