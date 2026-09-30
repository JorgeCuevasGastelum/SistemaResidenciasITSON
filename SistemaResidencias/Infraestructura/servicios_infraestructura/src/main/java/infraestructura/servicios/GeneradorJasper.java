package infraestructura.servicios;

import dtos.HabitacionDTO;
import java.util.HashMap;
import java.util.Map;
import dtos.ResidenteDTO;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class GeneradorJasper {

    // PATHS DE LAS PLANTILLAS DE LOS REPORTES

    private String CONTRATO_CONTADO_ARRENDAMIENTO_PATH = "/reports/contrato_arrendamiento_contado.jrxml";
    private String CONTRATO_M3PAGOS_ARRENDAMIENTO_PATH = "/reports/contrato_arrendamiento_M3PAGOS.jrxml";
    private String CONTRATO_4PAGOS_ARRENDAMIENTO_PATH ="/reports/contrato_arrendamiento_4PAGOS.jrxml";
    private String MANTENIMIENTO_HABITACION_PATH = "/reports/contrato_arrendamiento.jrxml";
    private String QUEJAS_SUGERENCIAS_PATH = "/reports/contrato_arrendamiento.jrxml";


    // PATHS DONDE SE GUARDAN LOS ARCHIVOS
    // Ruta raíz
    private final Path BASE_PATH = Paths.get(
            System.getProperty("user.home"),
            "Documents",
            "Residencias"
    );

    private final Path CONTRATO_CONTADO_ARRENDAMIENTO_SAVING_PATH = BASE_PATH.resolve("Contratos").resolve("Contado");

    private String CONTRATO_M3PAGOS_ARRENDAMIENTO_SAVING_PATH = "";
    private String CONTRATO_4PAGOS_ARRENDAMIENTO_SAVING_PATH = "";
    private String LISTA_ASIGNACION_SAVING_PATH = "";
    private String MANTENIMIENTO_HABITACION_SAVING_PATH = "";
    private String QUEJAS_SUGERENCIAS_SAVING_PATH = "";


    private void generarJasperReport(
            String direccionFile,
            Path savingPath,
            String filename,
            Map<String, Object> params) throws JRException {

        try {

            Files.createDirectories(savingPath);
            JasperReport report = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream(direccionFile)
            );
            
            JasperPrint print = JasperFillManager.fillReport(
                    report,
                    params,
                    new JREmptyDataSource()
            );

            Path outputFile = savingPath.resolve(filename + ".pdf");
            JasperExportManager.exportReportToPdfFile(
                    print,
                    outputFile.toString()
            );

        } catch (Exception e) {
            throw new JRException("Hubo un error al generar el PDF", e);
        }
    }


    public void generarContratoArrendamientoResidente(
            ResidenteDTO residenteDTO,
            TipoDocumentos tipoContrato) {

        Map<String, Object> params = new HashMap<>();

        params.put("NOMBRE", residenteDTO.getNombreCompleto());
        params.put("ID", residenteDTO.getId());
        params.put("CARRERA", residenteDTO.getCarrera());
        params.put("FIADOR", residenteDTO.getNombreAval());
        params.put("DIRECCION", residenteDTO.getDireccion());
        params.put("CIUDAD", residenteDTO.getCiudad());
        params.put("ESTADO", residenteDTO.getEstadoPais());
        params.put("PAIS", residenteDTO.getPais());
        params.put("HABITACION", residenteDTO.getNumeroHabitacion());
        
        HabitacionDTO habitacion = new HabitacionDTO();
        params.put("PISO",habitacion.getPiso());
        if(habitacion.getGenero().equals(habitacion.getGenero().MUJER)){
             params.put("ALA", "IZQUIERDA");
        }else{
            params.put("ALA", "DERECHA");
        }

        String fileName = "Contrato_"
                + residenteDTO.getId()
                + "_"
                + residenteDTO.getNombreCompleto();

        switch (tipoContrato) {
            case ARRENDAMIENTO_CONTADO:
                try {
                    generarJasperReport(
                            CONTRATO_CONTADO_ARRENDAMIENTO_PATH,
                            CONTRATO_CONTADO_ARRENDAMIENTO_SAVING_PATH,
                            fileName,
                            params
                    );
                } catch (JRException e) {
                    lanzarError(
                            "No se pudo generar el contrato:\n"
                            + e.getMessage()
                    );
                }
                break;
            
            case ARRENDAMIENTO_MY3PAGOS:
                try {
                    generarJasperReport(
                            CONTRATO_CONTADO_ARRENDAMIENTO_PATH,
                            CONTRATO_CONTADO_ARRENDAMIENTO_SAVING_PATH,
                            fileName,
                            params
                    );
                } catch (JRException e) {
                    lanzarError(
                            "No se pudo generar el contrato:\n"
                            + e.getMessage()
                    );
                }
                break;
            
        }
    }
    
    
    private void lanzarError(String mensaje) {
        JOptionPane.showMessageDialog(
                null,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}