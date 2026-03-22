//package com.mycompany.sistemaresidenciasmain;
//
//import administradorAsignaciones.AdministradorAsignaciones;
//import administradorAsignaciones.IAdministradorAsignaciones;
//import administradorHabitaciones.AdministradorHabitaciones;
//import administradorHabitaciones.IAdministradorHabitaciones;
//import administradorResidentes.AdministradorResidentes;
//import administradorResidentes.IAdministradorResidentes;
//import dtos.HabitacionDTO;
//import dtos.ResidenteDTO;
//import java.util.List;
//
///**
// *
// * @author Gamer
// */
//public class SistemaResidenciasMain {
//
//    public static void main(String[] args) {
//
//        IAdministradorResidentes adminResidentes = new AdministradorResidentes();
//
//        //CREAR DATOS MOCK (CORRANLO SIEMPRE PORQUE AL TERMINAR LA PRUEBA SE BORRAN LOS DATOS)
//        adminResidentes.crearDatosMock();
//
//        List<ResidenteDTO> residentes = adminResidentes.obtenerResidentesActivos();
//
//        System.out.println("\n=== LISTADO DE RESIDENTES ACTIVOS ===\n");
//
//        for (ResidenteDTO r : residentes) {
//            System.out.println(
//                    r.getId() + " - "
//                    + r.getNombre() + " "
//                    + r.getApellido_paterno() + " "
//                    + r.getApellido_materno()
//                    + " | Genero: " + r.getGenero()
//                    + " | Carrera: " + r.getCarrera()
//            );
//        }
//
//        System.out.println("\n=== OBTENER RESIDENTE POR ID ===\n");
//        ResidenteDTO residente = adminResidentes.obtenerResidentePorId("00000252825");
//
//        if (residente != null) {
//            System.out.println("ID: " + residente.getId());
//            System.out.println("Nombre: " + residente.getNombre() + " " + residente.getApellido_materno() + " " + residente.getApellido_paterno());
//            System.out.println("Carrera: " + residente.getCarrera());
//            System.out.println("Estado: " + residente.getEstado());
//        } else {
//            System.out.println("no lo encontre :(");
//        }
//        System.out.println("\n====================================\n");
//
//        IAdministradorHabitaciones adminHabitaciones = new AdministradorHabitaciones();
//
//        System.out.println("\n=== OBTENER HABITACIONES DISPONIBLES ===\n");
//        List<HabitacionDTO> habitaciones = adminHabitaciones.obtenerHabitacionDisponibles();
//        // DEBE REGRESAR 102, 103 y 104
//
//        for (HabitacionDTO h : habitaciones) {
//            System.out.println(
//                    "Habitación: " + h.getNumero_habitacion()
//                    + " | Capacidad: " + h.getCapacidad()
//                    + " | Genero: " + h.getGenero()
//            );
//        }
//        System.out.println("\n====================================\n");
//
//        System.out.println("\n=== OBTENER HABITACIONES DISPONIBLES PARA RESIDENTE ===\n");
//
//        List<HabitacionDTO> habitacionesResidente = adminHabitaciones.obtenerHabitacionDisponiblesParaResidente("00000252825");
//        System.out.println("HABITACIONES DISPONIBLES PARA: " + adminResidentes.obtenerResidentePorId("00000252825").getNombre());
//        // DEBE REGRESAR LA 103 YA QUE EL RESIDENTE ES HOMBRE Y ES LA UNICA HABITACION DE HOMBRE QUE ESTA DISPONIBLE
//        // 00000252274 YA TIENE UNA HABITACION NO REGRESARA NINGUNA
//        // 00000203020 YA TIENE UNA HABITACION NO REGRESA NINGUNA
//        // 00000253017 REGRESA LA 102 Y 104 YA QUE SON LAS HABITACIONES DE MUJERES Y AMBAS ESTAN DISPONIBLES
//        // 00000249718 REGRESA LA 102 Y 104 YA QUE SON LAS HABITACIONES DE MUJERES Y AMBAS ESTAN DISPONIBLES
//
//        for (HabitacionDTO h : habitacionesResidente) {
//            System.out.println(
//                    "Habitación: " + h.getNumero_habitacion()
//                    + " | Capacidad: " + h.getCapacidad()
//                    + " | Genero: " + h.getGenero()
//            );
//        }
//        System.out.println("\n====================================\n");
//
//        System.out.println("\n=== ASIGNAR HABITACION ===\n");
//        IAdministradorAsignaciones adminAsignaciones = new AdministradorAsignaciones();
//        boolean resultado = adminAsignaciones.asignarHabitacion("00000252825", 103);
//
//        if (resultado) {
//            System.out.println("Habitación asignada correctamente");
//        } else {
//            System.out.println("No se pudo asignar la habitación");
//        }
//        System.out.println("\n====================================\n");
//
//        //COMO LA HABITACION SOLO TENIA CAPACIDAD DE 1 YA NO DEBE APARECER COMO DISPONIBLE 
//        System.out.println("\n=== OBTENER HABITACIONES DISPONIBLES ===\n");
//        List<HabitacionDTO> habitacionesDisponibles = adminHabitaciones.obtenerHabitacionDisponibles();
//        // DEBE REGRESAR 102 y 104
//        for (HabitacionDTO h : habitacionesDisponibles) {
//            System.out.println(
//                    "Habitación: " + h.getNumero_habitacion()
//                    + " | Capacidad: " + h.getCapacidad()
//                    + " | Genero: " + h.getGenero()
//            );
//        }
//        System.out.println("\n====================================\n");
//
//         // BORRAR LOS REGISTROS DE LA BASE DE DATOS
//         // quitenlo si quieren revisar los registros despues desde sql
//        adminResidentes.limpiarDatosMock();
//    }
//}

package com.mycompany.sistemaresidenciasmain;

import administradorAsignaciones.AdministradorAsignaciones;
import administradorAsignaciones.IAdministradorAsignaciones;
import administradorHabitaciones.AdministradorHabitaciones;
import administradorHabitaciones.IAdministradorHabitaciones;
import administradorResidentes.AdministradorResidentes;
import administradorResidentes.IAdministradorResidentes;
import javax.swing.SwingUtilities;
import presentacion.control.AsignarHabitacionesControl;
import presentacion.vistas.VistaMain;

public class SistemaResidenciasMain {

    public static void main(String[] args) {

        IAdministradorResidentes   adminResidentes   = new AdministradorResidentes();
        IAdministradorHabitaciones adminHabitaciones = new AdministradorHabitaciones();
        IAdministradorAsignaciones adminAsignaciones = new AdministradorAsignaciones();

        adminResidentes.crearDatosMock();

        SwingUtilities.invokeLater(() -> {

            VistaMain vista = new VistaMain();

            AsignarHabitacionesControl control = new AsignarHabitacionesControl(
                    adminResidentes, adminHabitaciones, adminAsignaciones);

            control.setVista(vista);
            vista.setControl(control);

            vista.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    adminResidentes.limpiarDatosMock();
                }
            });

            control.cargarResidentes();

            vista.setVisible(true);
        });
    }
}