package objetosnegocio;

import dtos.AsignacionHabitacionDTO;
import implementaciones.AccesoDatos;
import interfaz.IAccesoDatos;

public class AsignarHabitacionBO {

    private static AsignarHabitacionBO asignarHabitacionBO;

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

    public AsignacionHabitacionDTO obtenerAsignacionActiva(String residenteId) {
        IAccesoDatos accesoDatos = new AccesoDatos();
        return accesoDatos.obtenerAsignacionActiva(residenteId);
    }
}
