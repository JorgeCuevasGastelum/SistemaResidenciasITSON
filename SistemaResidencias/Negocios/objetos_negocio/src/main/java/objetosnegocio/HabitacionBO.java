package objetosnegocio;

import dtos.HabitacionDTO;
import enums.GeneroENUM;
import implementaciones.AccesoDatos;
import interfaz.IAccesoDatos;
import java.util.List;

public class HabitacionBO {
     /**
     * Instancia singleton del objeto negocio de habitacion
     */
    private static HabitacionBO habitacionBO;

    /**
     * Devuelve la instancia única de {@code HabitacionBO}.
     *
     * @return la instancia única.
     */
    public static HabitacionBO getInstance() {
        if (habitacionBO == null) {
            habitacionBO = new HabitacionBO();
        }
        return habitacionBO;
    }
    
    public List<HabitacionDTO> getHabitacionesDisponibles() {
        IAccesoDatos accesoDatos = new AccesoDatos();
        return accesoDatos.obtenerHabitacionesDisponibles();

    }
    
    public List<HabitacionDTO> getHabitacionesDisponiblesParaResidente(String id) {
        IAccesoDatos accesoDatos = new AccesoDatos();
        return accesoDatos.obtenerHabitacionesDisponiblesParaResidente(id);

    }
    
    public List<HabitacionDTO> obtenerHabitacionDisponiblesPorGenero(GeneroENUM genero) {
        IAccesoDatos accesoDatos = new AccesoDatos();
        return accesoDatos.obtenerHabitacionDisponiblesPorGenero(genero);

    }
    
    public List<HabitacionDTO> obtenerHabitacionDisponiblesPorPiso(GeneroENUM genero, int piso) {
        IAccesoDatos accesoDatos = new AccesoDatos();
        return accesoDatos.obtenerHabitacionDisponiblesPorPiso(genero, piso);
    }
    
    
}
