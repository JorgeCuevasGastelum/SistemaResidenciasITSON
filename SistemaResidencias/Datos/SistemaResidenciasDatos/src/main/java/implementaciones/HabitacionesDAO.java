package implementaciones;

import dtos.HabitacionDTO;
import entidades.AsignacionHabitacion;
import entidades.Habitacion;
import entidades.Residente;
import enums.EstadoAsignacionENUM;
import enums.EstadoHabitacion;
import enums.EstadoHabitacionENUM;
import enums.EstadoResidenteENUM;
import enums.GeneroENUM;
import interfaz.IHabitacionesDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
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
            h.numero_habitacion,
            h.capacidad,
            h.genero
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
    h.numero_habitacion,
    h.capacidad,
    h.genero
)
FROM Habitacion h, Residente r
WHERE r.id = :residenteId
AND h.genero = r.genero

AND (
    SELECT COUNT(a)
    FROM AsignacionHabitacion a
    WHERE a.habitacion.id = h.id
    AND a.estadoHabitacion = :estadoActiva
) < h.capacidad

AND NOT EXISTS (
    SELECT a2
    FROM AsignacionHabitacion a2
    WHERE a2.residente.id = :residenteId
    AND a2.estadoHabitacion = :estadoActiva
)
""";

        TypedQuery<HabitacionDTO> query
                = entityManager.createQuery(jpql, HabitacionDTO.class);

        query.setParameter("residenteId", residenteId);
        query.setParameter("estadoActiva", EstadoHabitacion.ACTIVA);

        return query.getResultList();
    }

    @Override
    public void crearHabitacionesMock() {

        EntityTransaction tx = entityManager.getTransaction();

        try {

            tx.begin();

            Habitacion h1 = new Habitacion();
            h1.setNumero_habitacion(101);
            h1.setCapacidad(2);
            h1.setGenero(GeneroENUM.HOMBRE);

            Habitacion h2 = new Habitacion();
            h2.setNumero_habitacion(102);
            h2.setCapacidad(2);
            h2.setGenero(GeneroENUM.MUJER);

            Habitacion h3 = new Habitacion();
            h3.setNumero_habitacion(103);
            h3.setCapacidad(1);
            h3.setGenero(GeneroENUM.HOMBRE);

            Habitacion h4 = new Habitacion();
            h4.setNumero_habitacion(104);
            h4.setCapacidad(3);
            h4.setGenero(GeneroENUM.MUJER);

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
