package entidades;

import enums.EstadoHabitacion;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author TRMrDucky
 */
@Entity
@Table(name = "asignacion_habitaciones")
public class AsignacionHabitacion {

    //Declaración de atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "residente_id", nullable = false)
    private Residente residente;

    @ManyToOne
    @JoinColumn(name = "habitacion_id", nullable = false)
    private Habitacion habitacion;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private LocalDate fechaFin;
    
    @Column(length = 20, nullable = false)
    private String cicloLectivo;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoHabitacion estadoHabitacion;

    public AsignacionHabitacion() {
    }

    public AsignacionHabitacion(Long id, Residente residente, Habitacion habitacion, LocalDate fechaInicio, LocalDate fechaFin, String cicloLectivo, EstadoHabitacion estadoHabitacion) {
        this.id = id;
        this.residente = residente;
        this.habitacion = habitacion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cicloLectivo = cicloLectivo;
        this.estadoHabitacion = estadoHabitacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Residente getResidente() {
        return residente;
    }

    public void setResidente(Residente residente) {
        this.residente = residente;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getCicloLectivo() {
        return cicloLectivo;
    }

    public void setCicloLectivo(String cicloLectivo) {
        this.cicloLectivo = cicloLectivo;
    }

    public EstadoHabitacion getEstadoHabitacion() {
        return estadoHabitacion;
    }

    public void setEstadoHabitacion(EstadoHabitacion estadoHabitacion) {
        this.estadoHabitacion = estadoHabitacion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
        hash = 67 * hash + Objects.hashCode(this.residente);
        hash = 67 * hash + Objects.hashCode(this.habitacion);
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
        final AsignacionHabitacion other = (AsignacionHabitacion) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "AsignacionHabitacion{" + "id=" + id + ", residente=" + residente + ", habitacion=" + habitacion + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + ", cicloLectivo=" + cicloLectivo + ", estadoHabitacion=" + estadoHabitacion + '}';
    }
    
    
}
