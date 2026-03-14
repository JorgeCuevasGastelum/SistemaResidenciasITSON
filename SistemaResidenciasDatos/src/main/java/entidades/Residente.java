package entidades;

import enums.EstadoENUM;
import enums.GeneroENUM;
import jakarta.persistence.*;
import java.time.LocalDate;

/**
 *
 * @author TRMrDucky
 */
@Entity
@Table(name = "residentes")
public class Residente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String nombre;

    @Column(length = 100, nullable = false)
    private String apellido_paterno;

    @Column(length = 100, nullable = false)
    private String apellido_materno;

    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GeneroENUM genero;

    @Column(length = 200, nullable = false)
    private String direccion;

    @Column(length = 150, nullable = false)
    private String correo;

    @Column(length = 15, nullable = false)
    private String telefono;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoENUM estado;

    @Column(nullable = false)
    private Integer permiso_vehicular;

}
