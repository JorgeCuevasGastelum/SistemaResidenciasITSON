package presentacion.vistas;

import dtos.ResidenteDTO;
import enums.EstadoPagoENUM;
import enums.EstadoResidenteENUM;
import enums.GeneroENUM;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.*;
import java.util.function.Consumer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;
import presentacion.control.GestionarResidentesControl;

public class PantallaGestionarResidentes extends JPanel {

    private static final Color AZUL_PRIMARIO = new Color(55, 75, 190);
    private static final Color AZUL_HOVER = new Color(45, 60, 160);
    private static final Color TEXTO_PRINCIPAL = new Color(30, 30, 40);
    private static final Color TEXTO_SEC = new Color(110, 110, 125);
    private static final Color BORDE_INPUT = new Color(200, 195, 225);
    private static final Color FONDO_INPUT = new Color(250, 249, 255);
    private static final Color FONDO_PANEL = new Color(243, 243, 247);
    private static final Color HEADER_TABLA = new Color(55, 75, 135);
    private static final Color CHIP_HABITACION = new Color(130, 110, 200);
    private static final Color CHIP_ACTIVO = new Color(40, 160, 80);
    private static final Color CHIP_INACTIVO = new Color(160, 90, 90);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private GestionarResidentesControl control;
    private ResidenteTableModel tableModel;
    private JTable tabla;

    public PantallaGestionarResidentes() {
        setOpaque(true);
        setBackground(FONDO_PANEL);
        setLayout(new BorderLayout());
        construirUI();
    }

    public void setControl(GestionarResidentesControl control) {
        this.control = control;
        control.setVista(this);
        control.cargarResidentes();
    }

    private void construirUI() {
        add(construirCabecera(), BorderLayout.NORTH);
        add(construirCuerpo(), BorderLayout.CENTER);
    }

    private JPanel construirCabecera() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(30, 36, 18, 36));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Gestionar Residentes");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(TEXTO_PRINCIPAL);

        JLabel subtitulo = new JLabel("Administra la información de los residentes");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitulo.setForeground(TEXTO_SEC);

        textos.add(titulo);
        textos.add(Box.createVerticalStrut(4));
        textos.add(subtitulo);

        JButton btnAgregar = botonPrimario("+ Agregar residente");
        btnAgregar.setPreferredSize(new Dimension(190, 44));
        btnAgregar.addActionListener(e -> abrirDialogoFormulario(null));

        header.add(textos, BorderLayout.WEST);
        header.add(btnAgregar, BorderLayout.EAST);
        return header;
    }

    private JPanel construirCuerpo() {
        JPanel cuerpo = new JPanel(new BorderLayout());
        cuerpo.setOpaque(false);
        cuerpo.setBorder(new EmptyBorder(0, 36, 36, 36));

        JPanel tarjeta = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
            }
        };
        tarjeta.setOpaque(false);

        tableModel = new ResidenteTableModel();
        tabla = new JTable(tableModel);
        configurarTabla();

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUI(new BarraDelgada());
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(5, 0));

        tarjeta.add(scroll, BorderLayout.CENTER);
        cuerpo.add(tarjeta, BorderLayout.CENTER);
        return cuerpo;
    }

    private void configurarTabla() {
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setRowHeight(52);
        tabla.setShowGrid(false);
        tabla.setIntercellSpacing(new Dimension(0, 0));
        tabla.setBackground(Color.WHITE);
        tabla.setSelectionBackground(new Color(245, 243, 255));
        tabla.setFocusable(false);

        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setForeground(Color.WHITE);
        header.setBackground(HEADER_TABLA);
        header.setPreferredSize(new Dimension(0, 46));
        header.setBorder(BorderFactory.createEmptyBorder());
        header.setDefaultRenderer(new HeaderRenderer());
        header.setReorderingAllowed(false);

        tabla.getColumnModel().getColumn(0).setPreferredWidth(130);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(175);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(110);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(95);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(130);
        tabla.getColumnModel().getColumn(6).setPreferredWidth(110);

        tabla.setShowHorizontalLines(true);
        tabla.setGridColor(new Color(238, 235, 250));

        DefaultTableCellRenderer textRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean sel, boolean focus, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, value, sel, focus, row, col);
                lbl.setBorder(new EmptyBorder(0, 16, 0, 8));
                lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                lbl.setForeground(TEXTO_PRINCIPAL);
                lbl.setBackground(sel ? new Color(245, 243, 255) : Color.WHITE);
                return lbl;
            }
        };
        for (int c : new int[]{0, 1, 2, 5}) {
            tabla.getColumnModel().getColumn(c).setCellRenderer(textRenderer);
        }

        tabla.getColumnModel().getColumn(3).setCellRenderer(new ChipRenderer(CHIP_HABITACION));
        tabla.getColumnModel().getColumn(4).setCellRenderer(new EstadoChipRenderer());
        tabla.getColumnModel().getColumn(6).setCellRenderer(new AccionesRenderer());

        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabla.rowAtPoint(e.getPoint());
                int col = tabla.columnAtPoint(e.getPoint());
                if (row < 0 || col != 6) return;

                Rectangle cellRect = tabla.getCellRect(row, col, false);
                int xInCell = e.getX() - cellRect.x;

                ResidenteDTO r = tableModel.getResidente(row);
                if (xInCell < 38) {
                    abrirDialogoFormulario(r.getId());
                } else if (xInCell < 72) {
                    confirmarEliminar(r);
                } else {
                    abrirDialogoDetalle(r.getId());
                }
            }
        });
    }

    public void mostrarResidentes(List<ResidenteDTO> residentes) {
        tableModel.setResidentes(residentes);
    }

    public void reiniciar() {
        if (control != null) control.cargarResidentes();
    }

    // ─── DIÁLOGO FORMULARIO ────────────────────────────────────────────────────

    private void abrirDialogoFormulario(String idResidente) {
        ResidenteDTO dto = null;
        boolean esEdicion = (idResidente != null);
        if (esEdicion) {
            dto = control.obtenerDetalle(idResidente);
        }
        new DialogoFormulario(obtenerVentana(), dto, esEdicion).setVisible(true);
    }

    private void abrirDialogoDetalle(String idResidente) {
        ResidenteDTO dto = control.obtenerDetalle(idResidente);
        if (dto == null) return;
        new DialogoDetalle(obtenerVentana(), dto).setVisible(true);
    }

    private void confirmarEliminar(ResidenteDTO r) {
        String nombre = r.getNombre() + " " + r.getApellido_paterno();
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "<html>¿Desactivar al residente <b>" + nombre + "</b>?<br>"
                + "<small>El registro cambiará a estado Inactivo.</small></html>",
                "Confirmar desactivación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (opcion == JOptionPane.YES_OPTION) {
            control.eliminarResidente(r.getId());
        }
    }

    private JFrame obtenerVentana() {
        Container c = getParent();
        while (c != null && !(c instanceof JFrame)) c = c.getParent();
        return (JFrame) c;
    }

    // ─── INNER: TABLE MODEL ───────────────────────────────────────────────────

    private class ResidenteTableModel extends AbstractTableModel {
        private final String[] COLUMNAS = {"ID", "Nombre", "Carrera", "Habitacion", "Estado", "Telefono", "Acciones"};
        private List<ResidenteDTO> datos = new ArrayList<>();

        void setResidentes(List<ResidenteDTO> lista) {
            this.datos = lista != null ? lista : new ArrayList<>();
            fireTableDataChanged();
        }

        ResidenteDTO getResidente(int row) {
            return datos.get(row);
        }

        @Override public int getRowCount() { return datos.size(); }
        @Override public int getColumnCount() { return COLUMNAS.length; }
        @Override public String getColumnName(int col) { return COLUMNAS[col]; }
        @Override public boolean isCellEditable(int row, int col) { return false; }

        @Override
        public Object getValueAt(int row, int col) {
            ResidenteDTO r = datos.get(row);
            return switch (col) {
                case 0 -> r.getId();
                case 1 -> r.getNombre() + " " + r.getApellido_paterno() + " " + r.getApellido_materno();
                case 2 -> r.getCarrera() != null ? abreviarCarrera(r.getCarrera()) : "-";
                case 3 -> r.getNumeroHabitacion() != null ? r.getNumeroHabitacion() : "N/A";
                case 4 -> r.getEstado();
                case 5 -> r.getTelefono() != null ? r.getTelefono() : "-";
                case 6 -> r;
                default -> "";
            };
        }

        private String abreviarCarrera(String carrera) {
            if (carrera.length() <= 18) return carrera;
            return carrera.substring(0, 16) + "...";
        }
    }

    // ─── INNER: RENDERERS ────────────────────────────────────────────────────

    private static class HeaderRenderer extends DefaultTableCellRenderer {
        private static final Color HEADER_TABLA = new Color(55, 75, 135);

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col) {
            JLabel lbl = new JLabel(value != null ? value.toString() : "");
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lbl.setForeground(Color.WHITE);
            lbl.setBackground(HEADER_TABLA);
            lbl.setOpaque(true);
            lbl.setBorder(new EmptyBorder(0, 14, 0, 0));
            lbl.setHorizontalAlignment(LEFT);
            return lbl;
        }
    }

    private static class ChipRenderer extends DefaultTableCellRenderer {
        private final Color chipColor;

        ChipRenderer(Color chipColor) {
            this.chipColor = chipColor;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col) {
            String texto = value != null ? value.toString() : "N/A";
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(isSelected ? new Color(245, 243, 255) : Color.WHITE);

            JLabel chip = new JLabel(texto, SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    Color c = "N/A".equals(texto) ? new Color(180, 170, 200) : chipColor;
                    g2.setColor(c);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            chip.setFont(new Font("Segoe UI", Font.BOLD, 11));
            chip.setForeground(Color.WHITE);
            chip.setOpaque(false);
            chip.setBorder(new EmptyBorder(4, 12, 4, 12));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.WEST;
            gbc.insets = new Insets(0, 14, 0, 0);
            panel.add(chip, gbc);
            return panel;
        }
    }

    private static class EstadoChipRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col) {
            EstadoResidenteENUM estado = (EstadoResidenteENUM) value;
            String texto = estado == EstadoResidenteENUM.ACTIVO ? "Activo" : "Inactivo";
            Color chipColor = estado == EstadoResidenteENUM.ACTIVO
                    ? new Color(40, 160, 80) : new Color(160, 90, 90);

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(isSelected ? new Color(245, 243, 255) : Color.WHITE);

            JLabel chip = new JLabel(texto, SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(chipColor);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            chip.setFont(new Font("Segoe UI", Font.BOLD, 11));
            chip.setForeground(Color.WHITE);
            chip.setOpaque(false);
            chip.setBorder(new EmptyBorder(4, 12, 4, 12));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.WEST;
            gbc.insets = new Insets(0, 14, 0, 0);
            panel.add(chip, gbc);
            return panel;
        }
    }

    private static class AccionesRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col) {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(isSelected ? new Color(245, 243, 255) : Color.WHITE);
            JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
            btns.setOpaque(false);
            btns.add(iconBtn(0, new Color(55, 75, 190)));   // editar
            btns.add(iconBtn(1, new Color(200, 60, 60)));   // eliminar
            btns.add(iconBtn(2, new Color(80, 100, 140)));  // detalle
            panel.add(btns); // GridBagLayout centra h y v por defecto
            return panel;
        }

        // tipo: 0 = lápiz/editar, 1 = X/eliminar, 2 = líneas/detalle
        private JComponent iconBtn(int tipo, Color color) {
            return new JComponent() {
                {
                    setPreferredSize(new Dimension(30, 30));
                    setOpaque(false);
                }
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

                    // fondo redondeado semitransparente
                    g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 22));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);

                    g2.setColor(color);
                    int cx = getWidth() / 2;
                    int cy = getHeight() / 2;

                    if (tipo == 0) {
                        // Lápiz (editar): romboide + punta
                        g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        int[] px = {cx - 5, cx + 2, cx + 5, cx - 2};
                        int[] py = {cy + 4, cy - 3, cy  ,  cy + 7};
                        g2.drawPolygon(px, py, 4);
                        g2.drawLine(cx - 5, cy + 4, cx - 7, cy + 7);
                        g2.drawLine(cx - 2, cy + 7, cx - 7, cy + 7);
                    } else if (tipo == 1) {
                        // Cruz (eliminar)
                        g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        g2.drawLine(cx - 4, cy - 4, cx + 4, cy + 4);
                        g2.drawLine(cx + 4, cy - 4, cx - 4, cy + 4);
                    } else {
                        // Tres líneas (detalle)
                        g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        g2.drawLine(cx - 5, cy - 4, cx + 5, cy - 4);
                        g2.drawLine(cx - 5, cy,     cx + 5, cy);
                        g2.drawLine(cx - 5, cy + 4, cx + 5, cy + 4);
                    }

                    g2.dispose();
                }
            };
        }
    }

    // ─── INNER: DIÁLOGO FORMULARIO ───────────────────────────────────────────

    private class DialogoFormulario extends JDialog {
        private static final String[] CARRERAS = {
            "Ingeniería en Software", "Ingeniería en Sistemas",
            "Ingeniería Industrial", "Administración de Empresas",
            "Derecho", "Psicología", "Ingeniería Civil",
            "Contaduría Pública", "Diseño Gráfico", "Medicina"
        };

        private JTextField campoNombre, campoAP, campoAM, campoMatricula;
        private JTextField campoTelefono, campoCorreo;
        private DatePickerField campoFechaNac, campoFechaIng;
        private JComboBox<String> comboCarrera, comboGenero, comboEstadoPago;
        private JTextField campoNombreAval, campoParentesco, campoTelAval, campoCorreoAval, campoDirAval;
        private JCheckBox checkVehiculo;
        private JTextField campoModelo, campoColor, campoPlacas;
        private DatePickerField campoUltimoPago;
        private JTextField campoAdeudo;

        private final boolean esEdicion;
        private ResidenteDTO dtoOriginal;
        private boolean reactivar = false;

        DialogoFormulario(JFrame owner, ResidenteDTO dto, boolean esEdicion) {
            super(owner, esEdicion ? "Editar Residente" : "Nuevo Residente", true);
            this.esEdicion = esEdicion;
            this.dtoOriginal = dto;
            setUndecorated(true);
            construirUI();
            if (dto != null) rellenarCampos(dto);
            pack();
            setShape(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 22, 22));
            setLocationRelativeTo(owner);
        }

        private void construirUI() {
            JPanel root = new JPanel();
            root.setLayout(new BorderLayout());
            root.setBackground(Color.WHITE);
            root.setBorder(BorderFactory.createLineBorder(AZUL_PRIMARIO, 2));

            // ── Cabecera azul ──────────────────────────────────────────────
            JPanel headerDlg = new JPanel(new BorderLayout());
            headerDlg.setBackground(AZUL_PRIMARIO);
            headerDlg.setBorder(new EmptyBorder(14, 22, 14, 16));
            JLabel lblTituloDlg = new JLabel(esEdicion ? "Editar Residente" : "Nuevo Residente");
            lblTituloDlg.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblTituloDlg.setForeground(Color.WHITE);
            JButton btnXDlg = btnCerrarBlanco();
            btnXDlg.addActionListener(e -> dispose());
            headerDlg.add(lblTituloDlg, BorderLayout.WEST);
            headerDlg.add(btnXDlg, BorderLayout.EAST);
            root.add(headerDlg, BorderLayout.NORTH);

            JPanel scroll = new JPanel();
            scroll.setLayout(new BoxLayout(scroll, BoxLayout.Y_AXIS));
            scroll.setBackground(Color.WHITE);
            scroll.setBorder(new EmptyBorder(20, 36, 20, 36));

            // ── Aviso: residente inactivo ───────────────────────────────────
            if (esEdicion && dtoOriginal != null && dtoOriginal.getEstado() == EstadoResidenteENUM.INACTIVO) {
                JPanel avisoInactivo = new JPanel(new BorderLayout(12, 0));
                avisoInactivo.setOpaque(true);
                avisoInactivo.setBackground(new Color(255, 244, 210));
                avisoInactivo.setBorder(new CompoundBorder(
                        new LineBorder(new Color(210, 160, 30), 1, true),
                        new EmptyBorder(10, 14, 10, 14)));
                avisoInactivo.setAlignmentX(LEFT_ALIGNMENT);
                avisoInactivo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));

                JLabel aviso = new JLabel("Este residente está marcado como Inactivo");
                aviso.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                aviso.setForeground(new Color(100, 65, 0));

                JButton btnReactivar = new JButton("Reactivar") {
                    @Override protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(getModel().isRollover() ? new Color(170, 120, 15) : new Color(195, 145, 25));
                        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };
                btnReactivar.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnReactivar.setForeground(Color.WHITE);
                btnReactivar.setContentAreaFilled(false);
                btnReactivar.setBorderPainted(false);
                btnReactivar.setFocusPainted(false);
                btnReactivar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                btnReactivar.setPreferredSize(new Dimension(105, 32));
                btnReactivar.addActionListener(e -> { reactivar = true; guardar(); });

                avisoInactivo.add(aviso, BorderLayout.CENTER);
                avisoInactivo.add(btnReactivar, BorderLayout.EAST);
                scroll.add(avisoInactivo);
                scroll.add(Box.createVerticalStrut(14));
            }

            scroll.add(seccion("Datos Personales"));
            scroll.add(Box.createVerticalStrut(14));

            campoNombre = campo("Nombre");
            campoAP = campo("Apellido paterno");
            campoAM = campo("Apellido materno");
            campoMatricula = campo("00000249718");
            comboCarrera = comboBox(CARRERAS);
            campoTelefono = campo("000-000-0000");
            campoCorreo = campo("correo@potros.itson.edu.mx");
            comboGenero = comboBox(new String[]{"Masculino", "Femenino", "Otro"});
            campoFechaNac = new DatePickerField("Fecha de nacimiento", true);
            campoFechaIng = new DatePickerField("Fecha de ingreso", false);

            if (esEdicion) campoMatricula.setEditable(false);

            scroll.add(fila2(etiqueta("Nombre"), campoNombre, etiqueta("Apellido paterno"), campoAP));
            scroll.add(Box.createVerticalStrut(10));
            scroll.add(fila2(etiqueta("Apellido materno"), campoAM, etiqueta("Matrícula"), campoMatricula));
            scroll.add(Box.createVerticalStrut(10));
            scroll.add(fila2(etiqueta("Carrera"), comboCarrera, etiqueta("Teléfono"), campoTelefono));
            scroll.add(Box.createVerticalStrut(10));
            scroll.add(fila2(etiqueta("Correo electrónico"), campoCorreo, etiqueta("Género"), comboGenero));
            scroll.add(Box.createVerticalStrut(10));
            scroll.add(fila2(etiqueta("Fecha de nacimiento"), campoFechaNac, etiqueta("Fecha de ingreso"), campoFechaIng));
            scroll.add(Box.createVerticalStrut(20));

            scroll.add(seccion("Información del Aval / Fiador"));
            scroll.add(Box.createVerticalStrut(14));

            campoNombreAval = campo("Nombre completo");
            campoParentesco = campo("Padre, Madre, etc.");
            campoTelAval = campo("123-456-7890");
            campoCorreoAval = campo("correo@email.com");
            campoDirAval = campo("Dirección completa");

            scroll.add(filaAncha(etiqueta("Nombre del aval"), campoNombreAval));
            scroll.add(Box.createVerticalStrut(10));
            scroll.add(fila2(etiqueta("Parentesco"), campoParentesco, etiqueta("Teléfono"), campoTelAval));
            scroll.add(Box.createVerticalStrut(10));
            scroll.add(fila2(etiqueta("Correo electrónico"), campoCorreoAval, etiqueta(""), new JLabel("")));
            scroll.add(Box.createVerticalStrut(10));
            scroll.add(filaAncha(etiqueta("Dirección"), campoDirAval));
            scroll.add(Box.createVerticalStrut(20));

            scroll.add(seccion("Permiso Vehicular"));
            scroll.add(Box.createVerticalStrut(12));

            checkVehiculo = new JCheckBox("¿Cuenta con vehículo?");
            checkVehiculo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            checkVehiculo.setForeground(TEXTO_PRINCIPAL);
            checkVehiculo.setOpaque(false);
            checkVehiculo.setAlignmentX(LEFT_ALIGNMENT);

            campoModelo = campo("Modelo del vehículo");
            campoColor = campo("Color");
            campoPlacas = campo("ABC-1234");

            scroll.add(checkVehiculo);
            scroll.add(Box.createVerticalStrut(10));
            scroll.add(fila2(etiqueta("Modelo"), campoModelo, etiqueta("Color"), campoColor));
            scroll.add(Box.createVerticalStrut(10));
            scroll.add(filaAncha(etiqueta("Placas"), campoPlacas));
            scroll.add(Box.createVerticalStrut(20));

            checkVehiculo.addActionListener(e -> actualizarCamposVehiculo());
            campoModelo.setEnabled(false);
            campoColor.setEnabled(false);
            campoPlacas.setEnabled(false);

            scroll.add(seccion("Plan de pago"));
            scroll.add(Box.createVerticalStrut(14));

            comboEstadoPago = comboBox(new String[]{"Al corriente", "Con deuda", "Moroso"});
            campoUltimoPago = new DatePickerField("Último pago", true);
            campoAdeudo = campo("$0.00");

            scroll.add(fila3(etiqueta("Estado de pagos"), comboEstadoPago,
                    etiqueta("Último pago"), campoUltimoPago,
                    etiqueta("Adeudo pendiente"), campoAdeudo));
            scroll.add(Box.createVerticalStrut(16));

            JScrollPane sp = new JScrollPane(scroll);
            sp.setBorder(BorderFactory.createEmptyBorder());
            sp.getVerticalScrollBar().setUI(new BarraDelgada());
            sp.getVerticalScrollBar().setPreferredSize(new Dimension(5, 0));
            sp.setPreferredSize(new Dimension(690, 560));
            sp.getVerticalScrollBar().setUnitIncrement(16);
            sp.getVerticalScrollBar().setBlockIncrement(64);

            // Botones FIJOS fuera del área desplazable (BorderLayout.SOUTH)
            JPanel botones = new JPanel(new GridLayout(1, 2, 12, 0));
            botones.setOpaque(true);
            botones.setBackground(Color.WHITE);
            botones.setBorder(new EmptyBorder(12, 36, 20, 36));

            JButton btnCancelar = botonSecundario("Cancelar");
            JButton btnGuardar = botonPrimario("Guardar");
            btnCancelar.setPreferredSize(new Dimension(250, 44));
            btnGuardar.setPreferredSize(new Dimension(250, 44));

            btnCancelar.addActionListener(e -> dispose());
            btnGuardar.addActionListener(e -> guardar());

            botones.add(btnCancelar);
            botones.add(btnGuardar);

            root.add(sp, BorderLayout.CENTER);
            root.add(botones, BorderLayout.SOUTH);
            setContentPane(root);
        }

        private void actualizarCamposVehiculo() {
            boolean tiene = checkVehiculo.isSelected();
            campoModelo.setEnabled(tiene);
            campoColor.setEnabled(tiene);
            campoPlacas.setEnabled(tiene);
        }

        private void setValor(JTextField campo, String valor) {
            if (valor != null && !valor.isEmpty()) {
                campo.setText(valor);
                campo.setForeground(TEXTO_PRINCIPAL);
            }
        }

        private void rellenarCampos(ResidenteDTO dto) {
            setValor(campoNombre, dto.getNombre());
            setValor(campoAP, dto.getApellido_paterno());
            setValor(campoAM, dto.getApellido_materno());
            setValor(campoMatricula, dto.getId());
            setValor(campoTelefono, dto.getTelefono());
            setValor(campoCorreo, dto.getCorreo());

            campoFechaNac.setDate(dto.getFechaNacimiento());
            campoFechaIng.setDate(dto.getFechaIngreso());

            if (dto.getGenero() != null) {
                comboGenero.setSelectedIndex(switch (dto.getGenero()) {
                    case HOMBRE -> 0;
                    case MUJER -> 1;
                    default -> 2;
                });
            }

            if (dto.getCarrera() != null) {
                for (int i = 0; i < comboCarrera.getItemCount(); i++) {
                    if (comboCarrera.getItemAt(i).equalsIgnoreCase(dto.getCarrera())) {
                        comboCarrera.setSelectedIndex(i);
                        break;
                    }
                }
            }

            setValor(campoNombreAval, dto.getNombreAval());
            setValor(campoParentesco, dto.getParentescoAval());
            setValor(campoTelAval, dto.getTelefonoAval());
            setValor(campoCorreoAval, dto.getCorreoAval());
            setValor(campoDirAval, dto.getDireccionAval());

            boolean tieneVehiculo = dto.getPermiso_vehicular() != null && dto.getPermiso_vehicular() > 0;
            checkVehiculo.setSelected(tieneVehiculo);
            actualizarCamposVehiculo();
            setValor(campoModelo, dto.getModeloVehiculo());
            setValor(campoColor, dto.getColorVehiculo());
            setValor(campoPlacas, dto.getPlacasVehiculo());

            if (dto.getEstadoPago() != null) {
                comboEstadoPago.setSelectedIndex(switch (dto.getEstadoPago()) {
                    case AL_CORRIENTE -> 0;
                    case CON_DEUDA -> 1;
                    case MOROSO -> 2;
                });
            }
            campoUltimoPago.setDate(dto.getUltimoPago());
            if (dto.getAdeudoPendiente() != null) {
                campoAdeudo.setText(String.format("%.2f", dto.getAdeudoPendiente()));
                campoAdeudo.setForeground(TEXTO_PRINCIPAL);
            }
        }

        private void guardar() {
            // ─── Validaciones ──────────────────────────────────────────────────
            String nombre    = getValor(campoNombre);
            String ap        = getValor(campoAP);
            String am        = getValor(campoAM);
            String matricula = getValor(campoMatricula);
            String correo    = getValor(campoCorreo);
            String telefono  = getValor(campoTelefono);

            if (nombre.isEmpty() || ap.isEmpty() || matricula.isEmpty()) {
                mostrarError("Los campos Nombre, Apellido paterno y Matrícula son obligatorios.");
                return;
            }
            if (!matricula.matches("\\d{11}")) {
                mostrarError("La matrícula debe contener exactamente 11 dígitos numéricos\n(ejemplo: 00000252274).");
                return;
            }
            if (!correo.isEmpty() && !correo.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
                mostrarError("El correo electrónico no tiene un formato válido.");
                return;
            }
            if (!telefono.isEmpty() && !telefono.matches("[\\d\\-\\s()]{7,15}")) {
                mostrarError("El teléfono debe tener entre 7 y 15 caracteres\n(dígitos, guiones o paréntesis).");
                return;
            }

            LocalDate hoy      = LocalDate.now();
            LocalDate fechaNac = campoFechaNac.getDate();
            LocalDate fechaIng = campoFechaIng.getDate();
            LocalDate ultPago  = campoUltimoPago.getDate();

            if (fechaNac != null && fechaNac.isAfter(hoy)) {
                mostrarError("La fecha de nacimiento no puede ser una fecha futura.");
                return;
            }
            if (fechaIng != null && fechaIng.isAfter(hoy)) {
                mostrarError("La fecha de ingreso no puede ser una fecha futura.");
                return;
            }
            if (ultPago != null && ultPago.isAfter(hoy)) {
                mostrarError("La fecha de último pago no puede ser una fecha futura.");
                return;
            }
            if (checkVehiculo.isSelected() && getValor(campoPlacas).isEmpty()) {
                mostrarError("Si el residente cuenta con vehículo, las placas son obligatorias.");
                return;
            }

            String adeudoTxt = getValor(campoAdeudo).replace("$", "").replace(",", "");
            double adeudo = 0.0;
            if (!adeudoTxt.isEmpty()) {
                try {
                    adeudo = Double.parseDouble(adeudoTxt);
                    if (adeudo < 0) { mostrarError("El adeudo pendiente no puede ser negativo."); return; }
                } catch (NumberFormatException ex) {
                    mostrarError("El adeudo pendiente debe ser un valor numérico."); return;
                }
            }

            // ─── Construir DTO ─────────────────────────────────────────────────
            ResidenteDTO dto = new ResidenteDTO();
            dto.setId(matricula);
            dto.setNombre(nombre);
            dto.setApellido_paterno(ap);
            dto.setApellido_materno(am);
            dto.setCarrera((String) comboCarrera.getSelectedItem());
            dto.setTelefono(telefono);
            dto.setCorreo(correo);
            dto.setGenero(switch (comboGenero.getSelectedIndex()) {
                case 1 -> GeneroENUM.MUJER;
                case 2 -> GeneroENUM.OTRO;
                default -> GeneroENUM.HOMBRE;
            });
            dto.setEstado((esEdicion && dtoOriginal != null && !reactivar)
                    ? dtoOriginal.getEstado() : EstadoResidenteENUM.ACTIVO);
            dto.setDireccion("");

            dto.setFechaNacimiento(fechaNac);
            dto.setFechaIngreso(fechaIng);

            dto.setNombreAval(getValor(campoNombreAval));
            dto.setParentescoAval(getValor(campoParentesco));
            dto.setTelefonoAval(getValor(campoTelAval));
            dto.setCorreoAval(getValor(campoCorreoAval));
            dto.setDireccionAval(getValor(campoDirAval));

            boolean tieneVehiculo = checkVehiculo.isSelected();
            dto.setPermiso_vehicular(tieneVehiculo ? 1 : 0);
            dto.setModeloVehiculo(tieneVehiculo ? getValor(campoModelo) : null);
            dto.setColorVehiculo(tieneVehiculo ? getValor(campoColor) : null);
            dto.setPlacasVehiculo(tieneVehiculo ? getValor(campoPlacas) : null);

            dto.setEstadoPago(switch (comboEstadoPago.getSelectedIndex()) {
                case 1 -> EstadoPagoENUM.CON_DEUDA;
                case 2 -> EstadoPagoENUM.MOROSO;
                default -> EstadoPagoENUM.AL_CORRIENTE;
            });
            dto.setUltimoPago(ultPago);
            dto.setAdeudoPendiente(adeudo);

            if (esEdicion) control.actualizarResidente(dto);
            else           control.guardarResidente(dto);
            dispose();
        }

        private void mostrarError(String msg) {
            JOptionPane.showMessageDialog(this, msg, "Datos inválidos", JOptionPane.WARNING_MESSAGE);
        }

        private LocalDate parsearFecha(String texto) {
            if (texto == null || texto.isEmpty() || texto.equals("dd/mm/yyyy")) return null;
            try {
                return LocalDate.parse(texto, FMT);
            } catch (DateTimeParseException e) {
                return null;
            }
        }

        // ── helpers de layout ──

        private JPanel fila2(JLabel l1, JComponent c1, JLabel l2, JComponent c2) {
            JPanel p = new JPanel(new GridLayout(1, 2, 14, 0));
            p.setOpaque(false);
            p.setAlignmentX(LEFT_ALIGNMENT);
            p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
            p.add(col(l1, c1));
            p.add(col(l2, c2));
            return p;
        }

        private JPanel fila3(JLabel l1, JComponent c1, JLabel l2, JComponent c2, JLabel l3, JComponent c3) {
            JPanel p = new JPanel(new GridLayout(1, 3, 10, 0));
            p.setOpaque(false);
            p.setAlignmentX(LEFT_ALIGNMENT);
            p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
            p.add(col(l1, c1));
            p.add(col(l2, c2));
            p.add(col(l3, c3));
            return p;
        }

        private JPanel filaAncha(JLabel lbl, JComponent comp) {
            JPanel p = new JPanel();
            p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
            p.setOpaque(false);
            p.setAlignmentX(LEFT_ALIGNMENT);
            p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
            lbl.setAlignmentX(LEFT_ALIGNMENT);
            comp.setAlignmentX(LEFT_ALIGNMENT);
            comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
            p.add(lbl);
            p.add(Box.createVerticalStrut(4));
            p.add(comp);
            return p;
        }

        private JPanel col(JLabel lbl, JComponent comp) {
            JPanel p = new JPanel();
            p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
            p.setOpaque(false);
            lbl.setAlignmentX(LEFT_ALIGNMENT);
            comp.setAlignmentX(LEFT_ALIGNMENT);
            comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
            p.add(lbl);
            p.add(Box.createVerticalStrut(4));
            p.add(comp);
            return p;
        }

        private JLabel seccion(String texto) {
            JLabel lbl = new JLabel(texto);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lbl.setForeground(TEXTO_PRINCIPAL);
            lbl.setAlignmentX(LEFT_ALIGNMENT);
            return lbl;
        }

        private JLabel etiqueta(String texto) {
            JLabel lbl = new JLabel(texto);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lbl.setForeground(TEXTO_SEC);
            return lbl;
        }

        private JTextField campo(String placeholder) {
            JTextField tf = new JTextField();
            tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            tf.setBorder(new CompoundBorder(
                    new LineBorder(BORDE_INPUT, 1, true),
                    new EmptyBorder(6, 10, 6, 10)));
            tf.setBackground(FONDO_INPUT);
            tf.putClientProperty("placeholder", placeholder);

            tf.setForeground(new Color(160, 160, 175));
            tf.setText(placeholder);
            tf.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (placeholder.equals(tf.getText())) {
                        tf.setText("");
                        tf.setForeground(TEXTO_PRINCIPAL);
                    }
                }
                @Override
                public void focusLost(FocusEvent e) {
                    if (tf.getText().isEmpty()) {
                        tf.setForeground(new Color(160, 160, 175));
                        tf.setText(placeholder);
                    }
                }
            });
            return tf;
        }

        private String getValor(JTextField campo) {
            String texto = campo.getText().trim();
            String placeholder = (String) campo.getClientProperty("placeholder");
            if (placeholder != null && placeholder.equals(texto)) return "";
            return texto;
        }

        private JComboBox<String> comboBox(String[] opciones) {
            JComboBox<String> cb = new JComboBox<>(opciones);
            cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            cb.setBackground(FONDO_INPUT);
            cb.setForeground(TEXTO_PRINCIPAL);
            cb.setBorder(new LineBorder(BORDE_INPUT, 1, true));
            return cb;
        }
    }

    // ─── INNER: DIÁLOGO DETALLE ───────────────────────────────────────────────

    private class DialogoDetalle extends JDialog {

        DialogoDetalle(JFrame owner, ResidenteDTO dto) {
            super(owner, "Detalle del Residente", true);
            setUndecorated(true);
            construirUI(dto);
            pack();
            setShape(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 22, 22));
            setLocationRelativeTo(owner);
        }

        private void construirUI(ResidenteDTO dto) {
            // ── Contenedor exterior (BorderLayout) ─────────────────────────
            JPanel outer = new JPanel(new BorderLayout());
            outer.setBackground(Color.WHITE);
            outer.setBorder(BorderFactory.createLineBorder(AZUL_PRIMARIO, 2));

            // Cabecera azul
            JPanel headerDet = new JPanel(new BorderLayout());
            headerDet.setBackground(AZUL_PRIMARIO);
            headerDet.setBorder(new EmptyBorder(12, 20, 12, 14));
            JLabel lblTitleDet = new JLabel("Detalle del Residente");
            lblTitleDet.setFont(new Font("Segoe UI", Font.BOLD, 15));
            lblTitleDet.setForeground(Color.WHITE);
            JButton btnXDet = btnCerrarBlanco();
            btnXDet.addActionListener(e -> dispose());
            headerDet.add(lblTitleDet, BorderLayout.WEST);
            headerDet.add(btnXDet, BorderLayout.EAST);
            outer.add(headerDet, BorderLayout.NORTH);

            // ── Contenido desplazable ───────────────────────────────────────
            JPanel root = new JPanel();
            root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
            root.setBackground(Color.WHITE);
            root.setBorder(new EmptyBorder(16, 32, 16, 32));

            String nombreCompleto = (dto.getNombre() != null ? dto.getNombre() : "")
                    + " " + (dto.getApellido_paterno() != null ? dto.getApellido_paterno() : "")
                    + " " + (dto.getApellido_materno() != null ? dto.getApellido_materno() : "");

            root.add(centrado(nombreCompleto.trim(), new Font("Segoe UI", Font.BOLD, 16), TEXTO_PRINCIPAL));
            root.add(Box.createVerticalStrut(6));

            String estadoTexto = dto.getEstado() == EstadoResidenteENUM.ACTIVO ? "Activo" : "Inactivo";
            Color estadoColor = dto.getEstado() == EstadoResidenteENUM.ACTIVO
                    ? new Color(40, 160, 80) : new Color(160, 90, 90);
            root.add(chip(estadoTexto, estadoColor));
            root.add(Box.createVerticalStrut(18));

            root.add(seccionDetalle("Datos Personales"));
            root.add(Box.createVerticalStrut(8));
            root.add(filaDetalle("Matrícula", dto.getId()));
            root.add(filaDetalle("Carrera", dto.getCarrera()));
            root.add(filaDetalle("Correo", dto.getCorreo()));
            root.add(filaDetalle("Teléfono", dto.getTelefono()));
            root.add(filaDetalle("Género", generoTexto(dto.getGenero())));
            root.add(filaDetalle("Fecha nac.", dto.getFechaNacimiento() != null ? dto.getFechaNacimiento().format(FMT) : "-"));
            root.add(filaDetalle("Fecha ingreso", dto.getFechaIngreso() != null ? dto.getFechaIngreso().format(FMT) : "-"));
            root.add(filaDetalle("Habitación", dto.getNumeroHabitacion() != null ? dto.getNumeroHabitacion() : "Sin asignar"));
            root.add(Box.createVerticalStrut(12));

            if (dto.getNombreAval() != null && !dto.getNombreAval().isEmpty()) {
                root.add(seccionDetalle("Aval / Fiador"));
                root.add(Box.createVerticalStrut(8));
                root.add(filaDetalle("Nombre", dto.getNombreAval()));
                root.add(filaDetalle("Parentesco", dto.getParentescoAval()));
                root.add(filaDetalle("Teléfono", dto.getTelefonoAval()));
                root.add(Box.createVerticalStrut(12));
            }

            if (dto.getPermiso_vehicular() != null && dto.getPermiso_vehicular() > 0) {
                root.add(seccionDetalle("Vehículo"));
                root.add(Box.createVerticalStrut(8));
                root.add(filaDetalle("Modelo", dto.getModeloVehiculo()));
                root.add(filaDetalle("Color", dto.getColorVehiculo()));
                root.add(filaDetalle("Placas", dto.getPlacasVehiculo()));
                root.add(Box.createVerticalStrut(12));
            }

            root.add(seccionDetalle("Plan de Pago"));
            root.add(Box.createVerticalStrut(8));
            root.add(filaDetalle("Estado", estadoPagoTexto(dto.getEstadoPago())));
            root.add(filaDetalle("Último pago", dto.getUltimoPago() != null ? dto.getUltimoPago().format(FMT) : "-"));
            root.add(filaDetalle("Adeudo", dto.getAdeudoPendiente() != null
                    ? String.format("$%.2f", dto.getAdeudoPendiente()) : "$0.00"));

            JScrollPane sp = new JScrollPane(root);
            sp.setBorder(BorderFactory.createEmptyBorder());
            sp.getVerticalScrollBar().setUI(new BarraDelgada());
            sp.getVerticalScrollBar().setPreferredSize(new Dimension(5, 0));
            sp.setPreferredSize(new Dimension(420, 500));
            outer.add(sp, BorderLayout.CENTER);

            // Pie con botón Cerrar
            JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            footer.setBackground(Color.WHITE);
            footer.setBorder(new EmptyBorder(0, 20, 16, 20));
            JButton btnCerrar = botonSecundario("Cerrar");
            btnCerrar.setPreferredSize(new Dimension(200, 40));
            btnCerrar.addActionListener(e -> dispose());
            footer.add(btnCerrar);
            outer.add(footer, BorderLayout.SOUTH);

            setContentPane(outer);
        }

        private JLabel centrado(String texto, Font f, Color c) {
            JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
            lbl.setFont(f);
            lbl.setForeground(c);
            lbl.setAlignmentX(CENTER_ALIGNMENT);
            return lbl;
        }

        private JPanel chip(String texto, Color color) {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            p.setOpaque(false);
            p.setAlignmentX(CENTER_ALIGNMENT);
            JLabel lbl = new JLabel(texto, SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(color);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lbl.setForeground(Color.WHITE);
            lbl.setOpaque(false);
            lbl.setBorder(new EmptyBorder(4, 16, 4, 16));
            p.add(lbl);
            return p;
        }

        private JPanel seccionDetalle(String texto) {
            JPanel p = new JPanel(new BorderLayout());
            p.setOpaque(false);
            p.setAlignmentX(CENTER_ALIGNMENT);
            JLabel lbl = new JLabel(texto);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lbl.setForeground(TEXTO_PRINCIPAL);
            p.add(lbl, BorderLayout.WEST);
            return p;
        }

        private JPanel filaDetalle(String clave, String valor) {
            JPanel p = new JPanel(new BorderLayout(8, 0));
            p.setOpaque(false);
            p.setAlignmentX(CENTER_ALIGNMENT);
            p.setBorder(new EmptyBorder(2, 0, 2, 0));
            JLabel k = new JLabel(clave);
            k.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            k.setForeground(TEXTO_SEC);
            k.setPreferredSize(new Dimension(110, 18));
            JLabel v = new JLabel(valor != null ? valor : "-");
            v.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            v.setForeground(TEXTO_PRINCIPAL);
            p.add(k, BorderLayout.WEST);
            p.add(v, BorderLayout.CENTER);
            return p;
        }

        private String generoTexto(GeneroENUM g) {
            if (g == null) return "-";
            return switch (g) {
                case HOMBRE -> "Masculino";
                case MUJER -> "Femenino";
                default -> "Otro";
            };
        }

        private String estadoPagoTexto(EstadoPagoENUM e) {
            if (e == null) return "-";
            return switch (e) {
                case AL_CORRIENTE -> "Al corriente";
                case CON_DEUDA -> "Con deuda";
                case MOROSO -> "Moroso";
            };
        }
    }

    // ─── HELPERS GLOBALES ────────────────────────────────────────────────────

    private JButton botonPrimario(String texto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? AZUL_HOVER : AZUL_PRIMARIO);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton botonSecundario(String texto) {
        return new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(245, 242, 255));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }

            {
                setFont(new Font("Segoe UI", Font.PLAIN, 13));
                setForeground(AZUL_PRIMARIO);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setFocusPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        };
    }

    // Botón X blanco para cabeceras azules de diálogo
    private JButton btnCerrarBlanco() {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 35));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int cx = getWidth() / 2, cy = getHeight() / 2;
                g2.drawLine(cx - 5, cy - 5, cx + 5, cy + 5);
                g2.drawLine(cx + 5, cy - 5, cx - 5, cy + 5);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(28, 28));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ─── INNER: DATE PICKER FIELD ─────────────────────────────────────────────

    private class DatePickerField extends JPanel {
        private final JTextField displayField;
        private LocalDate selectedDate;
        private final boolean noFuturo;
        private final String placeholder;

        DatePickerField(String placeholder, boolean noFuturo) {
            this.placeholder = placeholder;
            this.noFuturo    = noFuturo;
            setLayout(new BorderLayout());
            setOpaque(false);

            // Contenedor con borde
            JPanel fieldContainer = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(FONDO_INPUT);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 5);
                    g2.dispose();
                }
            };
            fieldContainer.setOpaque(false);
            fieldContainer.setBorder(new LineBorder(BORDE_INPUT, 1, true));

            displayField = new JTextField();
            displayField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            displayField.setBorder(new EmptyBorder(6, 10, 6, 4));
            displayField.setOpaque(false);
            displayField.setEditable(false);
            displayField.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            displayField.setText(placeholder);
            displayField.setForeground(new Color(160, 160, 175));

            // Botón calendario dibujado con Graphics2D
            JButton calBtn = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(AZUL_PRIMARIO);
                    g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    int m = 5, w = getWidth() - m * 2, h = getHeight() - m * 2;
                    g2.drawRoundRect(m, m, w, h, 3, 3);
                    g2.drawLine(m, m + 6, m + w, m + 6);
                    g2.fillOval(m + w / 4 - 1,     m + 10, 3, 3);
                    g2.fillOval(m + w / 2 - 1,     m + 10, 3, 3);
                    g2.fillOval(m + 3 * w / 4 - 1, m + 10, 3, 3);
                    g2.fillOval(m + w / 4 - 1,     m + 16, 3, 3);
                    g2.fillOval(m + w / 2 - 1,     m + 16, 3, 3);
                    g2.dispose();
                }
            };
            calBtn.setPreferredSize(new Dimension(30, 30));
            calBtn.setContentAreaFilled(false);
            calBtn.setBorderPainted(false);
            calBtn.setFocusPainted(false);
            calBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            fieldContainer.add(displayField, BorderLayout.CENTER);
            fieldContainer.add(calBtn, BorderLayout.EAST);
            add(fieldContainer, BorderLayout.CENTER);

            displayField.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) { mostrarCalendario(); }
            });
            calBtn.addActionListener(e -> mostrarCalendario());
        }

        private void mostrarCalendario() {
            Window owner = SwingUtilities.getWindowAncestor(this);
            new CalendarioPopup(owner, selectedDate, noFuturo, date -> {
                selectedDate = date;
                displayField.setText(date.format(FMT));
                displayField.setForeground(TEXTO_PRINCIPAL);
            }).setVisible(true);
        }

        LocalDate getDate() { return selectedDate; }

        void setDate(LocalDate date) {
            selectedDate = date;
            if (date != null) {
                displayField.setText(date.format(FMT));
                displayField.setForeground(TEXTO_PRINCIPAL);
            } else {
                displayField.setText(placeholder);
                displayField.setForeground(new Color(160, 160, 175));
            }
        }
    }

    // ─── INNER: CALENDARIO POPUP ──────────────────────────────────────────────

    private class CalendarioPopup extends JDialog {
        private int mes, anio;
        private LocalDate seleccionada;
        private final boolean noFuturo;
        private final Consumer<LocalDate> onSelect;
        private JLabel lblMesAnio;
        private JPanel gridPanel;

        CalendarioPopup(Window owner, LocalDate inicial, boolean noFuturo, Consumer<LocalDate> onSelect) {
            super(owner, Dialog.ModalityType.APPLICATION_MODAL);
            this.noFuturo = noFuturo;
            this.onSelect = onSelect;
            LocalDate ref = inicial != null ? inicial : LocalDate.now();
            this.mes = ref.getMonthValue();
            this.anio = ref.getYear();
            this.seleccionada = inicial;
            setUndecorated(true);
            construirUI();
            pack();
            setShape(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));
            setLocationRelativeTo(owner);
        }

        private void construirUI() {
            JPanel root = new JPanel(new BorderLayout(0, 8));
            root.setBackground(Color.WHITE);
            root.setBorder(new CompoundBorder(
                    new LineBorder(AZUL_PRIMARIO, 2, true),
                    new EmptyBorder(12, 14, 12, 14)));

            // Navegación mes/año
            JPanel navBar = new JPanel(new BorderLayout(6, 0));
            navBar.setOpaque(false);
            JButton prev = calNavBtn("‹");
            JButton next = calNavBtn("›");
            lblMesAnio = new JLabel("", SwingConstants.CENTER);
            lblMesAnio.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblMesAnio.setForeground(TEXTO_PRINCIPAL);
            prev.addActionListener(e -> cambiarMes(-1));
            next.addActionListener(e -> cambiarMes(1));
            navBar.add(prev, BorderLayout.WEST);
            navBar.add(lblMesAnio, BorderLayout.CENTER);
            navBar.add(next, BorderLayout.EAST);

            // Nombres de días
            JPanel semana = new JPanel(new GridLayout(1, 7, 4, 0));
            semana.setOpaque(false);
            semana.setBorder(new EmptyBorder(4, 0, 2, 0));
            for (String d : new String[]{"Lu", "Ma", "Mi", "Ju", "Vi", "Sa", "Do"}) {
                JLabel l = new JLabel(d, SwingConstants.CENTER);
                l.setFont(new Font("Segoe UI", Font.BOLD, 11));
                l.setForeground(TEXTO_SEC);
                semana.add(l);
            }

            gridPanel = new JPanel(new GridLayout(6, 7, 4, 3));
            gridPanel.setOpaque(false);

            JPanel calContent = new JPanel(new BorderLayout(0, 0));
            calContent.setOpaque(false);
            calContent.add(semana, BorderLayout.NORTH);
            calContent.add(gridPanel, BorderLayout.CENTER);

            // Pie
            JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
            footer.setOpaque(false);
            footer.setBorder(new EmptyBorder(4, 0, 0, 0));
            JButton btnHoy     = calEnlace("Hoy");
            JButton btnCerrarC = calEnlace("Cancelar");
            btnHoy.addActionListener(e -> seleccionar(LocalDate.now()));
            btnCerrarC.addActionListener(e -> dispose());
            JLabel sep = new JLabel("|");
            sep.setForeground(TEXTO_SEC);
            footer.add(btnHoy);
            footer.add(sep);
            footer.add(btnCerrarC);

            root.add(navBar, BorderLayout.NORTH);
            root.add(calContent, BorderLayout.CENTER);
            root.add(footer, BorderLayout.SOUTH);
            setContentPane(root);
            actualizarGrid();
        }

        private JButton calNavBtn(String txt) {
            JButton b = new JButton(txt);
            b.setFont(new Font("Segoe UI", Font.BOLD, 18));
            b.setForeground(AZUL_PRIMARIO);
            b.setContentAreaFilled(false);
            b.setBorderPainted(false);
            b.setFocusPainted(false);
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            b.setPreferredSize(new Dimension(28, 26));
            return b;
        }

        private JButton calEnlace(String txt) {
            JButton b = new JButton(txt);
            b.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            b.setForeground(AZUL_PRIMARIO);
            b.setContentAreaFilled(false);
            b.setBorderPainted(false);
            b.setFocusPainted(false);
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            return b;
        }

        private void cambiarMes(int delta) {
            mes += delta;
            if (mes > 12) { mes = 1;  anio++; }
            if (mes < 1)  { mes = 12; anio--; }
            actualizarGrid();
        }

        private void actualizarGrid() {
            String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                              "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
            lblMesAnio.setText(meses[mes - 1] + " " + anio);
            gridPanel.removeAll();

            LocalDate primero = LocalDate.of(anio, mes, 1);
            LocalDate hoy     = LocalDate.now();
            int startDow = primero.getDayOfWeek().getValue(); // 1=Lun … 7=Dom

            for (int i = 1; i < startDow; i++) gridPanel.add(new JLabel(""));

            int diasMes = primero.lengthOfMonth();
            for (int d = 1; d <= diasMes; d++) {
                LocalDate fecha    = LocalDate.of(anio, mes, d);
                boolean bloqueada  = noFuturo && fecha.isAfter(hoy);
                boolean esHoy      = fecha.equals(hoy);
                boolean esSel      = fecha.equals(seleccionada);

                JButton btn = new JButton(String.valueOf(d)) {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        if (esSel) {
                            g2.setColor(AZUL_PRIMARIO);
                            g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                        } else if (esHoy) {
                            g2.setColor(new Color(55, 75, 190, 30));
                            g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                        }
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };
                btn.setFont(new Font("Segoe UI", esSel ? Font.BOLD : Font.PLAIN, 12));
                btn.setForeground(bloqueada ? new Color(200, 195, 220)
                        : esSel ? Color.WHITE : TEXTO_PRINCIPAL);
                btn.setContentAreaFilled(false);
                btn.setBorderPainted(false);
                btn.setFocusPainted(false);
                btn.setHorizontalAlignment(SwingConstants.CENTER);
                btn.setPreferredSize(new Dimension(28, 25));
                if (!bloqueada) {
                    btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    btn.addActionListener(ev -> seleccionar(fecha));
                }
                gridPanel.add(btn);
            }

            int total = (startDow - 1) + diasMes;
            int rem   = total % 7 == 0 ? 0 : 7 - (total % 7);
            for (int i = 0; i < rem; i++) gridPanel.add(new JLabel(""));

            gridPanel.revalidate();
            gridPanel.repaint();
        }

        private void seleccionar(LocalDate fecha) {
            if (noFuturo && fecha.isAfter(LocalDate.now())) return;
            onSelect.accept(fecha);
            dispose();
        }
    }

    private static class BarraDelgada extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            thumbColor = new Color(180, 160, 220, 160);
            trackColor = new Color(0, 0, 0, 0);
        }
        @Override protected JButton createDecreaseButton(int o) { return vacio(); }
        @Override protected JButton createIncreaseButton(int o) { return vacio(); }
        private static JButton vacio() {
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(0, 0));
            return b;
        }
        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);
            g2.fillRoundRect(r.x + 1, r.y, r.width - 2, r.height, 5, 5);
            g2.dispose();
        }
        @Override protected void paintTrack(Graphics g, JComponent c, Rectangle r) {}
    }
}
