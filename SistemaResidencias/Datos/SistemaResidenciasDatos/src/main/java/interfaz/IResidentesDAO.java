package interfaz;

import dtos.ResidenteDTO;
import entidades.Residente;
import enums.GeneroENUM;
import java.util.List;

public interface IResidentesDAO {
    List<ResidenteDTO> obtenerListadoResidentesActivos();
    ResidenteDTO obtenerResidentePorId(String id);
    List<ResidenteDTO> buscarResidentesSimilares(String textoComparable);
    List<ResidenteDTO> buscarResidentesPorGenero(GeneroENUM genero);
    List<ResidenteDTO> obtenerResidentesConHabitacion();
    List<ResidenteDTO> obtenerResidentesSinHabitacion();
    void guardarResidente(Residente residente);
    void eliminarResidentePorId(String id);
    void crearResidentesMock();
    void limpiarBaseDatos();
}
