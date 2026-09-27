package infraestructura.servicios;

import java.util.HashMap;
import java.util.Map;
import dtos.ResidenteDTO;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author abrilislas
 */
public class GeneradorJasper {
    
    private String DIRECCION_CONTRATO_ARRENDAMIENTO = "/reports/contrato_arrendamiento.jrxml";
    Map<String, Object> params = new HashMap<>();
    
    
    private void generarJasperReport(String direccionFile, String fileName) throws JRException{
        JasperReport report = JasperCompileManager.compileReport(
            getClass().getResourceAsStream(
                    DIRECCION_CONTRATO_ARRENDAMIENTO
                    )
        );

        JasperPrint print = JasperFillManager.fillReport(
            report,
            params,
            new JREmptyDataSource()
        );
        byte[] pdf = JasperExportManager.exportReportToPdf(print);
    }
    
    
    public void generarContratoArrendamientoResidente(ResidenteDTO residenteDTO){

        params.put("NOMBRE", residenteDTO.getNombreCompleto());
        params.put("ID", residenteDTO.getId());
        params.put("CARRERA", residenteDTO.getCarrera());
        params.put("",residenteDTO.getNombreAval());
        params.put("DIRECCION", residenteDTO.getDireccion());
        params.put("HABITACION", residenteDTO.getNumeroHabitacion());
    }  
}
