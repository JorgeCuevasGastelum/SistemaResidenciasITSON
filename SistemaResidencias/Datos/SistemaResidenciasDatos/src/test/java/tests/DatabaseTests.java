/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package tests;

import dtos.ResidenteDTO;
import entidades.Residente;
import enums.EstadoResidenteENUM;
import enums.GeneroENUM;
import implementaciones.ResidentesDAO;
import interfaz.IResidentesDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author TheRealMrDucky
 */
public class DatabaseTests {

    private EntityManagerFactory emf;
    private EntityManager em;
    private String dbName = "SistemaResidenciasTest";
    private IResidentesDAO residentesDAO;

    public DatabaseTests() {
    }

    @BeforeAll
    public static void setUpClass() {

    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        emf = Persistence.createEntityManagerFactory(dbName);
        em = emf.createEntityManager();
        residentesDAO = new ResidentesDAO(em);
    }

    @AfterEach
    public void tearDown() {
        em.close();
        emf.close();
    }

    @Test
    void testConexionDB() {
        assertNotNull(em);
    }

    @Test
    void addResidente() {
        Residente residente = new Residente(
                "1", "Juan", "Perez", "Lopez",
                LocalDate.of(2000, 5, 10),
                GeneroENUM.HOMBRE,
                "Calle 123",
                "juan@test.com",
                "6441234567",
                EstadoResidenteENUM.ACTIVO,
                1,
                "Ingenieria"
        );

        residentesDAO.guardarResidente(residente);

        Residente encontrado = em.find(Residente.class, "1");

        assertNotNull(encontrado);
        assertEquals("Juan", encontrado.getNombre());
    }

    @Test
    void deleteResidente() {
        Residente residente = new Residente(
                "123", "Juan", "Pérez", "López",
                LocalDate.of(2000, 1, 1),
                GeneroENUM.HOMBRE,
                "Calle Falsa 123",
                "juan@test.com",
                "6441234567",
                EstadoResidenteENUM.ACTIVO,
                1,
                "Ingeniería"
        );

        residentesDAO.guardarResidente(residente);

        residentesDAO.eliminarResidentePorId("123");

        em.clear();

        Residente eliminado = em.find(Residente.class, "123");

        assertNull(eliminado);
    }

    @Test
    void getAllResidentesActivos() {
        residentesDAO.crearResidentesMock();

        List<ResidenteDTO> lista = residentesDAO.obtenerListadoResidentesActivos();

        assertNotNull(lista);
        assertFalse(lista.isEmpty());

        for (ResidenteDTO r : lista) {
            assertEquals(EstadoResidenteENUM.ACTIVO, r.getEstado());
        }
    }

    @Test
    void getResidenteById() {
        Residente residente = new Residente(
                "123",
                "Juan",
                "Pérez",
                "López",
                LocalDate.of(2000, 1, 1),
                GeneroENUM.HOMBRE,
                "Calle Falsa 123",
                "juan@test.com",
                "6441234567",
                EstadoResidenteENUM.ACTIVO,
                1,
                "Ingeniería"
        );

        residentesDAO.guardarResidente(residente);

        ResidenteDTO encontrado = residentesDAO.obtenerResidentePorId("123");

        assertNotNull(encontrado);
    }

}
