package implementaciones;

import conexion.ManejadorConexiones;
import dtos.AsignacionHabitacionDTO;
import dtos.AsignacionReporteDTO;
import dtos.HabitacionDTO;
import dtos.ReferenciasPagoDTO;
import dtos.ResidenteDTO;
import entidades.Residente;
import entidades.ReferenciasPago;
import enums.EstadoPagoENUM;
import enums.EstadoResidenteENUM;
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
    public List<ResidenteDTO> obtenerTodosResidentes() {
        return this.residentesDAO.obtenerTodosResidentes();
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
    public void guardarResidente(ResidenteDTO dto) {
        Residente entidad = dtoAEntidad(dto);
        residentesDAO.guardarResidente(entidad);
    }

    @Override
    public void actualizarResidente(ResidenteDTO dto) {
        Residente entidad = dtoAEntidad(dto);
        residentesDAO.actualizarResidente(entidad);
    }

    @Override
    public void desactivarResidente(String id) {
        residentesDAO.desactivarResidente(id);
    }

    private Residente dtoAEntidad(ResidenteDTO dto) {
        Residente r = new Residente();
        r.setId(dto.getId());
        r.setNombre(dto.getNombre());
        r.setApellido_paterno(dto.getApellido_paterno());
        r.setApellido_materno(dto.getApellido_materno());
        r.setFechaNacimiento(dto.getFechaNacimiento() != null ? dto.getFechaNacimiento() : LocalDate.now());
        r.setFechaIngreso(dto.getFechaIngreso());
        r.setGenero(dto.getGenero());
        r.setDireccion(dto.getDireccion() != null ? dto.getDireccion() : "");
        r.setCorreo(dto.getCorreo() != null ? dto.getCorreo() : "");
        r.setTelefono(dto.getTelefono() != null ? dto.getTelefono() : "");
        r.setEstado(dto.getEstado() != null ? dto.getEstado() : EstadoResidenteENUM.ACTIVO);
        r.setPermiso_vehicular(dto.getPermiso_vehicular() != null ? dto.getPermiso_vehicular() : 0);
        r.setCarrera(dto.getCarrera() != null ? dto.getCarrera() : "");
        r.setNombreAval(dto.getNombreAval());
        r.setParentescoAval(dto.getParentescoAval());
        r.setTelefonoAval(dto.getTelefonoAval());
        r.setCorreoAval(dto.getCorreoAval());
        r.setDireccionAval(dto.getDireccionAval());
        r.setModeloVehiculo(dto.getModeloVehiculo());
        r.setColorVehiculo(dto.getColorVehiculo());
        r.setPlacasVehiculo(dto.getPlacasVehiculo());
        r.setEstadoPago(dto.getEstadoPago() != null ? dto.getEstadoPago() : EstadoPagoENUM.AL_CORRIENTE);
        r.setUltimoPago(dto.getUltimoPago());
        r.setAdeudoPendiente(dto.getAdeudoPendiente() != null ? dto.getAdeudoPendiente() : 0.0);
        return r;
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
    public List<AsignacionReporteDTO> obtenerListaAsignaciones() {
        return asignacionesDAO.obtenerListaAsignaciones();
    }

    @Override
    public int contarHabitacionesOcupadas() {
        return asignacionesDAO.contarHabitacionesOcupadas();
    }

    @Override
    public int contarResidentesAsignados() {
        return asignacionesDAO.contarResidentesAsignados();
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
