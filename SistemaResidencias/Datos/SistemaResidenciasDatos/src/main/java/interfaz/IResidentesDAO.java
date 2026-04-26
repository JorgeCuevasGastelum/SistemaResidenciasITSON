package interfaz;

import dtos.ResidenteDTO;
import enums.GeneroENUM;
import java.util.List;

public interface IResidentesDAO {
    List<ResidenteDTO> obtenerListadoResidentesActivos();
    ResidenteDTO obtenerResidentePorId(String id);
    List<ResidenteDTO> buscarResidentesSimilares(String textoComparable);
    List<ResidenteDTO> buscarResidentesPorGenero(GeneroENUM genero);
    void crearResidentesMock();
    void limpiarBaseDatos();
}
