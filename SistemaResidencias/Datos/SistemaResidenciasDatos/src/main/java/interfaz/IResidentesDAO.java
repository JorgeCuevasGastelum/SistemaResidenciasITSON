package interfaz;

import dtos.ResidenteDTO;
import java.util.List;

public interface IResidentesDAO {
    List<ResidenteDTO> obtenerListadoResidentesActivos();
    ResidenteDTO obtenerResidentePorId(String id);
    void crearResidentesMock();
    void limpiarBaseDatos();
    
}
