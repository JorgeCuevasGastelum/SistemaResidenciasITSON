package implementaciones;

import entidades.ReferenciasPago;
import interfaz.IReferenciasPagoDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class ReferenciasPagoDAO implements IReferenciasPagoDAO {

    private final EntityManager entityManager;

    public ReferenciasPagoDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Long guardarReferencia(ReferenciasPago referencia) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.persist(referencia);
            tx.commit();
            return referencia.getId();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return null;
        }
    }
}
