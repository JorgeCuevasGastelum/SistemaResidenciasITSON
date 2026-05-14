package objetosnegocio;

import dtos.AsignacionHabitacionDTO;
import dtos.AsignacionReporteDTO;
import implementaciones.AccesoDatos;
import interfaz.IAccesoDatos;
import java.util.List;

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

    public List<AsignacionReporteDTO> obtenerListaAsignaciones() {
        IAccesoDatos accesoDatos = new AccesoDatos();
        return accesoDatos.obtenerListaAsignaciones();
    }

    public int contarHabitacionesOcupadas() {
        IAccesoDatos accesoDatos = new AccesoDatos();
        return accesoDatos.contarHabitacionesOcupadas();
    }

    public int contarResidentesAsignados() {
        IAccesoDatos accesoDatos = new AccesoDatos();
        return accesoDatos.contarResidentesAsignados();
    }
}
