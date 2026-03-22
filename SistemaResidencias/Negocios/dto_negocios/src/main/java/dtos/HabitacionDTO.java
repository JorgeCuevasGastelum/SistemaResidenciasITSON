package dtos;

import enums.GeneroENUM;

public class HabitacionDTO {

    private Long id;
    private Integer piso;             
    private Integer numero_habitacion;
    private Integer capacidad;
    private GeneroENUM genero;

    private Integer ocupacionActual;

    public HabitacionDTO() {
    }

    public HabitacionDTO(Long id,
            Integer numero_habitacion,
            Integer capacidad,
            GeneroENUM genero) {
        this.id = id;
        this.numero_habitacion = numero_habitacion;
        this.capacidad = capacidad;
        this.genero = genero;
        this.ocupacionActual = 0;
    }

    public HabitacionDTO(Long id,
            Integer piso,
            Integer numero_habitacion,
            Integer capacidad,
            GeneroENUM genero) {
        this(id, numero_habitacion, capacidad, genero);
        this.piso = piso;
    }

    public HabitacionDTO(Long id,
            Integer piso,
            Integer numero_habitacion,
            Integer capacidad,
            GeneroENUM genero,
            Integer ocupacionActual) {
        this(id, piso, numero_habitacion, capacidad, genero);
        this.ocupacionActual = ocupacionActual;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPiso() {
        return piso;
    }

    public void setPiso(Integer piso) {
        this.piso = piso;
    }

    public Integer getNumero_habitacion() {
        return numero_habitacion;
    }

    public void setNumero_habitacion(Integer n) {
        this.numero_habitacion = n;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer c) {
        this.capacidad = c;
    }

    public GeneroENUM getGenero() {
        return genero;
    }

    public void setGenero(GeneroENUM g) {
        this.genero = g;
    }

    public Integer getOcupacionActual() {
        return ocupacionActual != null ? ocupacionActual : 0;
    }

    public void setOcupacionActual(Integer o) {
        this.ocupacionActual = o;
    }

    public int getLugaresDisponibles() {
        return capacidad - getOcupacionActual();
    }

    public boolean tieneCapacidad() {
        return getLugaresDisponibles() > 0;
    }
}
