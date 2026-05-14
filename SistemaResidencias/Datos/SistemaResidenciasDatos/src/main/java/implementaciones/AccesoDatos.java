package implementaciones;

import conexion.ManejadorConexiones;
import dtos.AsignacionHabitacionDTO;
import dtos.HabitacionDTO;
import dtos.ReferenciasPagoDTO;
import dtos.ResidenteDTO;
import entidades.Residente;
import entidades.ReferenciasPago;
import enums.GeneroENUM;
import interfaz.IAccesoDatos;
import interfaz.IAsignacionesDAO;
import interfaz.IHabitacionesDAO;
import interfaz.IReferenciasPagoDAO;
import interfaz.IResidentesDAO;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class AccesoDatos implements IAccesoDatos {

    private EntityManager em = ManejadorConexiones.getEntityManager();
    private IResidentesDAO residentesDAO = new ResidentesDAO(em);
    private IHabitacionesDAO habitacionesDAO = new HabitacionesDAO(em);
    private IAsignacionesDAO asignacionesDAO = new AsignacionesDAO(em);
    private IReferenciasPagoDAO referenciasDAO = new ReferenciasPagoDAO(em);

    @Override
    public List<ResidenteDTO> obtenerListadoResidentes() {
        return this.residentesDAO.obtenerListadoResidentesActivos();
    }

    @Override
    public ResidenteDTO getResidentePorId(String id) {
        return this.residentesDAO.obtenerResidentePorId(id);
    }

    @Override
    public List<ResidenteDTO> obtenerResultadoBusqueda(String textoComparable) {
        return residentesDAO.buscarResidentesSimilares(textoComparable);
    }

    @Override
    public List<ResidenteDTO> buscarResidentesPorGenero(GeneroENUM genero) {
        return residentesDAO.buscarResidentesPorGenero(genero);
    }

    @Override
    public List<ResidenteDTO> obtenerResidentesConHabitacion() {
        return residentesDAO.obtenerResidentesConHabitacion();
    }

    @Override
    public List<ResidenteDTO> obtenerResidentesSinHabitacion() {
        return residentesDAO.obtenerResidentesSinHabitacion();
    }

    @Override
    public List<HabitacionDTO> obtenerHabitacionesDisponibles() {
        return this.habitacionesDAO.obtenerHabitacionesDisponibles();
    }

    @Override
    public List<HabitacionDTO> obtenerHabitacionesDisponiblesParaResidente(String residenteId) {
        return this.habitacionesDAO.obtenerHabitacionesDisponiblesParaResidente(residenteId);
    }

    @Override
    public List<HabitacionDTO> obtenerHabitacionDisponiblesPorGenero(GeneroENUM genero) {
        return habitacionesDAO.obtenerHabitacionDisponiblesPorGenero(genero);
    }

    @Override
    public List<HabitacionDTO> obtenerHabitacionDisponiblesPorPiso(GeneroENUM genero, int piso) {
        return habitacionesDAO.obtenerHabitacionDisponiblesPorPiso(genero, piso);
    }

    @Override
    public boolean asignarHabitacion(String residenteId, Integer numeroHabitacion) {
        return this.asignacionesDAO.asignarHabitacion(residenteId, numeroHabitacion);
    }

    @Override
    public boolean tieneAsignacionActiva(String residenteId) {
        return asignacionesDAO.tieneAsignacionActiva(residenteId);
    }

    @Override
    public AsignacionHabitacionDTO obtenerAsignacionActiva(String residenteId) {
        return asignacionesDAO.obtenerAsignacionActiva(residenteId);
    }

    @Override
    public Long guardarReferenciaPago(ReferenciasPagoDTO dto) {
        Residente residente = em.find(Residente.class, dto.getIdResidente());
        if (residente == null) return null;

        ReferenciasPago entidad = new ReferenciasPago(
                residente,
                dto.getConcepto(),
                dto.getReferenciaBancaria(),
                dto.getPlanDePago(),
                dto.getCicloLectivo(),
                dto.getMonto(),
                dto.getFechaLimite(),
                LocalDate.now()
        );
        return referenciasDAO.guardarReferencia(entidad);
    }

    @Override
    public void crearDatosMock() {
        residentesDAO.crearResidentesMock();
        habitacionesDAO.crearHabitacionesMock();
        asignacionesDAO.crearAsignacionesMock();
    }

    @Override
    public void limpiarBaseDatos() {
        residentesDAO.limpiarBaseDatos();
    }
}
