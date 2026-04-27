package objetosnegocio;

import implementaciones.AccesoDatos;
import interfaz.IAccesoDatos;

public class AsignarHabitacionBO {
     /**
     * Instancia singleton del objeto negocio de AsignarHabitacionBO
     */
    private static AsignarHabitacionBO asignarHabitacionBO;

    /**
     * Devuelve la instancia única de {@code AsignarHabitacionBO}.
     *
     * @return la instancia única.
     */
    public static AsignarHabitacionBO getInstance() {
        if (asignarHabitacionBO == null) {
            asignarHabitacionBO = new AsignarHabitacionBO();
        }
        return asignarHabitacionBO;
    }
    
    public boolean asignarHabitacion(String residenteId, Integer numeroHabitacion) {
        IAccesoDatos accesoDatos = new AccesoDatos();
        return accesoDatos.asignarHabitacion(residenteId, numeroHabitacion);
    }

    public boolean tieneAsignacionActiva(String residenteId) {
        IAccesoDatos accesoDatos = new AccesoDatos();
        return accesoDatos.tieneAsignacionActiva(residenteId);
    }
}
