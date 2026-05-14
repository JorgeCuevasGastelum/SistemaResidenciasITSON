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
    public List<ResidenteDTO> obtenerTodosResidentes() {
        return residenteBO.getTodosResidentes();
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

    @Override
    public List<ResidenteDTO> obtenerResidentesConHabitacion() {
        return residenteBO.getResidentesConHabitacion();
    }

    @Override
    public List<ResidenteDTO> obtenerResidentesSinHabitacion() {
        return residenteBO.getResidentesSinHabitacion();
    }

    @Override
    public void guardarResidente(ResidenteDTO dto) {
        residenteBO.guardarResidente(dto);
    }

    @Override
    public void actualizarResidente(ResidenteDTO dto) {
        residenteBO.actualizarResidente(dto);
    }

    @Override
    public void desactivarResidente(String id) {
        residenteBO.desactivarResidente(id);
    }
}
