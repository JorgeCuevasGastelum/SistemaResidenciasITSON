package infraestructura.servicios;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import dtos.AsignacionReporteDTO;
import enums.EstadoPagoENUM;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GeneradorListaPdf {

    private static final Color AZUL_PRIMARIO  = new Color(55,  75, 190);
    private static final Color AZUL_CLARO     = new Color(235, 238, 255);
    private static final Color VERDE          = new Color(40,  160,  80);
    private static final Color VERDE_CLARO    = new Color(235, 255, 242);
    private static final Color AMBAR          = new Color(180, 120,  10);
    private static final Color AMBAR_CLARO    = new Color(255, 248, 220);
    private static final Color ROJO           = new Color(200,  40,  40);
    private static final Color ROJO_CLARO     = new Color(255, 235, 235);
    private static final Color GRIS_TEXTO     = new Color(110, 110, 120);
    private static final Color GRIS_FONDO     = new Color(248, 248, 252);
    private static final Color GRIS_LINEA     = new Color(220, 218, 235);

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public File generarPdf(List<AsignacionReporteDTO> lista, String titulo) throws Exception {
        File archivo = File.createTempFile("lista_asignacion_", ".pdf");

        Document doc = new Document(PageSize.A4.rotate(), 36, 36, 36, 50);
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(archivo));

        // Footer con número de página
        writer.setPageEvent(new PdfPageEventHelper() {
            @Override
            public void onEndPage(PdfWriter w, Document d) {
                PdfContentByte cb = w.getDirectContent();
                Font f = new Font(Font.HELVETICA, 8, Font.NORMAL, GRIS_TEXTO);
                Phrase ph = new Phrase("Página " + w.getPageNumber() + "  —  "
                        + "Generado el " + LocalDate.now().format(FMT), f);
                ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, ph,
                        (d.left() + d.right()) / 2, d.bottom() - 18, 0);
            }
        });

        doc.open();

        Font fInstitucion = new Font(Font.HELVETICA,  8, Font.NORMAL, GRIS_TEXTO);
        Font fTitulo      = new Font(Font.HELVETICA, 16, Font.BOLD,   Color.BLACK);
        Font fSubtitulo   = new Font(Font.HELVETICA,  9, Font.NORMAL, GRIS_TEXTO);
        Font fColHeader   = new Font(Font.HELVETICA,  9, Font.BOLD,   Color.WHITE);
        Font fCelda       = new Font(Font.HELVETICA, 10, Font.NORMAL, new Color(40, 40, 50));
        Font fChip        = new Font(Font.HELVETICA,  9, Font.BOLD,   Color.WHITE);

        // ── Encabezado institucional ──────────────────────────────────────────
        PdfPTable header = new PdfPTable(2);
        header.setWidthPercentage(100);
        header.setWidths(new float[]{4f, 1f});
        header.setSpacingAfter(14);

        Paragraph textoInst = new Paragraph(
                "Instituto Tecnológico de Sonora\n"
                + "Universidad Pública — Unidad Obregón, Campus Náinari.\n"
                + "Antonio Caso 2266, Villa ITSON, C.P. 85130.", fInstitucion);
        PdfPCell celdaTexto = new PdfPCell(textoInst);
        celdaTexto.setBorder(Rectangle.NO_BORDER);
        celdaTexto.setVerticalAlignment(Element.ALIGN_MIDDLE);
        header.addCell(celdaTexto);

        java.io.InputStream isLogo =
                getClass().getClassLoader().getResourceAsStream("LogoResidencias.png");
        PdfPCell celdaLogo = new PdfPCell();
        celdaLogo.setBorder(Rectangle.NO_BORDER);
        celdaLogo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celdaLogo.setVerticalAlignment(Element.ALIGN_MIDDLE);
        if (isLogo != null) {
            Image logo = Image.getInstance(isLogo.readAllBytes());
            logo.scaleToFit(90, 45);
            celdaLogo.setImage(logo);
        }
        header.addCell(celdaLogo);
        doc.add(header);

        // Línea divisora
        PdfPTable linea = new PdfPTable(1);
        linea.setWidthPercentage(100);
        linea.setSpacingAfter(10);
        PdfPCell lineaCell = new PdfPCell();
        lineaCell.setBackgroundColor(AZUL_PRIMARIO);
        lineaCell.setFixedHeight(2f);
        lineaCell.setBorder(Rectangle.NO_BORDER);
        linea.addCell(lineaCell);
        doc.add(linea);

        // Título y fecha
        doc.add(new Paragraph(titulo, fTitulo));
        doc.add(new Paragraph("Total de asignaciones activas: " + lista.size()
                + "   |   Fecha de generación: " + LocalDate.now().format(FMT), fSubtitulo));
        doc.add(new Paragraph(" "));

        // ── Tabla de datos ────────────────────────────────────────────────────
        PdfPTable tabla = new PdfPTable(5);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{1.2f, 2.8f, 2.4f, 1.2f, 1.6f});

        String[] cols = {"Habitación", "Nombre completo", "Carrera", "Piso", "Estado de pago"};
        for (String col : cols) {
            PdfPCell c = new PdfPCell(new Phrase(col, fColHeader));
            c.setBackgroundColor(AZUL_PRIMARIO);
            c.setPadding(8f);
            c.setBorderColor(AZUL_PRIMARIO);
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(c);
        }

        boolean fila = false;
        for (AsignacionReporteDTO d : lista) {
            Color bg = fila ? GRIS_FONDO : Color.WHITE;

            PdfPCell cHab = chipCell(d.getHabitacionTexto(), AZUL_PRIMARIO, bg, fChip, fCelda);
            cHab.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(cHab);

            tabla.addCell(celda(d.getNombreCompleto().trim(), fCelda, bg));
            tabla.addCell(celda(d.getCarrera() != null ? d.getCarrera() : "—", fCelda, bg));

            PdfPCell cPiso = celda(d.getPisoTexto(), fCelda, bg);
            cPiso.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(cPiso);

            Color[] chipColors = chipColores(d.getEstadoPago());
            PdfPCell cEstado = chipCell(d.getEstadoPagoTexto(), chipColors[0], bg, fChip, fCelda);
            cEstado.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(cEstado);

            fila = !fila;
        }

        doc.add(tabla);
        doc.close();
        return archivo;
    }

    private PdfPCell celda(String texto, Font f, Color bg) {
        PdfPCell c = new PdfPCell(new Phrase(texto, f));
        c.setBackgroundColor(bg);
        c.setPadding(7f);
        c.setBorderColor(GRIS_LINEA);
        c.setBorderWidth(0.5f);
        c.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return c;
    }

    /**
     * Simulates a chip by drawing a colored rounded rectangle via background +
     * padded phrase (OpenPDF doesn't support rounded cells natively so we just
     * use a tinted background + bold white text).
     */
    private PdfPCell chipCell(String texto, Color chipColor, Color rowBg, Font fChip, Font fCelda) {
        PdfPCell inner = new PdfPCell(new Phrase(texto, fChip));
        inner.setBackgroundColor(chipColor);
        inner.setPadding(5f);
        inner.setBorder(Rectangle.NO_BORDER);
        inner.setHorizontalAlignment(Element.ALIGN_CENTER);
        inner.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPTable wrapper = new PdfPTable(1);
        try { wrapper.setWidths(new float[]{1f}); } catch (DocumentException ignored) {}
        wrapper.addCell(inner);

        PdfPCell outer = new PdfPCell(wrapper);
        outer.setBackgroundColor(rowBg);
        outer.setPadding(5f);
        outer.setBorderColor(GRIS_LINEA);
        outer.setBorderWidth(0.5f);
        outer.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return outer;
    }

    private Color[] chipColores(EstadoPagoENUM ep) {
        if (ep == null) return new Color[]{GRIS_TEXTO};
        return switch (ep) {
            case AL_CORRIENTE -> new Color[]{VERDE};
            case CON_DEUDA    -> new Color[]{AMBAR};
            case MOROSO       -> new Color[]{ROJO};
        };
    }
}
