package entidades;

import enums.GeneroENUM;
import jakarta.persistence.*;
import java.util.Objects;
/**
 *
 * @author TRMrDucky
 */

@Entity
@Table(name = "habitaciones")
public class Habitacion {

    //Declaración de atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private Integer piso;

    @Column(nullable = false, unique = true)
    private Integer numero_habitacion;

    @Column(nullable = false)
    private Integer capacidad;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GeneroENUM genero;
    

    public Habitacion() {
    }

    public Habitacion(Long id, Integer numero_habitacion, Integer capacidad, GeneroENUM genero) {
        this.id = id;
        this.numero_habitacion = numero_habitacion;
        this.capacidad = capacidad;
        this.genero = genero;
        this.piso=piso;
    }

    public Habitacion(Long id, Integer piso, Integer numero_habitacion, Integer capacidad, GeneroENUM genero) {
        this(id, numero_habitacion, capacidad, genero);
        this.piso = piso;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.numero_habitacion);
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
        final Habitacion other = (Habitacion) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Habitacion{" + "id=" + id + ", numero_habitacion=" + numero_habitacion + ", capacidad=" + capacidad + ", genero=" + genero + '}';
    }

}
