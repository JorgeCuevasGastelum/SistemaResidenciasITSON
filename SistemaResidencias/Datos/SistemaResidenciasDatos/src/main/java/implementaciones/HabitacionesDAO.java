package implementaciones;

import dtos.HabitacionDTO;
import entidades.Habitacion;
import enums.EstadoHabitacion;
import enums.GeneroENUM;
import interfaz.IHabitacionesDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class HabitacionesDAO implements IHabitacionesDAO {

    private EntityManager entityManager;

    public HabitacionesDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<HabitacionDTO> obtenerHabitacionesDisponibles() {

        String jpql = """
        SELECT new dtos.HabitacionDTO(
            h.id,
            h.piso,
            h.numero_habitacion,
            h.capacidad,
            h.genero,
            (SELECT COUNT(a)
             FROM AsignacionHabitacion a
             WHERE a.habitacion = h
             AND a.estadoHabitacion = :estadoActiva)
        )
        FROM Habitacion h
        WHERE (
            SELECT COUNT(a)
            FROM AsignacionHabitacion a
            WHERE a.habitacion = h
            AND a.estadoHabitacion = :estadoActiva
        ) < h.capacidad
    """;

        TypedQuery<HabitacionDTO> query
                = entityManager.createQuery(jpql, HabitacionDTO.class);

        query.setParameter("estadoActiva", EstadoHabitacion.ACTIVA);

        return query.getResultList();
    }

    @Override
    public List<HabitacionDTO> obtenerHabitacionesDisponiblesParaResidente(String residenteId) {

        String jpql = """
SELECT new dtos.HabitacionDTO(
    h.id,
    h.piso,
    h.numero_habitacion,
    h.capacidad,
    h.genero,
    (SELECT COUNT(a)
     FROM AsignacionHabitacion a
     WHERE a.habitacion.id = h.id
     AND a.estadoHabitacion = :estadoActiva)
)
FROM Habitacion h, Residente r
WHERE r.id = :residenteId
AND h.genero = r.genero
""";

        TypedQuery<HabitacionDTO> query
                = entityManager.createQuery(jpql, HabitacionDTO.class);

        query.setParameter("residenteId", residenteId);
        query.setParameter("estadoActiva", EstadoHabitacion.ACTIVA);

        return query.getResultList();
    }

    @Override
    public List<HabitacionDTO> obtenerHabitacionDisponiblesPorGenero(GeneroENUM genero) {
        String jpql = """
                SELECT new dtos.HabitacionDTO(
                    h.id,
                    h.numero_habitacion,
                    h.capacidad,
                    h.genero
                )
                FROM Habitacion h
                WHERE h.genero = :genero
                AND (
                    SELECT COUNT(a)
                    FROM AsignacionHabitacion a
                    WHERE a.habitacion.id = h.id
                    AND a.estadoHabitacion = :estadoActiva
                ) < h.capacidad
                """;

        TypedQuery<HabitacionDTO> query
                = entityManager.createQuery(jpql, HabitacionDTO.class);

        query.setParameter("genero", genero);
        query.setParameter("estadoActiva", EstadoHabitacion.ACTIVA);

        return query.getResultList();
    }

    @Override
    public List<HabitacionDTO> obtenerHabitacionDisponiblesPorPiso(GeneroENUM genero, int piso) {
        String jpql = """
                SELECT new dtos.HabitacionDTO(
                    h.id,
                    h.numero_habitacion,
                    h.capacidad,
                    h.genero
                )
                FROM Habitacion h
                WHERE h.genero = :genero
                AND h.piso = :piso
                AND (
                    SELECT COUNT(a)
                    FROM AsignacionHabitacion a
                    WHERE a.habitacion.id = h.id
                    AND a.estadoHabitacion = :estadoActiva
                ) < h.capacidad
                """;

        TypedQuery<HabitacionDTO> query
                = entityManager.createQuery(jpql, HabitacionDTO.class);

        query.setParameter("genero", genero);
        query.setParameter("estadoActiva", EstadoHabitacion.ACTIVA);
        query.setParameter("piso", piso);

        return query.getResultList();
    }

    @Override
    public void crearHabitacionesMock() {

        EntityTransaction tx = entityManager.getTransaction();

        try {

            tx.begin();

            Habitacion h1 = new Habitacion();
            h1.setNumero_habitacion(1101);
            h1.setCapacidad(2);
            h1.setGenero(GeneroENUM.HOMBRE);
            h1.setPiso(1);

            Habitacion h2 = new Habitacion();
            h2.setNumero_habitacion(1102);
            h2.setCapacidad(2);
            h2.setGenero(GeneroENUM.MUJER);
            h2.setPiso(1);

            Habitacion h3 = new Habitacion();
            h3.setNumero_habitacion(1303);
            h3.setCapacidad(2);
            h3.setGenero(GeneroENUM.HOMBRE);
            h3.setPiso(3);

            Habitacion h4 = new Habitacion();
            h4.setNumero_habitacion(1204);
            h4.setCapacidad(2);
            h4.setGenero(GeneroENUM.MUJER);
            h4.setPiso(2);

            entityManager.persist(h1);
            entityManager.persist(h2);
            entityManager.persist(h3);
            entityManager.persist(h4);

            tx.commit();

            System.out.println("Habitaciones mock insertadas");

        } catch (Exception e) {

            if (tx.isActive()) {
                tx.rollback();
            }

            e.printStackTrace();
        }
    }

}
