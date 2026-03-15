package implementaciones;

import dtos.HabitacionDTO;
import entidades.Habitacion;
import enums.EstadoHabitacionENUM;
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

        //TEMPORAL NOMAS PARA PROBAR
        insertarHabitacionesMock();
        //TEMPORAL NOMAS PARA PROBAR
        
        String jpql = """
        SELECT new dtos.HabitacionDTO(
            h.id,
            h.numero_habitacion,
            h.capacidad,
            h.genero,
            h.estado
        )
        FROM Habitacion h
        WHERE h.estado = :estado
    """;

        TypedQuery<HabitacionDTO> query
                = entityManager.createQuery(jpql, HabitacionDTO.class);

        query.setParameter("estado", EstadoHabitacionENUM.DISPONIBLE);

        return query.getResultList();
    }

    public void insertarHabitacionesMock() {

        EntityTransaction tx = entityManager.getTransaction();

        try {

            tx.begin();

            Habitacion h1 = new Habitacion();
            h1.setNumero_habitacion(101);
            h1.setCapacidad(2);
            h1.setGenero(GeneroENUM.HOMBRE);
            h1.setEstado(EstadoHabitacionENUM.DISPONIBLE);

            Habitacion h2 = new Habitacion();
            h2.setNumero_habitacion(102);
            h2.setCapacidad(2);
            h2.setGenero(GeneroENUM.MUJER);
            h2.setEstado(EstadoHabitacionENUM.DISPONIBLE);

            Habitacion h3 = new Habitacion();
            h3.setNumero_habitacion(103);
            h3.setCapacidad(1);
            h3.setGenero(GeneroENUM.HOMBRE);
            h3.setEstado(EstadoHabitacionENUM.OCUPADA);

            Habitacion h4 = new Habitacion();
            h4.setNumero_habitacion(104);
            h4.setCapacidad(3);
            h4.setGenero(GeneroENUM.MUJER);
            h4.setEstado(EstadoHabitacionENUM.DISPONIBLE);

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
