package entidades;

import enums.EstadoPagoENUM;
import enums.EstadoResidenteENUM;
import enums.GeneroENUM;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "residentes")
public class Residente {

    @Id
    @Column(length = 20, nullable = false)
    private String id;

    @Column(length = 100, nullable = false)
    private String nombre;

    @Column(length = 100, nullable = false)
    private String apellido_paterno;

    @Column(length = 100, nullable = false)
    private String apellido_materno;

    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    @Column(nullable = true)
    private LocalDate fechaIngreso;

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
    private EstadoResidenteENUM estado;

    @Column(nullable = false)
    private Integer permiso_vehicular;

    @Column(length = 100, nullable = false)
    private String carrera;

    // Aval / Fiador
    @Column(length = 150)
    private String nombreAval;

    @Column(length = 100)
    private String parentescoAval;

    @Column(length = 15)
    private String telefonoAval;

    @Column(length = 150)
    private String correoAval;

    @Column(length = 250)
    private String direccionAval;

    // Permiso Vehicular
    @Column(length = 100)
    private String modeloVehiculo;

    @Column(length = 60)
    private String colorVehiculo;

    @Column(length = 20)
    private String placasVehiculo;

    // Plan de pago
    @Enumerated(EnumType.STRING)
    @Column
    private EstadoPagoENUM estadoPago;

    @Column
    private LocalDate ultimoPago;

    @Column
    private Double adeudoPendiente;

    public Residente() {
    }

    public Residente(String id, String nombre, String apellido_paterno, String apellido_materno,
            LocalDate fechaNacimiento, GeneroENUM genero, String direccion, String correo,
            String telefono, EstadoResidenteENUM estado, Integer permiso_vehicular, String carrera) {
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
        this.carrera = carrera;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido_paterno() { return apellido_paterno; }
    public void setApellido_paterno(String apellido_paterno) { this.apellido_paterno = apellido_paterno; }

    public String getApellido_materno() { return apellido_materno; }
    public void setApellido_materno(String apellido_materno) { this.apellido_materno = apellido_materno; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public GeneroENUM getGenero() { return genero; }
    public void setGenero(GeneroENUM genero) { this.genero = genero; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public EstadoResidenteENUM getEstado() { return estado; }
    public void setEstado(EstadoResidenteENUM estado) { this.estado = estado; }

    public Integer getPermiso_vehicular() { return permiso_vehicular; }
    public void setPermiso_vehicular(Integer permiso_vehicular) { this.permiso_vehicular = permiso_vehicular; }

    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }

    public String getNombreAval() { return nombreAval; }
    public void setNombreAval(String nombreAval) { this.nombreAval = nombreAval; }

    public String getParentescoAval() { return parentescoAval; }
    public void setParentescoAval(String parentescoAval) { this.parentescoAval = parentescoAval; }

    public String getTelefonoAval() { return telefonoAval; }
    public void setTelefonoAval(String telefonoAval) { this.telefonoAval = telefonoAval; }

    public String getCorreoAval() { return correoAval; }
    public void setCorreoAval(String correoAval) { this.correoAval = correoAval; }

    public String getDireccionAval() { return direccionAval; }
    public void setDireccionAval(String direccionAval) { this.direccionAval = direccionAval; }

    public String getModeloVehiculo() { return modeloVehiculo; }
    public void setModeloVehiculo(String modeloVehiculo) { this.modeloVehiculo = modeloVehiculo; }

    public String getColorVehiculo() { return colorVehiculo; }
    public void setColorVehiculo(String colorVehiculo) { this.colorVehiculo = colorVehiculo; }

    public String getPlacasVehiculo() { return placasVehiculo; }
    public void setPlacasVehiculo(String placasVehiculo) { this.placasVehiculo = placasVehiculo; }

    public EstadoPagoENUM getEstadoPago() { return estadoPago; }
    public void setEstadoPago(EstadoPagoENUM estadoPago) { this.estadoPago = estadoPago; }

    public LocalDate getUltimoPago() { return ultimoPago; }
    public void setUltimoPago(LocalDate ultimoPago) { this.ultimoPago = ultimoPago; }

    public Double getAdeudoPendiente() { return adeudoPendiente; }
    public void setAdeudoPendiente(Double adeudoPendiente) { this.adeudoPendiente = adeudoPendiente; }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + Objects.hashCode(this.id);
        hash = 31 * hash + Objects.hashCode(this.fechaNacimiento);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Residente other = (Residente) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Residente{id=" + id + ", nombre=" + nombre + "}";
    }
}
