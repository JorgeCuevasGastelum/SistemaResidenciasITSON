package implementaciones;

import conexion.ManejadorConexiones;
import dtos.ResidenteDTO;
import interfaz.IAccesoDatos;
import interfaz.IResidentesDAO;
import jakarta.persistence.EntityManager;
import java.util.List;

public class AccesoDatos implements IAccesoDatos {
    
    private EntityManager em = ManejadorConexiones.getEntityManager();
    /**
     * DAO para manejar los datos de los residentes en la base de datos
     */
    private IResidentesDAO residentesDAO = new ResidentesDAO(em);
    
    /**
     * Obtiene un residente a partir de su matricula
     *
     * @param matricula Matricula del residente
     * @return DTO con los datos del residente
     */
    @Override
    public List<ResidenteDTO> obtenerListadoResidentes() {
        return this.residentesDAO.obtenerListadoResidentesActivos();
    }

    @Override
    public ResidenteDTO getResidentePorId(String id) {
        return this.residentesDAO.obtenerResidentePorId(id);
    }
}
