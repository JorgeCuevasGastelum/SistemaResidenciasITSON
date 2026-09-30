package presentacion.control;

import administradorAsignaciones.IAdministradorAsignaciones;
import administradorHabitaciones.IAdministradorHabitaciones;
import administradorResidentes.IAdministradorResidentes;
import dtos.AsignacionReporteDTO;
import enums.EstadoPagoENUM;
import enums.GeneroENUM;
import infraestructura.servicios.ExportadorExcel;
import java.awt.Desktop;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import presentacion.vistas.PantallaListaAsignacion;

public class ListaAsignacionControl {

    private final IAdministradorAsignaciones adminAsignaciones;
    private final IAdministradorHabitaciones adminHabitaciones;
    private final IAdministradorResidentes adminResidentes;

    private PantallaListaAsignacion vista;

    private List<AsignacionReporteDTO> listaCompleta =
            new ArrayList<>();

    public ListaAsignacionControl(
            IAdministradorAsignaciones adminAsignaciones,
            IAdministradorHabitaciones adminHabitaciones,
            IAdministradorResidentes adminResidentes) {

        this.adminAsignaciones = adminAsignaciones;
        this.adminHabitaciones = adminHabitaciones;
        this.adminResidentes = adminResidentes;
    }

    // ========================================================================
    // VISTA
    // ========================================================================

    public void setVista(PantallaListaAsignacion vista) {
        this.vista = vista;
    }

    // ========================================================================
    // CARGAR LISTA
    // ========================================================================

    public void cargarLista() {

        listaCompleta =
                adminAsignaciones.obtenerListaAsignaciones();

        int asignados =
                listaCompleta.size();

        int ocupadas =
                (int) listaCompleta.stream()
                        .map(AsignacionReporteDTO::getNumeroHabitacion)
                        .filter(Objects::nonNull)
                        .distinct()
                        .count();

        int disponibles =
                adminHabitaciones
                        .obtenerHabitacionDisponibles()
                        .size();

        vista.actualizarStats(
                ocupadas,
                disponibles,
                asignados
        );

        vista.actualizarCiclosDisponibles(
                listaCompleta.stream()
                        .map(AsignacionReporteDTO::getCicloLectivo)
                        .filter(Objects::nonNull)
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList())
        );

        vista.mostrarLista(listaCompleta);
    }

    // ========================================================================
    // FILTROS
    // ========================================================================

    public void aplicarFiltros(
            Integer piso,
            GeneroENUM genero,
            EstadoPagoENUM estadoPago,
            String ciclo) {

        List<AsignacionReporteDTO> filtrada =
                listaCompleta.stream()

                        .filter(d ->
                                piso == null
                                || Objects.equals(
                                        d.getPiso(),
                                        piso
                                )
                        )

                        .filter(d ->
                                genero == null
                                || Objects.equals(
                                        d.getGenero(),
                                        genero
                                )
                        )

                        .filter(d ->
                                estadoPago == null
                                || Objects.equals(
                                        d.getEstadoPago(),
                                        estadoPago
                                )
                        )

                        .filter(d ->
                                ciclo == null
                                || ciclo.equals(
                                        d.getCicloLectivo()
                                )
                        )

                        .collect(Collectors.toList());

        vista.mostrarLista(filtrada);
    }

    public void limpiarFiltros() {

        vista.limpiarFiltros();

        vista.mostrarLista(listaCompleta);
    }

    // ========================================================================
    // GENERAR XLSX
    // ========================================================================

    /**
     * Genera el XLSX utilizando la plantilla original.
     *
     * Este es el archivo base del que posteriormente
     * se obtiene el PDF y la impresión.
     */
    private File generarExcel(
            List<AsignacionReporteDTO> lista)
            throws Exception {

        if (lista == null || lista.isEmpty()) {

            throw new IllegalArgumentException(
                    "No hay asignaciones para generar la lista."
            );
        }

        int cantidadDeportistas =
                adminResidentes.getDeportistas();

        int cantidadExtranjeros =
                adminResidentes.getIntercambios();

        ExportadorExcel exportador =
                new ExportadorExcel();

        return exportador.exportarExcel(
                lista,
                cantidadDeportistas,
                cantidadExtranjeros
        );
    }

    // ========================================================================
    // EXPORTAR EXCEL
    // ========================================================================

    /**
     * Genera y abre el XLSX basado en la plantilla original.
     */
    public void exportarExcel(
            List<AsignacionReporteDTO> listaActual) {

        try {

            File excel =
                    generarExcel(listaActual);

            if (!Desktop.isDesktopSupported()) {

                throw new IllegalStateException(
                        "El sistema no permite abrir archivos."
                );
            }

            Desktop desktop =
                    Desktop.getDesktop();

            if (!desktop.isSupported(
                    Desktop.Action.OPEN)) {

                throw new IllegalStateException(
                        "El sistema no permite abrir archivos."
                );
            }

            desktop.open(excel);

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    "Error al exportar Excel:\n\n"
                    + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ========================================================================
    // GENERAR PDF
    // ========================================================================

    /**
     * Genera el XLSX desde la plantilla y posteriormente
     * convierte ESE XLSX a PDF.
     *
     * Por lo tanto, el PDF conserva el diseño de la plantilla Excel.
     */
    public void exportarPdf(
            List<AsignacionReporteDTO> listaActual) {

        try {

            // ------------------------------------------------------------
            // 1. GENERAR XLSX
            // ------------------------------------------------------------

            File excel =
                    generarExcel(listaActual);


            // ------------------------------------------------------------
            // 2. CONVERTIR EL MISMO XLSX A PDF
            // ------------------------------------------------------------

            ExportadorExcel exportador =
                    new ExportadorExcel();

            File pdf =
                    exportador.convertirExcelAPdf(
                            excel
                    );


            // ------------------------------------------------------------
            // 3. ABRIR PDF
            // ------------------------------------------------------------

            if (!Desktop.isDesktopSupported()) {

                throw new IllegalStateException(
                        "El sistema no permite abrir archivos."
                );
            }

            Desktop desktop =
                    Desktop.getDesktop();

            if (!desktop.isSupported(
                    Desktop.Action.OPEN)) {

                throw new IllegalStateException(
                        "El sistema no permite abrir archivos."
                );
            }

            desktop.open(pdf);

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    "Error al generar PDF:\n\n"
                    + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ========================================================================
    // IMPRIMIR
    // ========================================================================

    /**
     * Genera el XLSX desde la plantilla, convierte ESE XLSX
     * a PDF y manda ESE PDF a la impresora del sistema.
     *
     * No se genera un reporte alternativo.
     */
    public void imprimir(
            List<AsignacionReporteDTO> listaActual) {

        try {

            // ------------------------------------------------------------
            // 1. GENERAR XLSX
            // ------------------------------------------------------------

            File excel =
                    generarExcel(listaActual);


            // ------------------------------------------------------------
            // 2. CONVERTIR EL MISMO XLSX A PDF
            // ------------------------------------------------------------

            ExportadorExcel exportador =
                    new ExportadorExcel();

            File pdf =
                    exportador.convertirExcelAPdf(
                            excel
                    );


            // ------------------------------------------------------------
            // 3. ENVIAR EL PDF A IMPRIMIR
            // ------------------------------------------------------------

            if (!Desktop.isDesktopSupported()) {

                throw new IllegalStateException(
                        "El sistema no permite imprimir archivos."
                );
            }

            Desktop desktop =
                    Desktop.getDesktop();

            if (!desktop.isSupported(
                    Desktop.Action.PRINT)) {

                throw new IllegalStateException(
                        "El sistema no permite imprimir archivos."
                );
            }

            desktop.print(pdf);


        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    "Error al imprimir:\n\n"
                    + e.getMessage(),
                    "Error de impresión",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
