package dtos;

import enums.EstadoAsignacionENUM;
import java.time.LocalDate;

public class AsignacionHabitacionDTO {

    private Integer id;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private EstadoAsignacionENUM estado;
    private String cicloLectivo;

    private Long idHabitacion;
    private Integer numeroHabitacion;
    private String idResidente;
    private String nombreResidente;

    public AsignacionHabitacionDTO() {
    }

    public AsignacionHabitacionDTO(LocalDate fechaInicio,
            LocalDate fechaFin,
            EstadoAsignacionENUM estado,
            String cicloLectivo,
            Long idHabitacion,
            String idResidente) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.cicloLectivo = cicloLectivo;
        this.idHabitacion = idHabitacion;
        this.idResidente = idResidente;
    }

    public AsignacionHabitacionDTO(Integer id,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            EstadoAsignacionENUM estado,
            String cicloLectivo,
            Long idHabitacion,
            Integer numeroHabitacion,
            String idResidente,
            String nombreResidente) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.cicloLectivo = cicloLectivo;
        this.idHabitacion = idHabitacion;
        this.numeroHabitacion = numeroHabitacion;
        this.idResidente = idResidente;
        this.nombreResidente = nombreResidente;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate d) {
        this.fechaInicio = d;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate d) {
        this.fechaFin = d;
    }

    public EstadoAsignacionENUM getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsignacionENUM e) {
        this.estado = e;
    }

    public String getCicloLectivo() {
        return cicloLectivo;
    }

    public void setCicloLectivo(String c) {
        this.cicloLectivo = c;
    }

    public Long getIdHabitacion() {
        return idHabitacion;
    }

    public void setIdHabitacion(Long id) {
        this.idHabitacion = id;
    }

    public Integer getNumeroHabitacion() {
        return numeroHabitacion;
    }

    public void setNumeroHabitacion(Integer n) {
        this.numeroHabitacion = n;
    }

    public String getIdResidente() {
        return idResidente;
    }

    public void setIdResidente(String id) {
        this.idResidente = id;
    }

    public String getNombreResidente() {
        return nombreResidente;
    }

    public void setNombreResidente(String n) {
        this.nombreResidente = n;
    }
}
