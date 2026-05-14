package objetosnegocio;

import dtos.ResidenteDTO;
import enums.GeneroENUM;
import implementaciones.AccesoDatos;
import interfaz.IAccesoDatos;
import java.util.List;

public class ResidenteBO {

    private static ResidenteBO residenteBO;

    public static ResidenteBO getInstance() {
        if (residenteBO == null) {
            residenteBO = new ResidenteBO();
        }
        return residenteBO;
    }

    public void crearDatosMock() {
        IAccesoDatos accesoDatos = new AccesoDatos();
        accesoDatos.crearDatosMock();
    }

    public void LimpiarDatosMock() {
        IAccesoDatos accesoDatos = new AccesoDatos();
        accesoDatos.limpiarBaseDatos();
    }

    public List<ResidenteDTO> getResidentesActivos() {
        IAccesoDatos accesoDatos = new AccesoDatos();
        return accesoDatos.obtenerListadoResidentes();
    }

    public List<ResidenteDTO> getTodosResidentes() {
        IAccesoDatos accesoDatos = new AccesoDatos();
        return accesoDatos.obtenerTodosResidentes();
    }

    public ResidenteDTO getResidentePorId(String id) {
        IAccesoDatos accesoDatos = new AccesoDatos();
        return accesoDatos.getResidentePorId(id);
    }

    public List<ResidenteDTO> getResidentesBusqueda(String texto) {
        IAccesoDatos accesoDatos = new AccesoDatos();
        return accesoDatos.obtenerResultadoBusqueda(texto);
    }

    public List<ResidenteDTO> getResidentesGenero(GeneroENUM genero) {
        IAccesoDatos accesoDatos = new AccesoDatos();
        return accesoDatos.buscarResidentesPorGenero(genero);
    }

    public List<ResidenteDTO> getResidentesConHabitacion() {
        IAccesoDatos accesoDatos = new AccesoDatos();
        return accesoDatos.obtenerResidentesConHabitacion();
    }

    public List<ResidenteDTO> getResidentesSinHabitacion() {
        IAccesoDatos accesoDatos = new AccesoDatos();
        return accesoDatos.obtenerResidentesSinHabitacion();
    }

    public void guardarResidente(ResidenteDTO dto) {
        IAccesoDatos accesoDatos = new AccesoDatos();
        accesoDatos.guardarResidente(dto);
    }

    public void actualizarResidente(ResidenteDTO dto) {
        IAccesoDatos accesoDatos = new AccesoDatos();
        accesoDatos.actualizarResidente(dto);
    }

    public void desactivarResidente(String id) {
        IAccesoDatos accesoDatos = new AccesoDatos();
        accesoDatos.desactivarResidente(id);
    }
}
