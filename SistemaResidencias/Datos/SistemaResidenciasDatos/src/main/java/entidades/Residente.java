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

    // =========================================================
    // DOMICILIO
    // =========================================================

    @Column(length = 200, nullable = false)
    private String direccion;

    @Column(length = 100, nullable = false)
    private String ciudad;

    @Column(length = 100, nullable = false)
    private String estadoPais;

    @Column(length = 100, nullable = false)
    private String pais;

    // =========================================================
    // CONTACTO
    // =========================================================

    @Column(length = 150, nullable = false)
    private String correo;

    @Column(length = 15, nullable = false)
    private String telefono;

    // =========================================================
    // ESTADO DEL RESIDENTE
    // =========================================================

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoResidenteENUM estado;

    @Column(nullable = false)
    private Integer permiso_vehicular;

    @Column(length = 100, nullable = false)
    private String carrera;
    
    @Column(length = 100, nullable = false)
    private Boolean isDeportista;
        
    @Column(length = 100, nullable = false)
    private Boolean isIntercambio;

    // =========================================================
    // AVAL / FIADOR
    // =========================================================

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

    // =========================================================
    // VEHÍCULO
    // =========================================================

    @Column(length = 100)
    private String modeloVehiculo;

    @Column(length = 60)
    private String colorVehiculo;

    @Column(length = 20)
    private String placasVehiculo;

    // =========================================================
    // PLAN DE PAGO
    // =========================================================

    @Enumerated(EnumType.STRING)
    @Column
    private EstadoPagoENUM estadoPago;

    @Column
    private LocalDate ultimoPago;

    @Column
    private Double adeudoPendiente;

    // =========================================================
    // CONSTRUCTORES
    // =========================================================

    public Residente() {
    }

    /**
     * Constructor básico.
     */
    public Residente(
            String id,
            String nombre,
            String apellido_paterno,
            String apellido_materno,
            LocalDate fechaNacimiento,
            GeneroENUM genero,
            String direccion,
            String ciudad,
            String estadoPais,
            String pais,
            String correo,
            String telefono,
            EstadoResidenteENUM estado,
            Integer permiso_vehicular,
            String carrera, 
            Boolean isDeportista,
            Boolean isIntercambio
    ) {

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
        this.isDeportista = isDeportista;
        this.isIntercambio = isIntercambio;
    }

    /**
     * Constructor completo.
     * @param id
     * @param nombre
     */
    public Residente(
            String id,
            String nombre,
            String apellido_paterno,
            String apellido_materno,
            LocalDate fechaNacimiento,
            LocalDate fechaIngreso,
            GeneroENUM genero,
            String direccion,
            String ciudad,
            String estadoPais,
            String pais,
            String correo,
            String telefono,
            EstadoResidenteENUM estado,
            Integer permiso_vehicular,
            String carrera,
            String nombreAval,
            String parentescoAval,
            String telefonoAval,
            String correoAval,
            String direccionAval,
            String modeloVehiculo,
            String colorVehiculo,
            String placasVehiculo,
            EstadoPagoENUM estadoPago,
            LocalDate ultimoPago,
            Double adeudoPendiente,
            Boolean isDeportista,
            Boolean isIntercambio
    ) {

        this.id = id;
        this.nombre = nombre;
        this.apellido_paterno = apellido_paterno;
        this.apellido_materno = apellido_materno;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaIngreso = fechaIngreso;
        this.genero = genero;

        this.direccion = direccion;
        this.ciudad = ciudad;
        this.estadoPais = estadoPais;
        this.pais = pais;

        this.correo = correo;
        this.telefono = telefono;

        this.estado = estado;
        this.permiso_vehicular = permiso_vehicular;
        this.carrera = carrera;

        this.nombreAval = nombreAval;
        this.parentescoAval = parentescoAval;
        this.telefonoAval = telefonoAval;
        this.correoAval = correoAval;
        this.direccionAval = direccionAval;

        this.modeloVehiculo = modeloVehiculo;
        this.colorVehiculo = colorVehiculo;
        this.placasVehiculo = placasVehiculo;

        this.estadoPago = estadoPago;
        this.ultimoPago = ultimoPago;
        this.adeudoPendiente = adeudoPendiente;
        
        this.isDeportista = isDeportista;
        this.isIntercambio = isIntercambio;
    }

    // =========================================================
    // GETTERS / SETTERS
    // =========================================================

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public GeneroENUM getGenero() {
        return genero;
    }

    public void setGenero(GeneroENUM genero) {
        this.genero = genero;
    }

    // =========================================================
    // DOMICILIO
    // =========================================================

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstadoPais() {
        return estadoPais;
    }

    public void setEstadoPais(String estadoPais) {
        this.estadoPais = estadoPais;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    // =========================================================
    // CONTACTO
    // =========================================================

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

    // =========================================================
    // ESTADO DEL RESIDENTE
    // =========================================================

    public EstadoResidenteENUM getEstado() {
        return estado;
    }

    public void setEstado(EstadoResidenteENUM estado) {
        this.estado = estado;
    }

    public Integer getPermiso_vehicular() {
        return permiso_vehicular;
    }

    public void setPermiso_vehicular(Integer permiso_vehicular) {
        this.permiso_vehicular = permiso_vehicular;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }
    
    
    public Boolean IsDeportista() {
        return isDeportista;
    }

    public void setIsDeportista(Boolean isDeportista) {
        this.isDeportista = isDeportista;
    }

    public Boolean IsIntercambio() {
        return isIntercambio;
    }

    public void setIsIntercambio(Boolean isIntercambio) {
        this.isIntercambio = isIntercambio;
    }

    // =========================================================
    // AVAL / FIADOR
    // =========================================================

    public String getNombreAval() {
        return nombreAval;
    }

    public void setNombreAval(String nombreAval) {
        this.nombreAval = nombreAval;
    }

    public String getParentescoAval() {
        return parentescoAval;
    }

    public void setParentescoAval(String parentescoAval) {
        this.parentescoAval = parentescoAval;
    }

    public String getTelefonoAval() {
        return telefonoAval;
    }

    public void setTelefonoAval(String telefonoAval) {
        this.telefonoAval = telefonoAval;
    }

    public String getCorreoAval() {
        return correoAval;
    }

    public void setCorreoAval(String correoAval) {
        this.correoAval = correoAval;
    }

    public String getDireccionAval() {
        return direccionAval;
    }

    public void setDireccionAval(String direccionAval) {
        this.direccionAval = direccionAval;
    }

    // =========================================================
    // VEHÍCULO
    // =========================================================

    public String getModeloVehiculo() {
        return modeloVehiculo;
    }

    public void setModeloVehiculo(String modeloVehiculo) {
        this.modeloVehiculo = modeloVehiculo;
    }

    public String getColorVehiculo() {
        return colorVehiculo;
    }

    public void setColorVehiculo(String colorVehiculo) {
        this.colorVehiculo = colorVehiculo;
    }

    public String getPlacasVehiculo() {
        return placasVehiculo;
    }

    public void setPlacasVehiculo(String placasVehiculo) {
        this.placasVehiculo = placasVehiculo;
    }

    // =========================================================
    // PLAN DE PAGO
    // =========================================================

    public EstadoPagoENUM getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(EstadoPagoENUM estadoPago) {
        this.estadoPago = estadoPago;
    }

    public LocalDate getUltimoPago() {
        return ultimoPago;
    }

    public void setUltimoPago(LocalDate ultimoPago) {
        this.ultimoPago = ultimoPago;
    }

    public Double getAdeudoPendiente() {
        return adeudoPendiente;
    }

    public void setAdeudoPendiente(Double adeudoPendiente) {
        this.adeudoPendiente = adeudoPendiente;
    }

    // =========================================================
    // EQUALS / HASHCODE
    // =========================================================

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Residente other = (Residente) obj;

        return Objects.equals(this.id, other.id);
    }
    
    // =========================================================
    // TO STRING
    // =========================================================

    @Override
    public String toString() {
        return "Residente{"
                + "id=" + id
                + ", nombre=" + nombre
                + ", apellido_paterno=" + apellido_paterno
                + ", apellido_materno=" + apellido_materno
                + "}";
    }
}