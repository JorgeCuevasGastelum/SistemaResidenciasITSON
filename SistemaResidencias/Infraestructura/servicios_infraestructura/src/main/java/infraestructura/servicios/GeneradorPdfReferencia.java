package infraestructura.servicios;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import dtos.ReferenciasPagoDTO;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;

public class GeneradorPdfReferencia {

    private static final Color AZUL_PRIMARIO = new Color(55, 75, 190);
    private static final Color GRIS_TEXTO = new Color(110, 110, 120);
    private static final Color GRIS_FONDO = new Color(245, 245, 248);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public File generarPdf(ReferenciasPagoDTO dto) throws Exception {
        File archivo = File.createTempFile("referencia_pago_", ".pdf");

        Document doc = new Document(PageSize.A5, 40, 40, 40, 40);
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(archivo));
        doc.open();

        Font fuenteInstitucion = new Font(Font.HELVETICA, 8, Font.NORMAL, GRIS_TEXTO);
        Font fuenteTitulo = new Font(Font.HELVETICA, 16, Font.BOLD, Color.BLACK);
        Font fuenteEtiq = new Font(Font.HELVETICA, 9, Font.NORMAL, GRIS_TEXTO);
        Font fuenteVal = new Font(Font.HELVETICA, 11, Font.BOLD, Color.BLACK);
        Font fuenteTotal = new Font(Font.HELVETICA, 14, Font.BOLD, AZUL_PRIMARIO);
        Font fuenteRef = new Font(Font.HELVETICA, 13, Font.BOLD, Color.BLACK);

        // Encabezado institucional
        PdfPTable encabezado = new PdfPTable(2);
        encabezado.setWidthPercentage(100);
        encabezado.setWidths(new float[]{3f, 1f});
        encabezado.setSpacingAfter(10);

        Paragraph textoInstitucional = new Paragraph(
                "Instituto Tecnológico de Sonora\n"
                + "Universidad Pública\n"
                + "Unidad Obregón, Campus Náinari.\n"
                + "Antonio Caso 2266, Villa ITSON, C.P. 85130.", fuenteInstitucion);
        PdfPCell celdaTexto = new PdfPCell(textoInstitucional);
        celdaTexto.setBorder(Rectangle.NO_BORDER);
        celdaTexto.setVerticalAlignment(Element.ALIGN_MIDDLE);
        encabezado.addCell(celdaTexto);

        java.io.InputStream isLogo = getClass().getClassLoader().getResourceAsStream("LogoResidencias.png");
        PdfPCell celdaLogo = new PdfPCell();
        celdaLogo.setBorder(Rectangle.NO_BORDER);
        celdaLogo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celdaLogo.setVerticalAlignment(Element.ALIGN_MIDDLE);
        if (isLogo != null) {
            Image imgLogo = Image.getInstance(isLogo.readAllBytes());
            imgLogo.scaleToFit(70, 70);
            celdaLogo.addElement(imgLogo);
        }
        encabezado.addCell(celdaLogo);
        doc.add(encabezado);

        agregarSeparador(doc);

        Paragraph titulo = new Paragraph("REFERENCIA DE PAGO", fuenteTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingBefore(8);
        titulo.setSpacingAfter(4);
        doc.add(titulo);

        agregarSeparador(doc);

        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{1f, 1.5f});
        tabla.setSpacingBefore(10);
        tabla.setSpacingAfter(10);

        agregarFilaTabla(tabla, "Residente:", dto.getNombreResidente(), fuenteEtiq, fuenteVal);
        agregarFilaTabla(tabla, "Habitacion:", String.valueOf(dto.getNumeroHabitacion()), fuenteEtiq, fuenteVal);
        agregarFilaTabla(tabla, "Concepto:", dto.getConcepto(), fuenteEtiq, fuenteVal);
        agregarFilaTabla(tabla, "Ciclo:", dto.getCicloLectivo(), fuenteEtiq, fuenteVal);
        agregarFilaTabla(tabla, "Plan de pago:", dto.getPlanDePago(), fuenteEtiq, fuenteVal);
        agregarFilaTabla(tabla, "Fecha limite:", dto.getFechaLimite() != null ? dto.getFechaLimite().format(FMT) : "-", fuenteEtiq, fuenteVal);
        doc.add(tabla);

        agregarSeparador(doc);

        Paragraph totalLabel = new Paragraph("Total a pagar:", fuenteEtiq);
        totalLabel.setSpacingBefore(8);
        doc.add(totalLabel);

        Paragraph totalVal = new Paragraph(String.format("$%,.2f", dto.getMonto()), fuenteTotal);
        totalVal.setSpacingAfter(12);
        doc.add(totalVal);

        PdfPTable tablaRef = new PdfPTable(1);
        tablaRef.setWidthPercentage(100);
        PdfPCell celda = new PdfPCell();
        celda.setBackgroundColor(GRIS_FONDO);
        celda.setBorderColor(new Color(210, 210, 215));
        celda.setPadding(12);

        Paragraph refLabel = new Paragraph("Referencia bancaria", fuenteEtiq);
        refLabel.setAlignment(Element.ALIGN_CENTER);

        Paragraph refNum = new Paragraph(dto.getReferenciaBancaria(), fuenteRef);
        refNum.setAlignment(Element.ALIGN_CENTER);

        celda.addElement(refLabel);
        celda.addElement(refNum);
        tablaRef.addCell(celda);
        doc.add(tablaRef);

        agregarLogosbancos(doc);

        if (dto.getFechaGeneracion() != null) {
            Paragraph fecha = new Paragraph("Generada el: " + dto.getFechaGeneracion().format(FMT), fuenteEtiq);
            fecha.setAlignment(Element.ALIGN_RIGHT);
            fecha.setSpacingBefore(8);
            doc.add(fecha);
        }

        doc.close();
        return archivo;
    }

    private void agregarLogosbancos(Document doc) throws Exception {
        PdfPTable tablaLogos = new PdfPTable(2);
        tablaLogos.setWidthPercentage(100);
        tablaLogos.setSpacingBefore(14);

        for (String recurso : new String[]{"bbva_logo.png", "santander_logo.png"}) {
            java.io.InputStream is = getClass().getClassLoader().getResourceAsStream(recurso);
            PdfPCell celda = new PdfPCell();
            celda.setBorder(Rectangle.NO_BORDER);
            celda.setHorizontalAlignment(Element.ALIGN_CENTER);
            celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celda.setPadding(4);

            if (is != null) {
                Image img = Image.getInstance(is.readAllBytes());
                img.scaleToFit(75f, 22f);
                celda.addElement(img);
            }
            tablaLogos.addCell(celda);
        }

        doc.add(tablaLogos);
    }

    private void agregarSeparador(Document doc) throws DocumentException {
        PdfPTable sep = new PdfPTable(1);
        sep.setWidthPercentage(100);
        sep.setSpacingBefore(4);
        sep.setSpacingAfter(4);
        PdfPCell celda = new PdfPCell();
        celda.setBorder(Rectangle.TOP);
        celda.setBorderColor(GRIS_TEXTO);
        celda.setBorderWidth(0.5f);
        celda.setFixedHeight(1);
        celda.setPadding(0);
        sep.addCell(celda);
        doc.add(sep);
    }

    private void agregarFilaTabla(PdfPTable tabla, String etiq, String valor,
            Font fEtiq, Font fVal) {
        PdfPCell c1 = new PdfPCell(new Phrase(etiq, fEtiq));
        c1.setBorder(Rectangle.NO_BORDER);
        c1.setPaddingBottom(6);

        PdfPCell c2 = new PdfPCell(new Phrase(valor != null ? valor : "-", fVal));
        c2.setBorder(Rectangle.NO_BORDER);
        c2.setPaddingBottom(6);

        tabla.addCell(c1);
        tabla.addCell(c2);
    }
}
