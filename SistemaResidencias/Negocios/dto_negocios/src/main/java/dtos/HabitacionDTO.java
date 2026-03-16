package dtos;

import enums.GeneroENUM;

public class HabitacionDTO {

    private Long id;
    private Integer numero_habitacion;
    private Integer capacidad;
    private GeneroENUM genero;

    public HabitacionDTO() {
    }

    public HabitacionDTO(Long id, Integer numero_habitacion, Integer capacidad, GeneroENUM genero) {
        this.id = id;
        this.numero_habitacion = numero_habitacion;
        this.capacidad = capacidad;
        this.genero = genero;
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
     
}
