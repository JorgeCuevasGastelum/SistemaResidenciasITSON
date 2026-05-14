package dtos;

import enums.EstadoPagoENUM;
import enums.GeneroENUM;
import java.time.LocalDate;

public class AsignacionReporteDTO {

    private Integer numeroHabitacion;
    private Integer piso;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String carrera;
    private GeneroENUM genero;
    private EstadoPagoENUM estadoPago;
    private LocalDate fechaIngreso;
    private String cicloLectivo;

    public AsignacionReporteDTO() {
    }

    public AsignacionReporteDTO(Integer numeroHabitacion, Integer piso,
            String nombre, String apellidoPaterno, String apellidoMaterno,
            String carrera, GeneroENUM genero, EstadoPagoENUM estadoPago,
            LocalDate fechaIngreso, String cicloLectivo) {
        this.numeroHabitacion = numeroHabitacion;
        this.piso = piso;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.carrera = carrera;
        this.genero = genero;
        this.estadoPago = estadoPago;
        this.fechaIngreso = fechaIngreso;
        this.cicloLectivo = cicloLectivo;
    }

    // ─── Getters & Setters ────────────────────────────────────────────────────

    public Integer getNumeroHabitacion() { return numeroHabitacion; }
    public void setNumeroHabitacion(Integer n) { this.numeroHabitacion = n; }

    public Integer getPiso() { return piso; }
    public void setPiso(Integer p) { this.piso = p; }

    public String getNombre() { return nombre; }
    public void setNombre(String n) { this.nombre = n; }

    public String getApellidoPaterno() { return apellidoPaterno; }
    public void setApellidoPaterno(String a) { this.apellidoPaterno = a; }

    public String getApellidoMaterno() { return apellidoMaterno; }
    public void setApellidoMaterno(String a) { this.apellidoMaterno = a; }

    public String getCarrera() { return carrera; }
    public void setCarrera(String c) { this.carrera = c; }

    public GeneroENUM getGenero() { return genero; }
    public void setGenero(GeneroENUM g) { this.genero = g; }

    public EstadoPagoENUM getEstadoPago() { return estadoPago; }
    public void setEstadoPago(EstadoPagoENUM e) { this.estadoPago = e; }

    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate f) { this.fechaIngreso = f; }

    public String getCicloLectivo() { return cicloLectivo; }
    public void setCicloLectivo(String c) { this.cicloLectivo = c; }

    // ─── Helper Methods ───────────────────────────────────────────────────────

    public String getNombreCompleto() {
        return (nombre != null ? nombre : "") + " "
                + (apellidoPaterno != null ? apellidoPaterno : "") + " "
                + (apellidoMaterno != null ? apellidoMaterno : "");
    }

    public String getHabitacionTexto() {
        return "Hab. " + (numeroHabitacion != null ? numeroHabitacion : "—");
    }

    public String getPisoTexto() {
        if (piso == null) return "—";
        return switch (piso) {
            case 1 -> "1er piso";
            case 2 -> "2do piso";
            case 3 -> "3er piso";
            default -> piso + "° piso";
        };
    }

    public String getEstadoPagoTexto() {
        if (estadoPago == null) return "—";
        return switch (estadoPago) {
            case AL_CORRIENTE -> "Al corriente";
            case CON_DEUDA -> "Con deuda";
            case MOROSO -> "Moroso";
        };
    }
}
