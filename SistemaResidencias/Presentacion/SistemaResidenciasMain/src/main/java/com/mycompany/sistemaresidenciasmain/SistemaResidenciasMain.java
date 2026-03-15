package com.mycompany.sistemaresidenciasmain;

import administradorHabitaciones.AdministradorHabitaciones;
import administradorHabitaciones.IAdministradorHabitaciones;
import administradorResidentes.AdministradorResidentes;
import administradorResidentes.IAdministradorResidentes;
import dtos.HabitacionDTO;
import dtos.ResidenteDTO;
import java.util.List;

/**
 *
 * @author Gamer
 */
public class SistemaResidenciasMain {

    public static void main(String[] args) {

        IAdministradorResidentes adminResidentes = new AdministradorResidentes();

        List<ResidenteDTO> residentes = adminResidentes.obtenerResidentesActivos();

        System.out.println("\n=== LISTADO DE RESIDENTES ACTIVOS ===\n");

        for (ResidenteDTO r : residentes) {
            System.out.println(
                    r.getId() + " - "
                    + r.getNombre() + " "
                    + r.getApellido_paterno() + " "
                    + r.getApellido_materno()
                    + " | Genero: " + r.getGenero()
                    + " | Carrera: " + r.getCarrera()
            );
        }

        System.out.println("\n=== OBTENER RESIDENTE POR ID ===\n");
        ResidenteDTO residente = adminResidentes.obtenerResidentePorId("00000252825");

        if (residente != null) {
            System.out.println("ID: " + residente.getId());
            System.out.println("Nombre: " + residente.getNombre() + " " + residente.getApellido_materno() + " " + residente.getApellido_paterno());
            System.out.println("Carrera: " + residente.getCarrera());
            System.out.println("Estado: " + residente.getEstado());
        } else {
            System.out.println("no lo encontre :(");
        }

        System.out.println("\n=== OBTENER HABITACIONES DISPONIBLES ===\n");
        IAdministradorHabitaciones adminHabitaciones = new AdministradorHabitaciones();

        List<HabitacionDTO> habitaciones = adminHabitaciones.obtenerHabitacionDisponibles();

        for (HabitacionDTO h : habitaciones) {
            System.out.println(
                    "Habitación: " + h.getNumero_habitacion()
                    + " | Capacidad: " + h.getCapacidad()
                    + " | Genero: " + h.getGenero()
            );
        }
    }
}
