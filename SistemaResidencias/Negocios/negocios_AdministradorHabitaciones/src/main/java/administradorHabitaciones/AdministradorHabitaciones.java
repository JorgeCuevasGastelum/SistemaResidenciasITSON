package administradorHabitaciones;

import dtos.HabitacionDTO;
import java.util.List;
import objetosnegocio.HabitacionBO;

public class AdministradorHabitaciones implements IAdministradorHabitaciones {

    private HabitacionBO habitacionesBO = HabitacionBO.getInstance();

    @Override
    public List<HabitacionDTO> obtenerHabitacionDisponibles() {
        return habitacionesBO.getHabitacionesDisponibles();
    }

    @Override
    public List<HabitacionDTO> obtenerHabitacionDisponiblesParaResidente(String id) {
        return habitacionesBO.getHabitacionesDisponiblesParaResidente(id);
    }

}
