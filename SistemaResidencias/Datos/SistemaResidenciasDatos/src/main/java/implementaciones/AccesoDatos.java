package implementaciones;

import conexion.ManejadorConexiones;
import dtos.HabitacionDTO;
import dtos.ResidenteDTO;
import enums.GeneroENUM;
import interfaz.IAccesoDatos;
import interfaz.IAsignacionesDAO;
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
    
    /**
     * DAO para manejar los datos de las asignaciones de habitaciones en la base de datos
     */
    private IAsignacionesDAO asignacionesDAO = new AsignacionesDAO(em);
    
    
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

    @Override
    public boolean asignarHabitacion(String residenteId, Integer numeroHabitacion) {
        return this.asignacionesDAO.asignarHabitacion(residenteId, numeroHabitacion);
    }
    
    @Override
    public void crearDatosMock(){
        residentesDAO.crearResidentesMock();
        habitacionesDAO.crearHabitacionesMock();
        asignacionesDAO.crearAsignacionesMock();
    }

    @Override
    public void limpiarBaseDatos() {
        residentesDAO.limpiarBaseDatos();
    }

    @Override
    public List<ResidenteDTO> obtenerResultadoBusqueda(String textoComparable) {
        return residentesDAO.buscarResidentesSimilares(textoComparable);
    } 

    @Override
    public List<HabitacionDTO> obtenerHabitacionDisponiblesPorGenero(GeneroENUM genero) {
        return habitacionesDAO.obtenerHabitacionDisponiblesPorGenero(genero);
    }

    @Override
    public List<HabitacionDTO> obtenerHabitacionDisponiblesPorPiso(GeneroENUM genero, int piso) {
        return habitacionesDAO.obtenerHabitacionDisponiblesPorPiso(genero, piso);
    } 
}
