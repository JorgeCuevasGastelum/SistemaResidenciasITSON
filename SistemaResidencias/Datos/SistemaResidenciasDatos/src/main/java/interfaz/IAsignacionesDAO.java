package interfaz;

public interface IAsignacionesDAO {

    boolean asignarHabitacion(String idResidente, Integer numeroHabitacion);
    boolean tieneAsignacionActiva(String residenteId);
    void crearAsignacionesMock();
}
