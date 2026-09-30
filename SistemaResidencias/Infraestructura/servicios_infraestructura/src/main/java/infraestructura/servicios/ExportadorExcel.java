package infraestructura.servicios;

import dtos.AsignacionReporteDTO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExportadorExcel {

    private static final String RUTA_PLANTILLA = "/LISTA_ASIGNACION.xlsx";
    private static final String RUTA_LIBREOFFICE = "/opt/homebrew/bin/soffice";

    public File exportarExcel(
            List<AsignacionReporteDTO> lista,
            int cantidadDeportistas,
            int cantidadExtranjeros) throws Exception {

        File archivo = File.createTempFile(
                "ASIGNACION_AGOSTO_DICIEMBRE_2026_",
                ".xlsx"
        );

        try (
                InputStream input =
                        ExportadorExcel.class.getResourceAsStream(RUTA_PLANTILLA);
                XSSFWorkbook workbook =
                        input != null ? new XSSFWorkbook(input) : null
        ) {

            if (input == null) {
                throw new FileNotFoundException(
                        "No se encontró la plantilla: " + RUTA_PLANTILLA
                );
            }

            if (workbook == null) {
                throw new IllegalStateException(
                        "No se pudo abrir la plantilla de Excel."
                );
            }

            Sheet hoja = workbook.getSheet("ASIGNACION HABITACION");

            if (hoja == null) {
                throw new IllegalStateException(
                        "No se encontró la hoja 'ASIGNACION HABITACION'."
                );
            }

            llenarHabitaciones(hoja, lista);

            llenarContadores(
                    hoja,
                    cantidadDeportistas,
                    cantidadExtranjeros
            );

            workbook.setForceFormulaRecalculation(true);

            try (OutputStream output = Files.newOutputStream(archivo.toPath())) {
                workbook.write(output);
            }
        }

        return archivo;
    }
    
    private void llenarHabitaciones(Sheet hoja, List<AsignacionReporteDTO> lista) {
        Map<String, List<String>> residentesPorHabitacion = new HashMap<>();
        
        if (lista != null) {
            for (AsignacionReporteDTO asignacion : lista) {

                if (asignacion == null || asignacion.getNumeroHabitacion() == null) {
                    continue;
                }
                
                String numeroHabitacion = asignacion.getNumeroHabitacion().toString().trim();
                String nombre = asignacion.getNombreCompleto();

                if (nombre == null) {
                    continue;
                }

                nombre = nombre.trim();

                if (nombre.isEmpty()) {
                    continue;
                }

                // Evitamos agregar nombres duplicados exactamente iguales a la misma habitación
                residentesPorHabitacion
                        .computeIfAbsent(numeroHabitacion, k -> new ArrayList<>());
                
                List<String> residentes = residentesPorHabitacion.get(numeroHabitacion);
                if (!residentes.contains(nombre)) {
                    residentes.add(nombre);
                }
            }
        }

        for (int fila = 7; fila <= 81; fila++) {

            Row row = hoja.getRow(fila);

            if (row == null) {
                continue;
            }

            // Primer bloque (Izquierda)
            colocarResidente(
                    row,
                    1, // Columna de número de habitación izq (B)
                    2, // Columna de nombre izq (C)
                    residentesPorHabitacion
            );

            // Segundo bloque (Derecha)
            colocarResidente(
                    row,
                    3, // Columna de número de habitación der (D)
                    4, // Columna de nombre der (E)
                    residentesPorHabitacion
            );
        }
    }

    private void colocarResidente(
            Row row,
            int columnaHabitacion,
            int columnaNombre,
            Map<String, List<String>> residentesPorHabitacion) {

        Cell celdaHabitacion = row.getCell(columnaHabitacion);

        if (celdaHabitacion == null) {
            return;
        }

        String numeroHabitacion = obtenerNumeroHabitacion(celdaHabitacion);

        if (numeroHabitacion == null || numeroHabitacion.isEmpty()) {
            return;
        }

        List<String> residentes = residentesPorHabitacion.get(numeroHabitacion);

        Cell celdaNombre = row.getCell(columnaNombre);
        if (celdaNombre == null) {
            celdaNombre = row.createCell(columnaNombre);
        }

        // Si hay residentes disponibles para esta habitación, colocamos y removemos el primero.
        // Si ya no hay (ej. la habitación es individual o ya se colocaron todos), dejamos la celda en blanco.
        if (residentes != null && !residentes.isEmpty()) {
            String nombre = residentes.remove(0);
            celdaNombre.setCellValue(nombre);
        } else {
            celdaNombre.setCellValue(""); 
        }
    }

    private String obtenerNumeroHabitacion(Cell celda) {
        if (celda == null) {
            return null;
        }

        if (celda.getCellType() == CellType.NUMERIC) {
            return String.valueOf((int) celda.getNumericCellValue());
        }

        if (celda.getCellType() == CellType.STRING) {
            String valor = celda.getStringCellValue().trim();
            if (valor.isEmpty()) {
                return null;
            }
            return valor;
        }

        return null;
    }
     
    private void llenarContadores(
            Sheet hoja,
            int cantidadDeportistas,
            int cantidadExtranjeros) {

        Row filaDeportistas = hoja.getRow(84);
        if (filaDeportistas == null) {
            filaDeportistas = hoja.createRow(84);
        }

        Cell celdaDeportistas = filaDeportistas.getCell(1);
        if (celdaDeportistas == null) {
            celdaDeportistas = filaDeportistas.createCell(1);
        }
        celdaDeportistas.setCellValue(cantidadDeportistas);

        Row filaExtranjeros = hoja.getRow(85);
        if (filaExtranjeros == null) {
            filaExtranjeros = hoja.createRow(85);
        }

        Cell celdaExtranjeros = filaExtranjeros.getCell(1);
        if (celdaExtranjeros == null) {
            celdaExtranjeros = filaExtranjeros.createCell(1);
        }
        celdaExtranjeros.setCellValue(cantidadExtranjeros);
    }

    public File convertirExcelAPdf(File excel) throws Exception {

        Path directorioTemporal = Files.createTempDirectory("excel_pdf_");

        ProcessBuilder pb = new ProcessBuilder(
                RUTA_LIBREOFFICE,
                "--headless",
                "--convert-to",
                "pdf",
                "--outdir",
                directorioTemporal.toString(),
                excel.getAbsolutePath()
        );

        pb.redirectErrorStream(true);
        Process proceso = pb.start();

        String salida = new String(proceso.getInputStream().readAllBytes());
        int resultado = proceso.waitFor();

        if (resultado != 0) {
            throw new IllegalStateException(
                    "LibreOffice no pudo convertir el Excel a PDF.\n" + salida
            );
        }

        String nombreSinExtension = excel.getName().replaceFirst("(?i)\\.xlsx$", "");

        File pdf = directorioTemporal
                .resolve(nombreSinExtension + ".pdf")
                .toFile();

        if (!pdf.exists()) {
            throw new IllegalStateException(
                    "LibreOffice terminó correctamente, pero no se encontró el PDF generado.\n" + salida
            );
        }

        return pdf;
    }
}