package implementaciones;

import dtos.AsignacionHabitacionDTO;
import dtos.AsignacionReporteDTO;
import entidades.AsignacionHabitacion;
import entidades.Habitacion;
import entidades.Residente;
import enums.EstadoHabitacion;
import enums.EstadoResidenteENUM;
import interfaz.IAsignacionesDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

public class AsignacionesDAO implements IAsignacionesDAO {

    private EntityManager entityManager;

    public AsignacionesDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public boolean asignarHabitacion(String residenteId, Integer numeroHabitacion) {

        EntityTransaction tx = entityManager.getTransaction();

        try {

            tx.begin();

            Residente residente = entityManager.find(Residente.class, residenteId);

            if (residente == null) {
                tx.rollback();
                return false;
            }

            List<Habitacion> habitaciones = entityManager.createQuery(
                    "SELECT h FROM Habitacion h WHERE h.numero_habitacion = :numero",
                    Habitacion.class
            )
            .setParameter("numero", numeroHabitacion)
            .getResultList();

            if (habitaciones.isEmpty()) {
                tx.rollback();
                return false;
            }

            Habitacion habitacion = habitaciones.get(0);

            List<AsignacionHabitacion> asignacionesActivas = entityManager.createQuery(
                    "SELECT a FROM AsignacionHabitacion a WHERE a.residente.id = :residenteId AND a.estadoHabitacion = :estado",
                    AsignacionHabitacion.class
            )
            .setParameter("residenteId", residenteId)
            .setParameter("estado", EstadoHabitacion.ACTIVA)
            .getResultList();

            for (AsignacionHabitacion anterior : asignacionesActivas) {
                anterior.setEstadoHabitacion(EstadoHabitacion.CANCELADA);
                entityManager.merge(anterior);
            }

            AsignacionHabitacion asignacion = new AsignacionHabitacion();
            asignacion.setResidente(residente);
            asignacion.setHabitacion(habitacion);
            asignacion.setFechaInicio(LocalDate.now());
            asignacion.setFechaFin(LocalDate.now().plusMonths(6));
            asignacion.setCicloLectivo("2025-1");
            asignacion.setEstadoHabitacion(EstadoHabitacion.ACTIVA);

            entityManager.persist(asignacion);

            tx.commit();

            return true;

        } catch (Exception e) {

            if (tx.isActive()) {
                tx.rollback();
            }

            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean tieneAsignacionActiva(String residenteId) {
        Long count = entityManager.createQuery(
                "SELECT COUNT(a) FROM AsignacionHabitacion a WHERE a.residente.id = :id AND a.estadoHabitacion = :estado",
                Long.class)
                .setParameter("id", residenteId)
                .setParameter("estado", EstadoHabitacion.ACTIVA)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public AsignacionHabitacionDTO obtenerAsignacionActiva(String residenteId) {
        List<AsignacionHabitacion> result = entityManager.createQuery(
                "SELECT a FROM AsignacionHabitacion a WHERE a.residente.id = :id AND a.estadoHabitacion = :estado",
                AsignacionHabitacion.class)
                .setParameter("id", residenteId)
                .setParameter("estado", EstadoHabitacion.ACTIVA)
                .getResultList();

        if (result.isEmpty()) return null;

        AsignacionHabitacion a = result.get(0);
        return new AsignacionHabitacionDTO(
                a.getId().intValue(),
                a.getFechaInicio(),
                a.getFechaFin(),
                null,
                a.getCicloLectivo(),
                a.getHabitacion().getId(),
                a.getHabitacion().getNumero_habitacion(),
                a.getResidente().getId(),
                a.getResidente().getNombre()
        );
    }

    @Override
    public List<AsignacionReporteDTO> obtenerListaAsignaciones() {
        String jpql = """
            SELECT new dtos.AsignacionReporteDTO(
                a.habitacion.numero_habitacion,
                a.habitacion.piso,
                a.residente.nombre,
                a.residente.apellido_paterno,
                a.residente.apellido_materno,
                a.residente.carrera,
                a.habitacion.genero,
                a.residente.estadoPago,
                a.residente.fechaIngreso,
                a.cicloLectivo
            )
            FROM AsignacionHabitacion a
            WHERE a.estadoHabitacion = :estado
              AND a.residente.estado = :estadoResidente
            ORDER BY a.habitacion.numero_habitacion ASC
            """;
        TypedQuery<AsignacionReporteDTO> query = entityManager.createQuery(jpql, AsignacionReporteDTO.class);
        query.setParameter("estado", EstadoHabitacion.ACTIVA);
        query.setParameter("estadoResidente", EstadoResidenteENUM.ACTIVO);
        return query.getResultList();
    }

    @Override
    public int contarHabitacionesOcupadas() {
        Long count = entityManager.createQuery(
                "SELECT COUNT(DISTINCT a.habitacion.id) FROM AsignacionHabitacion a"
                + " WHERE a.estadoHabitacion = :estado AND a.residente.estado = :estadoResidente",
                Long.class)
                .setParameter("estado", EstadoHabitacion.ACTIVA)
                .setParameter("estadoResidente", EstadoResidenteENUM.ACTIVO)
                .getSingleResult();
        return count != null ? count.intValue() : 0;
    }

    @Override
    public int contarResidentesAsignados() {
        Long count = entityManager.createQuery(
                "SELECT COUNT(a) FROM AsignacionHabitacion a"
                + " WHERE a.estadoHabitacion = :estado AND a.residente.estado = :estadoResidente",
                Long.class)
                .setParameter("estado", EstadoHabitacion.ACTIVA)
                .setParameter("estadoResidente", EstadoResidenteENUM.ACTIVO)
                .getSingleResult();
        return count != null ? count.intValue() : 0;
    }

    public void crearAsignacionesMock() {

        EntityTransaction tx = entityManager.getTransaction();

        try {

            tx.begin();

            Residente r1 = entityManager.find(Residente.class, "00000252825");
            Residente r2 = entityManager.find(Residente.class, "00000203020");

            Habitacion h1 = entityManager.createQuery(
                    "SELECT h FROM Habitacion h WHERE h.numero_habitacion = 1101",
                    Habitacion.class
            ).getSingleResult();

            AsignacionHabitacion a1 = new AsignacionHabitacion();
            a1.setResidente(r1);
            a1.setHabitacion(h1);
            a1.setFechaInicio(LocalDate.of(2025, 1, 10));
            a1.setFechaFin(LocalDate.of(2025, 6, 10));
            a1.setCicloLectivo("2025-1");
            a1.setEstadoHabitacion(EstadoHabitacion.ACTIVA);

            AsignacionHabitacion a2 = new AsignacionHabitacion();
            a2.setResidente(r2);
            a2.setHabitacion(h1);
            a2.setFechaInicio(LocalDate.of(2025, 1, 10));
            a2.setFechaFin(LocalDate.of(2025, 6, 10));
            a2.setCicloLectivo("2025-1");
            a2.setEstadoHabitacion(EstadoHabitacion.ACTIVA);

            entityManager.persist(a1);
            entityManager.persist(a2);

            tx.commit();

            System.out.println("Asignaciones mock insertadas");

        } catch (Exception e) {

            if (tx.isActive()) {
                tx.rollback();
            }

            e.printStackTrace();
        }
    }

}
