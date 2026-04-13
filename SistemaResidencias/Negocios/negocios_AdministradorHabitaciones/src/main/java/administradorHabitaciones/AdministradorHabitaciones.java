package administradorHabitaciones;

import dtos.HabitacionDTO;
import enums.GeneroENUM;
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

    @Override
    public List<HabitacionDTO> obtenerHabitacionDisponiblesPorGenero(GeneroENUM genero) {
        return habitacionesBO.obtenerHabitacionDisponiblesPorGenero(genero);
    }

    @Override
    public List<HabitacionDTO> obtenerHabitacionDisponiblesPorPiso(GeneroENUM genero, int piso) {
        return habitacionesBO.obtenerHabitacionDisponiblesPorPiso(genero, piso);
    }
}
