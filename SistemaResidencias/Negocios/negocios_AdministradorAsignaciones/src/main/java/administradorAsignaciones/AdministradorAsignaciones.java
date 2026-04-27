package administradorAsignaciones;

import objetosnegocio.AsignarHabitacionBO;

public class AdministradorAsignaciones implements IAdministradorAsignaciones{
    private AsignarHabitacionBO asignacionesBO = AsignarHabitacionBO.getInstance();

    @Override
    public boolean asignarHabitacion(String residenteId, Integer numeroHabitacion) {
        return this.asignacionesBO.asignarHabitacion(residenteId, numeroHabitacion);
    }

    @Override
    public boolean tieneAsignacionActiva(String residenteId) {
        return this.asignacionesBO.tieneAsignacionActiva(residenteId);
    }
}
