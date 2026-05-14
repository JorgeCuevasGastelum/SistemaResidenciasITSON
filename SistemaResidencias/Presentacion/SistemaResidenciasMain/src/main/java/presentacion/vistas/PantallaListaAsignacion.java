package presentacion.vistas;

import dtos.AsignacionReporteDTO;
import enums.EstadoPagoENUM;
import enums.GeneroENUM;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import presentacion.control.ListaAsignacionControl;

public class PantallaListaAsignacion extends JPanel {

    // ── Paleta ────────────────────────────────────────────────────────────────
    private static final Color AZUL_PRIMARIO = new Color(55,  75, 190);
    private static final Color AZUL_HOVER    = new Color(45,  60, 160);
    private static final Color AZUL_CLARO    = new Color(235, 238, 255);
    private static final Color VERDE         = new Color(40,  160,  80);
    private static final Color VERDE_CLARO   = new Color(230, 255, 235);
    private static final Color AMBAR         = new Color(170, 110,  10);
    private static final Color AMBAR_CLARO   = new Color(255, 245, 210);
    private static final Color ROJO          = new Color(200,  40,  40);
    private static final Color ROJO_CLARO    = new Color(255, 235, 235);
    private static final Color LILA_STAT     = new Color(120,  70, 200);
    private static final Color LILA_CLARO    = new Color(240, 232, 255);
    private static final Color TEXTO_PRINCIPAL = new Color(40,  40,  50);
    private static final Color TEXTO_SEC     = new Color(110, 110, 120);
    private static final Color FONDO_FILTROS = new Color(248, 247, 254);
    private static final Color BORDE_FILTROS = new Color(220, 215, 245);
    private static final Color GRIS_LINEA    = new Color(238, 235, 250);
    private static final Color FONDO_TABLA   = new Color(252, 251, 255);

    // ── Componentes ───────────────────────────────────────────────────────────
    private ListaAsignacionControl control;

    private JLabel lblOcupadas, lblDisponibles, lblAsignados, lblMantenimiento;

    private JComboBox<String> comboPiso;
    private JComboBox<String> comboGenero;
    private JComboBox<String> comboEstatus;
    private JComboBox<String> comboCiclo;

    private ListaTableModel modelo;
    private JTable tabla;
    private List<AsignacionReporteDTO> listaActual = new ArrayList<>();

    // ─────────────────────────────────────────────────────────────────────────

    public PantallaListaAsignacion() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        construirUI();
    }

    public void setControl(ListaAsignacionControl control) {
        this.control = control;
        control.setVista(this);   // wire the reverse reference so the control can reach this panel
    }

    public void reiniciar() {
        if (control != null) control.cargarLista();
    }

    // ── Construcción de UI ────────────────────────────────────────────────────

    private void construirUI() {
        // ── NORTH: header + stats ────────────────────────────────────────────
        JPanel norte = new JPanel();
        norte.setLayout(new BoxLayout(norte, BoxLayout.Y_AXIS));
        norte.setBackground(Color.WHITE);
        norte.setBorder(new EmptyBorder(28, 32, 16, 32));

        norte.add(construirHeader());
        norte.add(Box.createVerticalStrut(16));
        norte.add(construirStats());
        norte.add(Box.createVerticalStrut(4));

        add(norte, BorderLayout.NORTH);

        // ── CENTER: tabla (izquierda) + filtros (derecha) ────────────────────
        JPanel centro = new JPanel(new BorderLayout(0, 0));
        centro.setBackground(Color.WHITE);
        centro.setBorder(new EmptyBorder(0, 32, 24, 32));

        centro.add(construirPanelFiltros(), BorderLayout.WEST);
        centro.add(construirPanelTabla(),   BorderLayout.CENTER);

        add(centro, BorderLayout.CENTER);
    }

    // ── Header ────────────────────────────────────────────────────────────────

    private JPanel construirHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));

        JLabel titulo = new JLabel("Generar Lista de Asignación");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(TEXTO_PRINCIPAL);

        JLabel subtitulo = new JLabel("Visualiza y exporta el listado de residentes asignados a habitaciones");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitulo.setForeground(TEXTO_SEC);

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setOpaque(false);
        textos.add(titulo);
        textos.add(Box.createVerticalStrut(2));
        textos.add(subtitulo);

        p.add(textos, BorderLayout.WEST);
        return p;
    }

    // ── Stats cards ───────────────────────────────────────────────────────────

    private JPanel construirStats() {
        JPanel p = new JPanel(new GridLayout(1, 4, 12, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 88));

        lblOcupadas      = new JLabel("0");
        lblDisponibles   = new JLabel("0");
        lblAsignados     = new JLabel("0");
        lblMantenimiento = new JLabel("0");

        p.add(tarjeta("Habitaciones ocupadas",    lblOcupadas,      AZUL_PRIMARIO, AZUL_CLARO));
        p.add(tarjeta("Habitaciones disponibles", lblDisponibles,   VERDE,         VERDE_CLARO));
        p.add(tarjeta("Residentes asignados",     lblAsignados,     LILA_STAT,     LILA_CLARO));
        p.add(tarjeta("En mantenimiento",         lblMantenimiento, TEXTO_SEC,     new Color(245, 245, 248)));
        return p;
    }

    private JPanel tarjeta(String etiqueta, JLabel numero, Color colorNum, Color colorFondo) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(colorFondo);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.setColor(colorFondo.darker());
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout(0, 2));
        card.setBorder(new EmptyBorder(14, 18, 14, 18));

        numero.setFont(new Font("Segoe UI", Font.BOLD, 30));
        numero.setForeground(colorNum);

        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(TEXTO_SEC);

        card.add(numero, BorderLayout.CENTER);
        card.add(lbl,    BorderLayout.SOUTH);
        return card;
    }

    // ── Panel tabla (izquierda) ───────────────────────────────────────────────

    private JPanel construirPanelTabla() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(0, 12, 0, 0));

        // ── Barra superior: título + botones de export ──────────────────────
        JPanel barraTop = new JPanel(new BorderLayout());
        barraTop.setOpaque(false);
        barraTop.setBorder(new EmptyBorder(0, 0, 8, 0));

        JLabel lblTitTabla = new JLabel("Lista Consolidada de Residentes");
        lblTitTabla.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitTabla.setForeground(TEXTO_PRINCIPAL);

        JPanel botonesExport = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botonesExport.setOpaque(false);

        JButton btnPdf   = botonExport("Generar PDF",    new Color(200, 40,  40));
        JButton btnExcel = botonExport("Exportar Excel", new Color(33, 115, 70));
        JButton btnPrint = botonExport("Imprimir",       AZUL_PRIMARIO);

        btnPdf.addActionListener(e   -> { if (control != null) control.exportarPdf(listaActual); });
        btnExcel.addActionListener(e -> { if (control != null) control.exportarExcel(listaActual); });
        btnPrint.addActionListener(e -> { if (control != null) control.imprimir(listaActual); });

        botonesExport.add(btnPdf);
        botonesExport.add(btnExcel);
        botonesExport.add(btnPrint);

        barraTop.add(lblTitTabla,    BorderLayout.WEST);
        barraTop.add(botonesExport,  BorderLayout.EAST);

        p.add(barraTop, BorderLayout.NORTH);
        p.add(construirTabla(), BorderLayout.CENTER);
        return p;
    }

    private JScrollPane construirTabla() {
        modelo = new ListaTableModel();
        tabla  = new JTable(modelo) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }

            @Override
            public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : FONDO_TABLA);
                }
                return c;
            }
        };

        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setRowHeight(48);
        tabla.setShowHorizontalLines(true);
        tabla.setGridColor(GRIS_LINEA);
        tabla.setShowVerticalLines(false);
        tabla.setSelectionBackground(AZUL_CLARO);
        tabla.setSelectionForeground(TEXTO_PRINCIPAL);
        tabla.setIntercellSpacing(new Dimension(0, 0));
        tabla.setBackground(Color.WHITE);

        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.getTableHeader().setBackground(AZUL_PRIMARIO);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setPreferredSize(new Dimension(0, 40));
        tabla.getTableHeader().setReorderingAllowed(false);

        // Anchos de columnas
        int[] anchos = {110, 230, 210, 90, 120};
        String[] nombres = {"Habitación", "Nombre", "Carrera", "Piso", "Estado de pago"};
        for (int i = 0; i < anchos.length; i++) {
            tabla.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
            tabla.getColumnModel().getColumn(i).setHeaderValue(nombres[i]);
        }

        // Renderers
        tabla.getColumnModel().getColumn(1).setCellRenderer(textoPad());
        tabla.getColumnModel().getColumn(2).setCellRenderer(textoPad());
        tabla.getColumnModel().getColumn(3).setCellRenderer(centroPad());
        tabla.getColumnModel().getColumn(0).setCellRenderer(new HabitacionChipRenderer());
        tabla.getColumnModel().getColumn(4).setCellRenderer(new EstadoPagoChipRenderer());

        JScrollPane sp = new JScrollPane(tabla);
        sp.setBorder(BorderFactory.createLineBorder(GRIS_LINEA));
        sp.setBackground(Color.WHITE);
        sp.getViewport().setBackground(Color.WHITE);
        estilizarScrollBar(sp);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        sp.getVerticalScrollBar().setBlockIncrement(64);
        return sp;
    }

    // ── Panel filtros (derecha) ───────────────────────────────────────────────

    private JPanel construirPanelFiltros() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FONDO_FILTROS);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.setColor(BORDE_FILTROS);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 18, 20, 18));
        card.setPreferredSize(new Dimension(252, 0));

        // Título "Filtros"
        JLabel lblTit = new JLabel("Filtros");
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTit.setForeground(AZUL_PRIMARIO);
        lblTit.setAlignmentX(LEFT_ALIGNMENT);
        card.add(lblTit);
        card.add(Box.createVerticalStrut(16));

        // Filtros
        comboPiso    = comboFiltro();
        comboGenero  = comboFiltro();
        comboEstatus = comboFiltro();
        comboCiclo   = comboFiltro();

        card.add(seccionFiltro("Por piso",        comboPiso));
        card.add(Box.createVerticalStrut(12));
        card.add(seccionFiltro("Por género",      comboGenero));
        card.add(Box.createVerticalStrut(12));
        card.add(seccionFiltro("Por estatus",     comboEstatus));
        card.add(Box.createVerticalStrut(12));
        card.add(seccionFiltro("Ciclo escolar",   comboCiclo));
        card.add(Box.createVerticalGlue());
        card.add(Box.createVerticalStrut(24));

        // Botones
        JButton btnAplicar = botonPrimarioAncho("Aplicar filtros");
        btnAplicar.addActionListener(e -> aplicarFiltros());
        btnAplicar.setAlignmentX(LEFT_ALIGNMENT);
        card.add(btnAplicar);
        card.add(Box.createVerticalStrut(8));

        JButton btnLimpiar = botonSecundarioAncho("Limpiar filtros");
        btnLimpiar.addActionListener(e -> { if (control != null) control.limpiarFiltros(); });
        btnLimpiar.setAlignmentX(LEFT_ALIGNMENT);
        card.add(btnLimpiar);

        // Llenar opciones
        comboPiso.addItem("Todos los pisos");
        comboPiso.addItem("1er piso");
        comboPiso.addItem("2do piso");
        comboPiso.addItem("3er piso");

        comboGenero.addItem("Todos los géneros");
        comboGenero.addItem("Hombres");
        comboGenero.addItem("Mujeres");

        comboEstatus.addItem("Todos los estados");
        comboEstatus.addItem("Al corriente");
        comboEstatus.addItem("Con deuda");
        comboEstatus.addItem("Moroso");

        comboCiclo.addItem("Todos los ciclos");

        return card;
    }

    private JPanel seccionFiltro(String etiqueta, JComboBox<String> combo) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(TEXTO_SEC);
        lbl.setAlignmentX(LEFT_ALIGNMENT);

        combo.setAlignmentX(LEFT_ALIGNMENT);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));

        p.add(lbl);
        p.add(Box.createVerticalStrut(4));
        p.add(combo);
        return p;
    }

    // ── Actualización de datos ────────────────────────────────────────────────

    public void mostrarLista(List<AsignacionReporteDTO> lista) {
        this.listaActual = lista != null ? lista : new ArrayList<>();
        modelo.setDatos(this.listaActual);
    }

    public void actualizarStats(int ocupadas, int disponibles, int asignados) {
        lblOcupadas.setText(String.valueOf(ocupadas));
        lblDisponibles.setText(String.valueOf(disponibles));
        lblAsignados.setText(String.valueOf(asignados));
        lblMantenimiento.setText("0");
    }

    public void actualizarCiclosDisponibles(List<String> ciclos) {
        String sel = (String) comboCiclo.getSelectedItem();
        comboCiclo.removeAllItems();
        comboCiclo.addItem("Todos los ciclos");
        ciclos.forEach(comboCiclo::addItem);
        if (sel != null) comboCiclo.setSelectedItem(sel);
    }

    public void limpiarFiltros() {
        comboPiso.setSelectedIndex(0);
        comboGenero.setSelectedIndex(0);
        comboEstatus.setSelectedIndex(0);
        comboCiclo.setSelectedIndex(0);
    }

    // ── Lógica de filtros ─────────────────────────────────────────────────────

    private void aplicarFiltros() {
        if (control == null) return;

        Integer piso = switch (comboPiso.getSelectedIndex()) {
            case 1 -> 1; case 2 -> 2; case 3 -> 3; default -> null;
        };
        GeneroENUM genero = switch (comboGenero.getSelectedIndex()) {
            case 1 -> GeneroENUM.HOMBRE;
            case 2 -> GeneroENUM.MUJER;
            default -> null;
        };
        EstadoPagoENUM estado = switch (comboEstatus.getSelectedIndex()) {
            case 1 -> EstadoPagoENUM.AL_CORRIENTE;
            case 2 -> EstadoPagoENUM.CON_DEUDA;
            case 3 -> EstadoPagoENUM.MOROSO;
            default -> null;
        };
        String ciclo = comboCiclo.getSelectedIndex() == 0
                ? null : (String) comboCiclo.getSelectedItem();

        control.aplicarFiltros(piso, genero, estado, ciclo);
    }

    // ── Helpers de botones / combos ───────────────────────────────────────────

    private JComboBox<String> comboFiltro() {
        JComboBox<String> cb = new JComboBox<>();
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cb.setBackground(Color.WHITE);
        cb.setForeground(TEXTO_PRINCIPAL);
        cb.setFocusable(false);
        return cb;
    }

    private JButton botonExport(String texto, Color color) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? color.darker() : color);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 32));
        return btn;
    }

    private JButton botonPrimarioAncho(String texto) {
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
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setPreferredSize(new Dimension(216, 40));
        return btn;
    }

    private JButton botonSecundarioAncho(String texto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(235, 230, 255) : new Color(245, 242, 255));
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(AZUL_PRIMARIO);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btn.setPreferredSize(new Dimension(216, 38));
        return btn;
    }

    private DefaultTableCellRenderer textoPad() {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setBorder(new EmptyBorder(0, 16, 0, 8));
        r.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        r.setForeground(TEXTO_PRINCIPAL);
        return r;
    }

    private DefaultTableCellRenderer centroPad() {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(SwingConstants.CENTER);
        r.setBorder(new EmptyBorder(0, 8, 0, 8));
        r.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        r.setForeground(TEXTO_SEC);
        return r;
    }

    // ── Scroll bar styling ────────────────────────────────────────────────────

    private void estilizarScrollBar(JScrollPane sp) {
        sp.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor = new Color(180, 160, 220, 160);
                trackColor = new Color(0, 0, 0, 0);
            }
            @Override protected JButton createDecreaseButton(int o) { return botVacio(); }
            @Override protected JButton createIncreaseButton(int o) { return botVacio(); }
            private JButton botVacio() {
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
        });
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(5, 0));
    }

    // ── Table Model ───────────────────────────────────────────────────────────

    private static final String[] COLUMNAS = {"Habitación", "Nombre", "Carrera", "Piso", "Estado de pago"};

    class ListaTableModel extends AbstractTableModel {

        private List<AsignacionReporteDTO> datos = new ArrayList<>();

        void setDatos(List<AsignacionReporteDTO> lista) {
            this.datos = lista;
            fireTableDataChanged();
        }

        @Override public int getRowCount()    { return datos.size(); }
        @Override public int getColumnCount() { return COLUMNAS.length; }
        @Override public String getColumnName(int col) { return COLUMNAS[col]; }

        @Override
        public Object getValueAt(int row, int col) {
            AsignacionReporteDTO d = datos.get(row);
            return switch (col) {
                case 0 -> d.getHabitacionTexto();
                case 1 -> d.getNombreCompleto().trim();
                case 2 -> d.getCarrera() != null ? d.getCarrera() : "—";
                case 3 -> d.getPisoTexto();
                case 4 -> d;        // DTO completo → chip renderer
                default -> "";
            };
        }
    }

    // ── Chip Renderers ────────────────────────────────────────────────────────

    /** Chip azul para Habitación */
    private class HabitacionChipRenderer extends JPanel implements TableCellRenderer {

        private final JLabel lbl = new JLabel();

        HabitacionChipRenderer() {
            setLayout(new GridBagLayout());
            setOpaque(false);

            JPanel chip = new JPanel(new GridBagLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(AZUL_CLARO);
                    g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
                    g2.setColor(AZUL_PRIMARIO);
                    g2.setStroke(new BasicStroke(1.2f));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
                    g2.dispose();
                }
            };
            chip.setOpaque(false);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lbl.setForeground(AZUL_PRIMARIO);
            chip.setBorder(new EmptyBorder(4, 12, 4, 12));
            chip.add(lbl);
            add(chip);
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v,
                boolean sel, boolean foc, int row, int col) {
            lbl.setText(v != null ? v.toString() : "—");
            setBackground(sel ? t.getSelectionBackground()
                    : row % 2 == 0 ? Color.WHITE : FONDO_TABLA);
            setOpaque(true);
            return this;
        }
    }

    /** Chip de color dinámico para Estado de pago */
    private class EstadoPagoChipRenderer extends JPanel implements TableCellRenderer {

        private final JLabel lbl = new JLabel();
        private Color chipColor = VERDE;
        private Color chipFondo = VERDE_CLARO;

        EstadoPagoChipRenderer() {
            setLayout(new GridBagLayout());
            setOpaque(false);

            JPanel chip = new JPanel(new GridBagLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(chipFondo);
                    g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
                    g2.setColor(chipColor);
                    g2.setStroke(new BasicStroke(1.2f));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
                    g2.dispose();
                }
            };
            chip.setOpaque(false);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            chip.setBorder(new EmptyBorder(4, 12, 4, 12));
            chip.add(lbl);
            add(chip);
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v,
                boolean sel, boolean foc, int row, int col) {
            EstadoPagoENUM ep = null;
            String texto = "—";
            if (v instanceof AsignacionReporteDTO d) {
                ep    = d.getEstadoPago();
                texto = d.getEstadoPagoTexto();
            }
            lbl.setText(texto);
            if (ep == null) {
                chipColor = TEXTO_SEC;
                chipFondo = new Color(245, 245, 248);
            } else switch (ep) {
                case AL_CORRIENTE -> { chipColor = VERDE;  chipFondo = VERDE_CLARO; }
                case CON_DEUDA    -> { chipColor = AMBAR;  chipFondo = AMBAR_CLARO; }
                case MOROSO       -> { chipColor = ROJO;   chipFondo = ROJO_CLARO;  }
            }
            lbl.setForeground(chipColor);
            setBackground(sel ? t.getSelectionBackground()
                    : row % 2 == 0 ? Color.WHITE : FONDO_TABLA);
            setOpaque(true);
            return this;
        }
    }
}
