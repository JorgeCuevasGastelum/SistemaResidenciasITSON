package implementaciones;

import dtos.ResidenteDTO;
import entidades.Residente;
import enums.EstadoHabitacion;
import enums.EstadoPagoENUM;
import enums.EstadoResidenteENUM;
import enums.GeneroENUM;
import interfaz.IResidentesDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

public class ResidentesDAO implements IResidentesDAO {

    private EntityManager entityManager;

    public ResidentesDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<ResidenteDTO> obtenerListadoResidentesActivos() {
        String jpql = """
            SELECT new dtos.ResidenteDTO(
                r.id,
                r.nombre,
                r.apellido_paterno,
                r.apellido_materno,
                r.genero,
                r.estado,
                r.carrera
            )
            FROM Residente r
            WHERE r.estado = :estado
            """;
        TypedQuery<ResidenteDTO> query = entityManager.createQuery(jpql, ResidenteDTO.class);
        query.setParameter("estado", EstadoResidenteENUM.ACTIVO);
        return query.getResultList();
    }

    @Override
    public List<ResidenteDTO> obtenerTodosResidentes() {
        String jpql = """
            SELECT new dtos.ResidenteDTO(
                r.id,
                r.nombre,
                r.apellido_paterno,
                r.apellido_materno,
                r.fechaNacimiento,
                r.genero,
                r.direccion,
                r.correo,
                r.telefono,
                r.estado,
                r.permiso_vehicular,
                r.carrera
            )
            FROM Residente r
            ORDER BY r.nombre ASC
            """;
        TypedQuery<ResidenteDTO> query = entityManager.createQuery(jpql, ResidenteDTO.class);
        return query.getResultList();
    }

    @Override
    public ResidenteDTO obtenerResidentePorId(String id) {
        String jpql = """
            SELECT new dtos.ResidenteDTO(
                r.id,
                r.nombre,
                r.apellido_paterno,
                r.apellido_materno,
                r.fechaNacimiento,
                r.fechaIngreso,
                r.genero,
                r.direccion,
                r.ciudad,
                r.estado
                r.pais,
                r.correo,
                r.telefono,
                r.estado,
                r.permiso_vehicular,
                r.carrera,
                r.nombreAval,
                r.parentescoAval,
                r.telefonoAval,
                r.correoAval,
                r.direccionAval,
                r.modeloVehiculo,
                r.colorVehiculo,
                r.placasVehiculo,
                r.estadoPago,
                r.ultimoPago,
                r.adeudoPendiente
                r.isDeportista,
                r.isIntercambio
            )
            FROM Residente r
            WHERE r.id = :id
            """;
        TypedQuery<ResidenteDTO> query = entityManager.createQuery(jpql, ResidenteDTO.class);
        query.setParameter("id", id);
        List<ResidenteDTO> resultados = query.getResultList();
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    @Override
    public void guardarResidente(Residente residente) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.persist(residente);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void actualizarResidente(Residente residente) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.merge(residente);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void desactivarResidente(String id) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            Residente residente = entityManager.find(Residente.class, id);
            if (residente != null) {
                residente.setEstado(EstadoResidenteENUM.INACTIVO);
                entityManager.merge(residente);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void eliminarResidentePorId(String id) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            Residente residente = entityManager.find(Residente.class, id);
            if (residente != null) {
                entityManager.remove(residente);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public List<ResidenteDTO> buscarResidentesSimilares(String textoComparable) {
        String jpql = """
            SELECT new dtos.ResidenteDTO(
                r.id, r.nombre, r.apellido_paterno, r.apellido_materno,
                r.genero, r.estado, r.carrera
            )
            FROM Residente r
            WHERE (LOWER(r.nombre) LIKE :texto OR r.id LIKE :texto)
            AND r.estado = :estado
            """;
        TypedQuery<ResidenteDTO> query = entityManager.createQuery(jpql, ResidenteDTO.class);
        query.setParameter("texto", "%" + textoComparable.toLowerCase() + "%");
        query.setParameter("estado", EstadoResidenteENUM.ACTIVO);
        return query.getResultList();
    }

    @Override
    public List<ResidenteDTO> buscarResidentesPorGenero(GeneroENUM genero) {
        String jpql = """
            SELECT new dtos.ResidenteDTO(
                r.id, r.nombre, r.apellido_paterno, r.apellido_materno,
                r.genero, r.estado, r.carrera
            )
            FROM Residente r
            WHERE r.genero = :genero
            """;
        TypedQuery<ResidenteDTO> query = entityManager.createQuery(jpql, ResidenteDTO.class);
        query.setParameter("genero", genero);
        return query.getResultList();
    }

    @Override
    public List<ResidenteDTO> obtenerResidentesConHabitacion() {
        String jpql = """
            SELECT new dtos.ResidenteDTO(
                r.id, r.nombre, r.apellido_paterno, r.apellido_materno,
                r.genero, r.estado, r.carrera
            )
            FROM Residente r
            WHERE r.estado = :estado
            AND EXISTS (
                SELECT a FROM AsignacionHabitacion a
                WHERE a.residente = r AND a.estadoHabitacion = :estadoActiva
            )
            """;
        TypedQuery<ResidenteDTO> query = entityManager.createQuery(jpql, ResidenteDTO.class);
        query.setParameter("estado", EstadoResidenteENUM.ACTIVO);
        query.setParameter("estadoActiva", EstadoHabitacion.ACTIVA);
        return query.getResultList();
    }

    @Override
    public List<ResidenteDTO> obtenerResidentesSinHabitacion() {
        String jpql = """
            SELECT new dtos.ResidenteDTO(
                r.id, r.nombre, r.apellido_paterno, r.apellido_materno,
                r.genero, r.estado, r.carrera
            )
            FROM Residente r
            WHERE r.estado = :estado
            AND NOT EXISTS (
                SELECT a FROM AsignacionHabitacion a
                WHERE a.residente = r AND a.estadoHabitacion = :estadoActiva
            )
            """;
        TypedQuery<ResidenteDTO> query = entityManager.createQuery(jpql, ResidenteDTO.class);
        query.setParameter("estado", EstadoResidenteENUM.ACTIVO);
        query.setParameter("estadoActiva", EstadoHabitacion.ACTIVA);
        return query.getResultList();
    }

    @Override
    public void crearResidentesMock() {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();

            Residente r1 = new Residente();
            r1.setId("00000252274");
            r1.setNombre("Jorge");
            r1.setApellido_paterno("Cuevas");
            r1.setApellido_materno("Gastelum");
            r1.setFechaNacimiento(LocalDate.of(2004, 10, 11));
            r1.setGenero(GeneroENUM.HOMBRE);
            r1.setDireccion("Calle 1");
            r1.setCiudad("Cd. Obregon");
            r1.setEstadoPais("Sonora");
            r1.setPais("Mexico");
            r1.setCorreo("jorge.cuevas252274@potros.itson.edu.mx");
            r1.setTelefono("6441222916");
            r1.setEstado(EstadoResidenteENUM.ACTIVO);
            r1.setPermiso_vehicular(1);
            r1.setCarrera("Ing. Software");
            r1.setEstadoPago(EstadoPagoENUM.AL_CORRIENTE);
            r1.setIsDeportista(Boolean.TRUE);
            r1.setIsIntercambio(Boolean.FALSE);

            Residente r3 = new Residente();
            r3.setId("00000252825");
            r3.setNombre("Ari");
            r3.setApellido_paterno("Montoya");
            r3.setApellido_materno("Navarro");
            r3.setFechaNacimiento(LocalDate.of(2001, 11, 3));
            r3.setGenero(GeneroENUM.HOMBRE);
            r3.setDireccion("Calle 3");
            r3.setCiudad("Cd. Obregon");
            r3.setEstadoPais("Sonora");
            r3.setPais("Mexico");
            r3.setCorreo("ari@itson.edu.mx");
            r3.setTelefono("6447778888");
            r3.setEstado(EstadoResidenteENUM.ACTIVO);
            r3.setPermiso_vehicular(3);
            r3.setCarrera("Ing. Software");
            r3.setEstadoPago(EstadoPagoENUM.CON_DEUDA);
            r3.setIsDeportista(Boolean.TRUE);
            r3.setIsIntercambio(Boolean.FALSE);

            Residente r4 = new Residente();
            r4.setId("00000253017");
            r4.setNombre("Abril");
            r4.setApellido_paterno("Reyes");
            r4.setApellido_materno("Islas");
            r4.setFechaNacimiento(LocalDate.of(2005, 11, 3));
            r4.setGenero(GeneroENUM.MUJER);
            r4.setDireccion("Calle 4");
            r4.setCiudad("Nogales");
            r4.setEstadoPais("Sonora");
            r4.setPais("Mexico");
            r4.setCorreo("abril@itson.edu.mx");
            r4.setTelefono("6447722888");
            r4.setEstado(EstadoResidenteENUM.ACTIVO);
            r4.setPermiso_vehicular(4);
            r4.setCarrera("Ing. Software");
            r4.setEstadoPago(EstadoPagoENUM.AL_CORRIENTE);
            r4.setIsDeportista(Boolean.FALSE);
            r4.setIsIntercambio(Boolean.TRUE);
            

            entityManager.persist(r1);
            entityManager.persist(r3);
            entityManager.persist(r4);  

            tx.commit();
            System.out.println("Residentes mock insertados correctamente");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void limpiarBaseDatos() {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.createQuery("DELETE FROM ReferenciasPago").executeUpdate();
            entityManager.createQuery("DELETE FROM AsignacionHabitacion").executeUpdate();
            entityManager.createQuery("DELETE FROM Habitacion").executeUpdate();
            entityManager.createQuery("DELETE FROM Residente").executeUpdate();
            tx.commit();
            System.out.println("Base de datos limpiada correctamente");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
    }
}
