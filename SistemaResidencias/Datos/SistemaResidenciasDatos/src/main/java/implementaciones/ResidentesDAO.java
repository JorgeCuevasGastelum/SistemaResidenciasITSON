package implementaciones;

import dtos.ResidenteDTO;
import entidades.Residente;
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

        //TEMPORAL NOMAS PARA PROBAR
        insertarDatosMock();
        //TEMPORAL NOMAS PARA PROBAR

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

        TypedQuery<ResidenteDTO> query
                = entityManager.createQuery(jpql, ResidenteDTO.class);

        query.setParameter("estado", EstadoResidenteENUM.ACTIVO);

        return query.getResultList();
    }

    public void insertarDatosMock() {

        EntityTransaction tx = entityManager.getTransaction();

        try {

            tx.begin();

            Residente r1 = new Residente();
            r1.setNombre("Jorge");
            r1.setApellido_paterno("Cuevas");
            r1.setApellido_materno("Gastelum");
            r1.setFechaNacimiento(LocalDate.of(2004, 10, 11));
            r1.setGenero(GeneroENUM.HOMBRE);
            r1.setDireccion("Calle 1");
            r1.setCorreo("jorge.cuevas@itson.edu.mx");
            r1.setTelefono("6441222916");
            r1.setEstado(EstadoResidenteENUM.ACTIVO);
            r1.setPermiso_vehicular(1);
            r1.setCarrera("Ing. Software");

            Residente r2 = new Residente();
            r2.setNombre("Joserra");
            r2.setApellido_paterno("Reynaga");
            r2.setApellido_materno("Nuñez");
            r2.setFechaNacimiento(LocalDate.of(2005, 8, 21));
            r2.setGenero(GeneroENUM.HOMBRE);
            r2.setDireccion("Calle 2");
            r2.setCorreo("joserra@itson.edu.mx");
            r2.setTelefono("6445551234");
            r2.setEstado(EstadoResidenteENUM.ACTIVO);
            r2.setPermiso_vehicular(2);
            r2.setCarrera("Ing. Software");

            Residente r3 = new Residente();
            r3.setNombre("Ari");
            r3.setApellido_paterno("Montoya");
            r3.setApellido_materno("Navarro");
            r3.setFechaNacimiento(LocalDate.of(2001, 11, 3));
            r3.setGenero(GeneroENUM.HOMBRE);
            r3.setDireccion("Calle 3");
            r3.setCorreo("ari@itson.edu.mx");
            r3.setTelefono("6447778888");
            r3.setEstado(EstadoResidenteENUM.INACTIVO);
            r3.setPermiso_vehicular(3);
            r3.setCarrera("Ing. Software");

            Residente r4 = new Residente();
            r4.setNombre("Abril");
            r4.setApellido_paterno("Reyes");
            r4.setApellido_materno("Islas");
            r4.setFechaNacimiento(LocalDate.of(2005, 11, 3));
            r4.setGenero(GeneroENUM.MUJER);
            r4.setDireccion("Calle 4");
            r4.setCorreo("abril@itson.edu.mx");
            r4.setTelefono("6447722888");
            r4.setEstado(EstadoResidenteENUM.ACTIVO);
            r4.setPermiso_vehicular(4);
            r4.setCarrera("Ing. Software");

            Residente r5 = new Residente();
            r5.setNombre("Melissa");
            r5.setApellido_paterno("Chavez");
            r5.setApellido_materno("Gutierrez");
            r5.setFechaNacimiento(LocalDate.of(2004, 11, 3));
            r5.setGenero(GeneroENUM.MUJER);
            r5.setDireccion("Calle 5");
            r5.setCorreo("melissa@itson.edu.mx");
            r5.setTelefono("6443378888");
            r5.setEstado(EstadoResidenteENUM.ACTIVO);
            r5.setPermiso_vehicular(5);
            r5.setCarrera("Ing. Software");

            entityManager.persist(r1);
            entityManager.persist(r2);
            entityManager.persist(r3);
            entityManager.persist(r4);
            entityManager.persist(r5);

            tx.commit();

            System.out.println("Residentes mock insertados correctamente");

        } catch (Exception e) {

            if (tx.isActive()) {
                tx.rollback();
            }

            e.printStackTrace();
        }
    }
}
