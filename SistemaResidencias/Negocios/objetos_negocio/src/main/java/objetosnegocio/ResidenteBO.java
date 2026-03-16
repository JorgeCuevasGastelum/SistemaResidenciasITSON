package objetosnegocio;

import dtos.ResidenteDTO;
import implementaciones.AccesoDatos;
import interfaz.IAccesoDatos;
import java.util.List;

public class ResidenteBO {
    
    /**
     * Instancia Singleton del objeto negocio de residente.
     */
    private static ResidenteBO residenteBO;

    /**
     * Metodo que obtiene la instancia del objeto negocio de residente.
     *
     * @return Instancia del objeto negocio de residente
     */
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
    
    public ResidenteDTO getResidentePorId(String id) {
        IAccesoDatos accesoDatos = new AccesoDatos();
        return accesoDatos.getResidentePorId(id); 

    }
    
    
}
