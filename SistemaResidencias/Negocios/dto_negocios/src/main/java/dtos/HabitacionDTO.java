package dtos;

import enums.EstadoHabitacionENUM;
import enums.GeneroENUM;

public class HabitacionDTO {

    private Long id;
    private Integer numero_habitacion;
    private Integer capacidad;
    private GeneroENUM genero;
    private EstadoHabitacionENUM estado;

    public HabitacionDTO() {
    }

    public HabitacionDTO(Long id, Integer numero_habitacion, Integer capacidad, GeneroENUM genero, EstadoHabitacionENUM estado) {
        this.id = id;
        this.numero_habitacion = numero_habitacion;
        this.capacidad = capacidad;
        this.genero = genero;
        this.estado = estado;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumero_habitacion() {
        return numero_habitacion;
    }

    public void setNumero_habitacion(Integer numero_habitacion) {
        this.numero_habitacion = numero_habitacion;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public GeneroENUM getGenero() {
        return genero;
    }

    public void setGenero(GeneroENUM genero) {
        this.genero = genero;
    }

    public EstadoHabitacionENUM getEstado() {
        return estado;
    }

    public void setEstado(EstadoHabitacionENUM estado) {
        this.estado = estado;
    }
    
    
}
