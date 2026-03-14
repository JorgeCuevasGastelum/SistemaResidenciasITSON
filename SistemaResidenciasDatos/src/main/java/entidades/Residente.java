package entidades;

import enums.EstadoENUM;
import enums.GeneroENUM;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author TRMrDucky
 */
@Entity
@Table(name = "residentes")
public class Residente {

    //Declaración de atributos
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

    /**
     * Constructor vacío
     */
    public Residente() {
    }

    
    /**
     * Constructor completo
     * @param nombre Nombre del residente
     * @param apellido_paterno Apellido Paterno del residente
     * @param apellido_materno Apellido Materno del residente
     * @param fechaNacimiento Fecha de nacimeinto del residente
     * @param genero género del residente
     * @param direccion dirección del residente
     * @param correo Correo del residente
     * @param telefono Teléfono del residente
     * @param estado Estado del residente
     * @param permiso_vehicular  permiso vehícular del residente
     */
    public Residente(String nombre, String apellido_paterno, String apellido_materno, LocalDate fechaNacimiento, GeneroENUM genero, String direccion, String correo, String telefono, EstadoENUM estado, Integer permiso_vehicular) {
        this.id = id;
        this.nombre = nombre;
        this.apellido_paterno = apellido_paterno;
        this.apellido_materno = apellido_materno;
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
        this.direccion = direccion;
        this.correo = correo;
        this.telefono = telefono;
        this.estado = estado;
        this.permiso_vehicular = permiso_vehicular;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido_paterno() {
        return apellido_paterno;
    }

    public void setApellido_paterno(String apellido_paterno) {
        this.apellido_paterno = apellido_paterno;
    }

    public String getApellido_materno() {
        return apellido_materno;
    }

    public void setApellido_materno(String apellido_materno) {
        this.apellido_materno = apellido_materno;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public GeneroENUM getGenero() {
        return genero;
    }

    public void setGenero(GeneroENUM genero) {
        this.genero = genero;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public EstadoENUM getEstado() {
        return estado;
    }

    public void setEstado(EstadoENUM estado) {
        this.estado = estado;
    }

    public Integer getPermiso_vehicular() {
        return permiso_vehicular;
    }

    public void setPermiso_vehicular(Integer permiso_vehicular) {
        this.permiso_vehicular = permiso_vehicular;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + Objects.hashCode(this.id);
        hash = 31 * hash + Objects.hashCode(this.fechaNacimiento);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Residente other = (Residente) obj;
        return Objects.equals(this.id, other.id);
    }
}
