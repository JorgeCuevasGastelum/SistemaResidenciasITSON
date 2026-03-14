package conexion;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 *
 * @author TRMrDucky
 */
public class ManejadorConexiones {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("SistemaResidencias");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

}
