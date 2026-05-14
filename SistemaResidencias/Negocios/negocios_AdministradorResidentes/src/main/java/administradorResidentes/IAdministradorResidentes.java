package administradorResidentes;

import dtos.ResidenteDTO;
import enums.GeneroENUM;
import java.util.List;

public interface IAdministradorResidentes {

    void crearDatosMock();
    void limpiarDatosMock();

    List<ResidenteDTO> obtenerResidentesActivos();
    List<ResidenteDTO> obtenerTodosResidentes();
    ResidenteDTO obtenerResidentePorId(String id);
    List<ResidenteDTO> obtenerResidentesBusqueda(String textoComparable);
    List<ResidenteDTO> obtenerResidentePorGenero(GeneroENUM genero);
    List<ResidenteDTO> obtenerResidentesConHabitacion();
    List<ResidenteDTO> obtenerResidentesSinHabitacion();

    void guardarResidente(ResidenteDTO dto);
    void actualizarResidente(ResidenteDTO dto);
    void desactivarResidente(String id);
}
