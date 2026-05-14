package presentacion.control;

import administradorAsignaciones.IAdministradorAsignaciones;
import administradorReferencias.IAdministradorReferencias;
import administradorResidentes.IAdministradorResidentes;
import dtos.AsignacionHabitacionDTO;
import dtos.ResidenteDTO;
import dtos.ReferenciasPagoDTO;
import java.awt.Desktop;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import infraestructura.servicios.EnviadorCorreo;
import infraestructura.servicios.GeneradorPdfReferencia;
import presentacion.vistas.PantallaReferenciaPago;

public class ReferenciasPagoControl {

    private static final String[] PLANES = {"Contado", "Mitad y Tres Pagos", "4 Pagos (Mensualidad)"};
    private static final double[] MONTOS_PLAN = {20651.0, 10872.0, 5653.0};

    private final IAdministradorResidentes adminResidentes;
    private final IAdministradorAsignaciones adminAsignaciones;
    private final IAdministradorReferencias adminReferencias;

    private PantallaReferenciaPago vista;
    private ResidenteDTO residenteSeleccionado;
    private AsignacionHabitacionDTO asignacionActiva;

    // Configuración SMTP
    private String smtpHost = "smtp.gmail.com";
    private int smtpPort = 587;
    private String smtpUsuario = "itsonresidencias@gmail.com";
    private String smtpContrasena = "duug qqoj gqfk uiom";

    public ReferenciasPagoControl(IAdministradorResidentes adminResidentes,
            IAdministradorAsignaciones adminAsignaciones,
            IAdministradorReferencias adminReferencias) {
        this.adminResidentes = adminResidentes;
        this.adminAsignaciones = adminAsignaciones;
        this.adminReferencias = adminReferencias;
    }

    public void setVista(PantallaReferenciaPago vista) {
        this.vista = vista;
    }

    public void configurarSmtp(String host, int port, String usuario, String contrasena) {
        this.smtpHost = host;
        this.smtpPort = port;
        this.smtpUsuario = usuario;
        this.smtpContrasena = contrasena;
    }

    public List<ResidenteDTO> buscarResidentes(String texto) {
        return adminResidentes.obtenerResidentesBusqueda(texto);
    }

    public void seleccionarResidente(ResidenteDTO residente) {
        ResidenteDTO completo = adminResidentes.obtenerResidentePorId(residente.getId());
        this.residenteSeleccionado = completo != null ? completo : residente;

        AsignacionHabitacionDTO asignacion = adminAsignaciones.obtenerAsignacionActiva(residenteSeleccionado.getId());
        if (asignacion == null) {
            vista.mostrarErrorSinHabitacion();
            this.residenteSeleccionado = null;
            return;
        }
        this.asignacionActiva = asignacion;
        vista.mostrarResidenteSeleccionado(residenteSeleccionado, asignacion.getNumeroHabitacion());
    }

    public void limpiarSeleccion() {
        residenteSeleccionado = null;
        asignacionActiva = null;
    }

    public String[] getPlanes() {
        return PLANES;
    }

    public double getMontoParaPlan(int indicePlan) {
        if (indicePlan >= 0 && indicePlan < MONTOS_PLAN.length) {
            return MONTOS_PLAN[indicePlan];
        }
        return 0;
    }

    public String calcularCicloLectivo() {
        int mes = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        return (mes >= 1 && mes <= 5)
                ? "Enero - Mayo " + year
                : "Agosto - Diciembre " + year;
    }

    public LocalDate calcularFechaLimite() {
        int mes = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        return (mes >= 1 && mes <= 5)
                ? LocalDate.of(year, 5, 31)
                : LocalDate.of(year, 12, 31);
    }

    public ReferenciasPagoDTO construirReferencia(String planDePago, String cicloLectivo,
            double monto, LocalDate fechaLimite) {
        if (residenteSeleccionado == null || asignacionActiva == null) return null;

        String concepto = generarConcepto(residenteSeleccionado, cicloLectivo);
        String referenciaBancaria = generarReferenciaBancaria();
        String nombreCompleto = residenteSeleccionado.getNombre()
                + " " + residenteSeleccionado.getApellido_paterno()
                + (residenteSeleccionado.getApellido_materno() != null
                        ? " " + residenteSeleccionado.getApellido_materno() : "");

        return new ReferenciasPagoDTO(
                residenteSeleccionado.getId(),
                nombreCompleto.trim(),
                residenteSeleccionado.getCorreo(),
                asignacionActiva.getNumeroHabitacion(),
                concepto,
                referenciaBancaria,
                planDePago,
                cicloLectivo,
                monto,
                fechaLimite
        );
    }

    public Long guardarReferencia(ReferenciasPagoDTO dto) {
        return adminReferencias.guardarReferencia(dto);
    }

    public File generarPdf(ReferenciasPagoDTO dto) {
        try {
            return new GeneradorPdfReferencia().generarPdf(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void imprimirPdf(File pdf) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean enviarPorCorreo(ReferenciasPagoDTO dto, File pdf) {
        if (smtpUsuario.isBlank()) return false;

        String correo = dto.getCorreoResidente();
        if (correo == null || correo.isBlank()) return false;

        try {
            EnviadorCorreo enviador = new EnviadorCorreo(smtpHost, smtpPort, smtpUsuario, smtpContrasena);
            String asunto = "Referencia de pago - " + dto.getConcepto();
            String cuerpo = "Hola " + dto.getNombreResidente() + ",\n\n"
                    + "Adjuntamos tu referencia de pago correspondiente al ciclo " + dto.getCicloLectivo() + ".\n\n"
                    + "Referencia bancaria: " + dto.getReferenciaBancaria() + "\n"
                    + "Total a pagar: $" + String.format("%,.2f", dto.getMonto()) + "\n"
                    + "Fecha límite: " + dto.getFechaLimite().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n\n"
                    + "Residencias Estudiantiles ITSON";
            enviador.enviarConAdjunto(correo, asunto, cuerpo, pdf);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String generarConcepto(ResidenteDTO r, String cicloLectivo) {
        StringBuilder iniciales = new StringBuilder();
        if (r.getNombre() != null && !r.getNombre().isBlank())
            iniciales.append(r.getNombre().charAt(0));
        if (r.getApellido_paterno() != null && !r.getApellido_paterno().isBlank())
            iniciales.append(r.getApellido_paterno().charAt(0));
        if (r.getApellido_materno() != null && !r.getApellido_materno().isBlank())
            iniciales.append(r.getApellido_materno().charAt(0));

        String cicloAbrev = cicloLectivo.startsWith("Enero") ? "EM" : "AD";
        int year = LocalDate.now().getYear();
        return iniciales.toString().toUpperCase() + cicloAbrev + year;
    }

    private String generarReferenciaBancaria() {
        long num = 1_000_000_000_000_000L + (long) (new Random().nextDouble() * 8_999_999_999_999_999L);
        return String.valueOf(num);
    }
}
