package implementaciones;

import entidades.AsignacionHabitacion;
import entidades.Habitacion;
import entidades.Residente;
import enums.EstadoHabitacion;
import interfaz.IAsignacionesDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.time.LocalDate;

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

        Habitacion habitacion = entityManager.createQuery(
                "SELECT h FROM Habitacion h WHERE h.numero_habitacion = :numero",
                Habitacion.class
        )
        .setParameter("numero", numeroHabitacion)
        .getSingleResult();

        if (residente == null || habitacion == null) {
            return false;
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

    public void crearAsignacionesMock() {

        EntityTransaction tx = entityManager.getTransaction();

        try {

            tx.begin();

            Residente r1 = entityManager.find(Residente.class, "00000252274");
            Residente r2 = entityManager.find(Residente.class, "00000203020");

            Habitacion h1 = entityManager.createQuery(
                    "SELECT h FROM Habitacion h WHERE h.numero_habitacion = 101",
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
