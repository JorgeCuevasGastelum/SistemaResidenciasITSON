package com.mycompany.sistemaresidenciasmain;

import administradorResidentes.AdministradorResidentes;
import administradorResidentes.IAdministradorResidentes;
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

    System.out.println("=== LISTADO DE RESIDENTES ACTIVOS ===");

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
}
}
