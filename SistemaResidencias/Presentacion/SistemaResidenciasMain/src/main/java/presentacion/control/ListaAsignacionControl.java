package presentacion.control;

import administradorAsignaciones.IAdministradorAsignaciones;
import administradorHabitaciones.IAdministradorHabitaciones;
import dtos.AsignacionReporteDTO;
import enums.EstadoPagoENUM;
import enums.GeneroENUM;
import infraestructura.servicios.ExportadorExcel;
import infraestructura.servicios.GeneradorListaPdf;
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

    private PantallaListaAsignacion vista;
    private List<AsignacionReporteDTO> listaCompleta = new ArrayList<>();

    public ListaAsignacionControl(IAdministradorAsignaciones adminAsignaciones,
            IAdministradorHabitaciones adminHabitaciones) {
        this.adminAsignaciones = adminAsignaciones;
        this.adminHabitaciones = adminHabitaciones;
    }

    public void setVista(PantallaListaAsignacion vista) {
        this.vista = vista;
    }

    // ─── Carga principal ───────────────────────────────────────────────────────

    public void cargarLista() {
        listaCompleta = adminAsignaciones.obtenerListaAsignaciones();

        int asignados = listaCompleta.size();
        int ocupadas  = (int) listaCompleta.stream()
                .map(AsignacionReporteDTO::getNumeroHabitacion)
                .filter(Objects::nonNull)
                .distinct()
                .count();
        int disponibles = adminHabitaciones.obtenerHabitacionDisponibles().size();

        vista.actualizarStats(ocupadas, disponibles, asignados);
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

    // ─── Filtros ──────────────────────────────────────────────────────────────

    public void aplicarFiltros(Integer piso, GeneroENUM genero,
            EstadoPagoENUM estadoPago, String ciclo) {
        List<AsignacionReporteDTO> filtrada = listaCompleta.stream()
                .filter(d -> piso == null || Objects.equals(d.getPiso(), piso))
                .filter(d -> genero == null || Objects.equals(d.getGenero(), genero))
                .filter(d -> estadoPago == null || Objects.equals(d.getEstadoPago(), estadoPago))
                .filter(d -> ciclo == null || ciclo.equals(d.getCicloLectivo()))
                .collect(Collectors.toList());
        vista.mostrarLista(filtrada);
    }

    public void limpiarFiltros() {
        vista.limpiarFiltros();
        vista.mostrarLista(listaCompleta);
    }

    // ─── Exportar PDF ─────────────────────────────────────────────────────────

    public void exportarPdf(List<AsignacionReporteDTO> listaActual) {
        try {
            GeneradorListaPdf gen = new GeneradorListaPdf();
            File pdf = gen.generarPdf(listaActual, "Lista de Asignación de Habitaciones — ITSON");
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdf);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al generar PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ─── Exportar Excel ───────────────────────────────────────────────────────

    public void exportarExcel(List<AsignacionReporteDTO> listaActual) {
        try {
            ExportadorExcel exp = new ExportadorExcel();
            File xlsx = exp.exportarExcel(listaActual,
                    "Lista de Asignación de Habitaciones — ITSON");
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(xlsx);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al exportar Excel: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ─── Imprimir ─────────────────────────────────────────────────────────────
    // Desktop.print() falla en Windows cuando no hay acción de impresión nativa
    // registrada para PDFs. Se abre el PDF con el visor predeterminado (Edge /
    // Adobe Reader) y el usuario imprime desde ahí con Ctrl+P.

    public void imprimir(List<AsignacionReporteDTO> listaActual) {
        try {
            GeneradorListaPdf gen = new GeneradorListaPdf();
            File pdf = gen.generarPdf(listaActual, "Lista de Asignación de Habitaciones — ITSON");
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                Desktop.getDesktop().open(pdf);
                JOptionPane.showMessageDialog(null,
                        "El PDF se abrió en tu visor predeterminado.\nUsa Ctrl+P para imprimir.",
                        "Imprimir", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al abrir el PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
