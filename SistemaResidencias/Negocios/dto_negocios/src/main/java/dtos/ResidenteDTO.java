package dtos;

import enums.EstadoResidenteENUM;
import enums.GeneroENUM;
import java.time.LocalDate;

public class ResidenteDTO {

    private String id;
    private String nombre;
    private String apellido_paterno;
    private String apellido_materno;
    private LocalDate fechaNacimiento;
    private GeneroENUM genero;
    private String direccion;
    private String correo;
    private String telefono;
    private EstadoResidenteENUM estado;
    private Integer permiso_vehicular;
    private String carrera;

    public ResidenteDTO() {
    }

    
    /** RESIDENTE LISTADO **/
    public ResidenteDTO(String id, String nombre, String apellido_paterno, String apellido_materno, GeneroENUM genero, EstadoResidenteENUM estado, String carrera) {
        this.id = id;
        this.nombre = nombre;
        this.apellido_paterno = apellido_paterno;
        this.apellido_materno = apellido_materno;
        this.genero = genero;
        this.estado = estado;
        this.carrera = carrera;
    }

    
    /** RESIDENTE DETALLADO **/
    public ResidenteDTO(String id, String nombre, String apellido_paterno, String apellido_materno,
            LocalDate fechaNacimiento, GeneroENUM genero, String direccion,
            String correo, String telefono, EstadoResidenteENUM estado, Integer permiso_vehicular, String carrera) {
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
    
    
}
