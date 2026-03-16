package implementaciones;

import conexion.ManejadorConexiones;
import dtos.HabitacionDTO;
import dtos.ResidenteDTO;
import interfaz.IAccesoDatos;
import interfaz.IHabitacionesDAO;
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
     * DAO para manejar los datos de las habitaciones en la base de datos
     */
    private IHabitacionesDAO habitacionesDAO = new HabitacionesDAO(em);
    
    
    @Override
    public List<ResidenteDTO> obtenerListadoResidentes() {
        return this.residentesDAO.obtenerListadoResidentesActivos();
    }

    @Override
    public ResidenteDTO getResidentePorId(String id) {
        return this.residentesDAO.obtenerResidentePorId(id);
    }

    @Override
    public List<HabitacionDTO> obtenerHabitacionesDisponibles() {
        return this.habitacionesDAO.obtenerHabitacionesDisponibles();
    }
    
    @Override
    public List<HabitacionDTO> obtenerHabitacionesDisponiblesParaResidente(String residenteId){
        return this.habitacionesDAO.obtenerHabitacionesDisponiblesParaResidente(residenteId);
    }
    
    
}
