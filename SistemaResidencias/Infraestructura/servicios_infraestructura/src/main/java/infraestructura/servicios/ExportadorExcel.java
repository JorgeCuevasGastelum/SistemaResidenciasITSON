package infraestructura.servicios;

import dtos.AsignacionReporteDTO;
import enums.EstadoPagoENUM;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

public class ExportadorExcel {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public File exportarExcel(List<AsignacionReporteDTO> lista, String tituloReporte) throws Exception {
        File archivo = File.createTempFile("lista_asignacion_", ".xlsx");

        try (XSSFWorkbook wb = new XSSFWorkbook();
             FileOutputStream out = new FileOutputStream(archivo)) {

            XSSFSheet hoja = wb.createSheet("Asignaciones");

            // ── Estilos ───────────────────────────────────────────────────────
            XSSFCellStyle estTitulo  = estiloTitulo(wb);
            XSSFCellStyle estSubt    = estiloSubtitulo(wb);
            XSSFCellStyle estHeader  = estiloHeader(wb);
            XSSFCellStyle estCeldaA  = estiloCelda(wb, true);
            XSSFCellStyle estCeldaB  = estiloCelda(wb, false);
            XSSFCellStyle estAlC     = estiloChip(wb, new byte[]{40,  (byte)160, 80});
            XSSFCellStyle estDeuda   = estiloChip(wb, new byte[]{(byte)180, (byte)120, 10});
            XSSFCellStyle estMoroso  = estiloChip(wb, new byte[]{(byte)200, 40, 40});

            // ── Fila título ───────────────────────────────────────────────────
            Row rowTitulo = hoja.createRow(0);
            rowTitulo.setHeightInPoints(28);
            Cell cTitulo = rowTitulo.createCell(0);
            cTitulo.setCellValue(tituloReporte);
            cTitulo.setCellStyle(estTitulo);
            hoja.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

            // ── Fila subtítulo ────────────────────────────────────────────────
            Row rowSubt = hoja.createRow(1);
            rowSubt.setHeightInPoints(18);
            Cell cSubt = rowSubt.createCell(0);
            cSubt.setCellValue("Total: " + lista.size() + " asignaciones  |  Generado: "
                    + LocalDate.now().format(FMT));
            cSubt.setCellStyle(estSubt);
            hoja.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));

            // Fila vacía
            hoja.createRow(2).setHeightInPoints(8);

            // ── Fila de encabezados ───────────────────────────────────────────
            Row rowHeader = hoja.createRow(3);
            rowHeader.setHeightInPoints(22);
            String[] cols = {"Habitación", "Nombre completo", "Carrera", "Piso", "Estado de pago"};
            for (int i = 0; i < cols.length; i++) {
                Cell c = rowHeader.createCell(i);
                c.setCellValue(cols[i]);
                c.setCellStyle(estHeader);
            }

            // ── Filas de datos ────────────────────────────────────────────────
            int rowIdx = 4;
            for (AsignacionReporteDTO d : lista) {
                Row fila = hoja.createRow(rowIdx);
                fila.setHeightInPoints(20);
                boolean par = (rowIdx % 2 == 0);
                XSSFCellStyle base = par ? estCeldaA : estCeldaB;

                celda(fila, 0, d.getHabitacionTexto(), base);
                celda(fila, 1, d.getNombreCompleto().trim(), base);
                celda(fila, 2, d.getCarrera() != null ? d.getCarrera() : "—", base);
                celda(fila, 3, d.getPisoTexto(), base);

                // Estado de pago con color de fondo
                XSSFCellStyle estEstado = chipParaEstado(wb, d.getEstadoPago(), estAlC, estDeuda, estMoroso);
                celda(fila, 4, d.getEstadoPagoTexto(), estEstado);

                rowIdx++;
            }

            // ── Anchos de columna ─────────────────────────────────────────────
            int[] anchos = {14, 32, 28, 14, 18};
            for (int i = 0; i < anchos.length; i++) {
                hoja.setColumnWidth(i, anchos[i] * 256);
            }

            wb.write(out);
        }
        return archivo;
    }

    // ── Helpers de estilo ─────────────────────────────────────────────────────

    private XSSFCellStyle estiloTitulo(XSSFWorkbook wb) {
        XSSFCellStyle s = wb.createCellStyle();
        XSSFFont f = wb.createFont();
        f.setBold(true);
        f.setFontHeightInPoints((short) 16);
        f.setColor(new XSSFColor(new byte[]{55, 75, (byte)190}, null));
        s.setFont(f);
        s.setAlignment(HorizontalAlignment.LEFT);
        s.setVerticalAlignment(VerticalAlignment.CENTER);
        return s;
    }

    private XSSFCellStyle estiloSubtitulo(XSSFWorkbook wb) {
        XSSFCellStyle s = wb.createCellStyle();
        XSSFFont f = wb.createFont();
        f.setFontHeightInPoints((short) 10);
        f.setColor(new XSSFColor(new byte[]{(byte)110, (byte)110, (byte)120}, null));
        s.setFont(f);
        s.setAlignment(HorizontalAlignment.LEFT);
        s.setVerticalAlignment(VerticalAlignment.CENTER);
        return s;
    }

    private XSSFCellStyle estiloHeader(XSSFWorkbook wb) {
        XSSFCellStyle s = wb.createCellStyle();
        s.setFillForegroundColor(new XSSFColor(new byte[]{55, 75, (byte)190}, null));
        s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFFont f = wb.createFont();
        f.setBold(true);
        f.setColor(new XSSFColor(new byte[]{(byte)255, (byte)255, (byte)255}, null));
        f.setFontHeightInPoints((short) 11);
        s.setFont(f);
        s.setAlignment(HorizontalAlignment.CENTER);
        s.setVerticalAlignment(VerticalAlignment.CENTER);
        s.setBorderBottom(BorderStyle.THIN);
        s.setBottomBorderColor(new XSSFColor(new byte[]{55, 75, (byte)190}, null));
        return s;
    }

    private XSSFCellStyle estiloCelda(XSSFWorkbook wb, boolean alterna) {
        XSSFCellStyle s = wb.createCellStyle();
        if (alterna) {
            s.setFillForegroundColor(new XSSFColor(new byte[]{(byte)248, (byte)248, (byte)252}, null));
            s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        XSSFFont f = wb.createFont();
        f.setFontHeightInPoints((short) 10);
        s.setFont(f);
        s.setVerticalAlignment(VerticalAlignment.CENTER);
        s.setBorderBottom(BorderStyle.HAIR);
        s.setBottomBorderColor(new XSSFColor(new byte[]{(byte)220, (byte)218, (byte)235}, null));
        return s;
    }

    private XSSFCellStyle estiloChip(XSSFWorkbook wb, byte[] rgb) {
        XSSFCellStyle s = wb.createCellStyle();
        s.setFillForegroundColor(new XSSFColor(rgb, null));
        s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFFont f = wb.createFont();
        f.setBold(true);
        f.setFontHeightInPoints((short) 10);
        f.setColor(new XSSFColor(new byte[]{(byte)255, (byte)255, (byte)255}, null));
        s.setFont(f);
        s.setAlignment(HorizontalAlignment.CENTER);
        s.setVerticalAlignment(VerticalAlignment.CENTER);
        return s;
    }

    private XSSFCellStyle chipParaEstado(XSSFWorkbook wb, EstadoPagoENUM ep,
            XSSFCellStyle alC, XSSFCellStyle deuda, XSSFCellStyle moroso) {
        if (ep == null) return alC;
        return switch (ep) {
            case AL_CORRIENTE -> alC;
            case CON_DEUDA    -> deuda;
            case MOROSO       -> moroso;
        };
    }

    private void celda(Row fila, int col, String valor, CellStyle estilo) {
        Cell c = fila.createCell(col);
        c.setCellValue(valor != null ? valor : "—");
        c.setCellStyle(estilo);
    }
}
