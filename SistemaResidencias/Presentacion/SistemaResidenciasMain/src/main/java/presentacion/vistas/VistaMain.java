/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package presentacion.vistas;

import dtos.HabitacionDTO;
import dtos.ResidenteDTO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import presentacion.control.AsignarHabitacionesControl;
import presentacion.vistas.componentes.HabitacionCardPanel;
import presentacion.vistas.componentes.ResidenteCardPanel;
import presentacion.vistas.componentes.SidebarItem;
import presentacion.vistas.componentes.SidebarPanel;

/**
 *
 * @author Dana Chavez
 */
public class VistaMain extends javax.swing.JFrame {

    private static final Color MORADO = new Color(120, 80, 200);
    private static final Color MORADO_HOVER = new Color(100, 60, 180);
    private static final Color EXITO = new Color(40, 180, 100);
    private static final Color TEXTO_PRINCIPAL = new Color(30, 30, 40);
    private static final Color TEXTO_SEC = new Color(120, 120, 135);
    private static final Color BORDE_INPUT = new Color(210, 205, 230);
    private static final Color FONDO_INPUT = new Color(250, 249, 255);

    private AsignarHabitacionesControl control;
    private SidebarPanel sidebar;

    private final List<ResidenteCardPanel> tarjetasResidentes = new ArrayList<>();
    private final List<HabitacionCardPanel> tarjetasHabitaciones = new ArrayList<>();

    private JPanel confirmacionPanel;

    public VistaMain() {
        initComponents();
        aplicarEstilos();
    }

    public void setControl(AsignarHabitacionesControl control) {
        this.control = control;
    }

    private void aplicarEstilos() {
        hacerTransparente(jScrollPaneResidentes);
        hacerTransparente(jScrollPaneHabitacion);

        jPanelResidentes.setOpaque(false);
        jPanelResidentes.setLayout(new BoxLayout(jPanelResidentes, BoxLayout.Y_AXIS));

        jPanelHabitacion.setOpaque(false);
        jPanelHabitacion.setLayout(new BoxLayout(jPanelHabitacion, BoxLayout.Y_AXIS));

        jPanelSideBar.setLayout(new BorderLayout());

        sidebar = new SidebarPanel(
                List.of(
                        new SidebarItem("Administrar Residentes", "administrar_residentes"),
                        new SidebarItem("Asignar Habitaciones", "asignar_habitaciones"),
                        new SidebarItem("Generar referencia", "generar_referencia"),
                        new SidebarItem("Gestionar áreas", "gestionar_areas"),
                        new SidebarItem("Mantenimiento", "mantenimiento"),
                        new SidebarItem("Gestionar habitaciones", "gestionar_habitaciones"),
                        new SidebarItem("Generar documentación", "generar_documentacion"),
                        new SidebarItem("Registrar cita", "registrar_cita"),
                        new SidebarItem("Administrar usuarios", "administrar_usuarios"),
                        new SidebarItem("Generar listas", "generar_listas"),
                        new SidebarItem("Buscar residentes", "buscar_residentes"),
                        new SidebarItem("Otras opciones...", "otras_opciones")
                )
        );

        sidebar.setOnNavegacion(clave -> {
            if ("asignar_habitaciones".equals(clave)) {
                control.cargarResidentes();
            }
        });
        sidebar.setActivo("asignar_habitaciones");

        jPanelSideBar.add(sidebar, BorderLayout.CENTER);

        jPanelSeleccion.setOpaque(false);
        jPanelSeleccion.setLayout(new BorderLayout());
        construirPanelConfirmacion();

        estilizarTextField(buscadorResidentes, "Buscar residente...");
        estilizarComboBox(jComboBoxFiltroResidentes,
                new String[]{"Mostrar todos", "Femenino", "Masculino"});
        estilizarComboBox(jComboBoxFiltroHabitacion,
                new String[]{"Todos los pisos", "1er piso", "2do piso", "3er piso"});
    }

    private void hacerTransparente(JScrollPane sp) {
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setBorder(BorderFactory.createEmptyBorder());
        // Scrollbar minimalista
        sp.getVerticalScrollBar().setUI(new ScrollBarDelgada());
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(5, 0));
        sp.getVerticalScrollBar().setOpaque(false);
    }

    private void estilizarTextField(JTextField tf, String placeholder) {
        tf.setText(placeholder);
        tf.setForeground(TEXTO_SEC);
        tf.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        tf.setOpaque(true);
        tf.setBackground(FONDO_INPUT);
        tf.setBorder(BorderFactory.createCompoundBorder(
                new BordereRedondeado(BORDE_INPUT, 10),
                new EmptyBorder(0, 10, 0, 10)));
        tf.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (tf.getText().equals(placeholder)) {
                    tf.setText("");
                    tf.setForeground(TEXTO_PRINCIPAL);
                    tf.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (tf.getText().isEmpty()) {
                    tf.setText(placeholder);
                    tf.setForeground(TEXTO_SEC);
                    tf.setFont(new Font("Segoe UI", Font.ITALIC, 12));
                }
            }
        });
    }

    private void estilizarComboBox(JComboBox<String> cb, String[] opciones) {
        cb.setModel(new DefaultComboBoxModel<>(opciones));
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cb.setForeground(TEXTO_PRINCIPAL);
        cb.setBackground(FONDO_INPUT);
        cb.setBorder(new BordereRedondeado(BORDE_INPUT, 10));
        cb.setUI(new ComboUI());
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                lbl.setBorder(new EmptyBorder(6, 10, 6, 10));
                lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                lbl.setBackground(isSelected ? new Color(240, 235, 255) : Color.WHITE);
                lbl.setForeground(isSelected ? MORADO : TEXTO_PRINCIPAL);
                return lbl;
            }
        });
    }

    private void construirPanelConfirmacion() {
        confirmacionPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 230));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        confirmacionPanel.setOpaque(false);
        confirmacionPanel.setLayout(new BoxLayout(confirmacionPanel, BoxLayout.Y_AXIS));
        confirmacionPanel.setBorder(new EmptyBorder(14, 14, 14, 14));
        jPanelSeleccion.add(confirmacionPanel, BorderLayout.CENTER);
        mostrarPlaceholderConfirmacion();
    }

    private void mostrarPlaceholderConfirmacion() {
        confirmacionPanel.removeAll();
        JLabel lbl = new JLabel(
                "<html><center><span style='color:#787888'>Completa los pasos<br>anteriores</span></center></html>",
                SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lbl.setAlignmentX(CENTER_ALIGNMENT);
        confirmacionPanel.add(Box.createVerticalGlue());
        confirmacionPanel.add(lbl);
        confirmacionPanel.add(Box.createVerticalGlue());
        confirmacionPanel.revalidate();
        confirmacionPanel.repaint();
    }

    public void mostrarResidentes(List<ResidenteDTO> residentes) {
        tarjetasResidentes.clear();
        jPanelResidentes.removeAll();
        for (ResidenteDTO r : residentes) {
            ResidenteCardPanel card = new ResidenteCardPanel(r);
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));
            card.setAlignmentX(LEFT_ALIGNMENT);
            card.setOnSeleccion(() -> control.seleccionarResidente(r));
            tarjetasResidentes.add(card);
            jPanelResidentes.add(card);
            jPanelResidentes.add(Box.createVerticalStrut(5));
        }
        jPanelResidentes.revalidate();
        jPanelResidentes.repaint();
    }

    public void marcarResidenteSeleccionado(ResidenteDTO residente) {
        tarjetasResidentes.forEach(c
                -> c.setSeleccionado(c.getResidente().getId().equals(residente.getId())));
    }

    public void mostrarHabitaciones(List<HabitacionDTO> habitaciones) {
        tarjetasHabitaciones.clear();
        jPanelHabitacion.removeAll();
        if (habitaciones == null || habitaciones.isEmpty()) {
            JLabel lbl = new JLabel("Sin habitaciones disponibles");
            lbl.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            lbl.setForeground(TEXTO_SEC);
            lbl.setAlignmentX(CENTER_ALIGNMENT);
            jPanelHabitacion.add(Box.createVerticalStrut(16));
            jPanelHabitacion.add(lbl);
        } else {
            for (HabitacionDTO h : habitaciones) {
                HabitacionCardPanel card = new HabitacionCardPanel(h);
                card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));
                card.setAlignmentX(LEFT_ALIGNMENT);
                card.setOnSeleccion(() -> control.seleccionarHabitacion(h));
                tarjetasHabitaciones.add(card);
                jPanelHabitacion.add(card);
                jPanelHabitacion.add(Box.createVerticalStrut(5));
            }
        }
        jPanelHabitacion.revalidate();
        jPanelHabitacion.repaint();
    }

    public void marcarHabitacionSeleccionada(HabitacionDTO habitacion) {
        tarjetasHabitaciones.forEach(c
                -> c.setSeleccionado(c.getHabitacion().getNumero_habitacion()
                        .equals(habitacion.getNumero_habitacion())));
    }

    public void mostrarConfirmacion(ResidenteDTO residente, HabitacionDTO habitacion) {
        confirmacionPanel.removeAll();

        JPanel banner = panelRedondeado(new Color(220, 248, 232), 10);
        banner.setLayout(new BorderLayout(6, 0));
        banner.setBorder(new EmptyBorder(8, 10, 8, 10));
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));
        banner.setAlignmentX(LEFT_ALIGNMENT);

        JLabel check = new JLabel(">");
        check.setFont(new Font("Segoe UI", Font.BOLD, 16));
        check.setForeground(EXITO);

        JPanel textoVal = new JPanel();
        textoVal.setLayout(new BoxLayout(textoVal, BoxLayout.Y_AXIS));
        textoVal.setOpaque(false);
        JLabel v1 = new JLabel("Validación exitosa");
        v1.setFont(new Font("Segoe UI", Font.BOLD, 11));
        v1.setForeground(EXITO);
        JLabel v2 = new JLabel("Habitación compatible con el residente");
        v2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        v2.setForeground(new Color(30, 100, 60));
        textoVal.add(v1);
        textoVal.add(v2);

        banner.add(check, BorderLayout.WEST);
        banner.add(textoVal, BorderLayout.CENTER);

        JPanel datos = panelRedondeado(new Color(248, 246, 255), 10);
        datos.setLayout(new GridLayout(2, 2, 8, 4));
        datos.setBorder(new EmptyBorder(10, 10, 10, 10));
        datos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));
        datos.setAlignmentX(LEFT_ALIGNMENT);
        datos.add(etiq("Residente"));
        datos.add(etiq("Habitación"));
        datos.add(val(residente.getNombre() + " " + residente.getApellido_paterno()));
        datos.add(val("Hab. " + habitacion.getNumero_habitacion()
                + (habitacion.getPiso() != null ? " · " + formatPiso(habitacion.getPiso()) : "")));

        JButton btnConfirmar = botonPrimario("Confirmar asignación");
        btnConfirmar.addActionListener(e -> control.confirmarAsignacion());

        confirmacionPanel.add(banner);
        confirmacionPanel.add(Box.createVerticalStrut(10));
        confirmacionPanel.add(datos);
        confirmacionPanel.add(Box.createVerticalStrut(12));
        confirmacionPanel.add(btnConfirmar);
        confirmacionPanel.add(Box.createVerticalGlue());
        confirmacionPanel.revalidate();
        confirmacionPanel.repaint();
    }

    public void mostrarAsignacionExitosa(ResidenteDTO residente, HabitacionDTO habitacion) {
        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(28, 28, 28, 28));

        JLabel icoCheck = new JLabel(">", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(EXITO);
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        icoCheck.setFont(new Font("Segoe UI", Font.BOLD, 28));
        icoCheck.setForeground(Color.WHITE);
        icoCheck.setOpaque(false);
        icoCheck.setPreferredSize(new Dimension(64, 64));
        icoCheck.setMaximumSize(new Dimension(64, 64));
        icoCheck.setAlignmentX(CENTER_ALIGNMENT);

        body.add(icoCheck);
        body.add(Box.createVerticalStrut(12));
        body.add(lblCentrado("¡Asignación Confirmada!", new Font("Segoe UI", Font.BOLD, 18), TEXTO_PRINCIPAL));
        body.add(Box.createVerticalStrut(4));
        body.add(lblCentrado("La habitación ha sido asignada exitosamente",
                new Font("Segoe UI", Font.PLAIN, 12), TEXTO_SEC));
        body.add(Box.createVerticalStrut(18));

        JPanel det = panelRedondeado(new Color(248, 246, 255), 10);
        det.setLayout(new GridLayout(3, 2, 8, 4));
        det.setBorder(new EmptyBorder(12, 14, 12, 14));
        det.setMaximumSize(new Dimension(340, 90));
        det.setAlignmentX(CENTER_ALIGNMENT);
        det.add(etiq("Residente"));
        det.add(etiq("Habitación"));
        det.add(val(residente.getNombre() + " " + residente.getApellido_paterno()));
        det.add(val("Hab. " + habitacion.getNumero_habitacion()));
        det.add(etiq("Fecha asignación"));
        det.add(new JLabel(""));
        det.add(val(fecha));
        det.add(new JLabel(""));
        body.add(det);
        body.add(Box.createVerticalStrut(16));

        JButton btnContrato = botonPrimario("Generar contrato de arrendamiento");
        btnContrato.setMaximumSize(new Dimension(340, 40));
        btnContrato.setAlignmentX(CENTER_ALIGNMENT);

        JButton btnCerrar = botonSecundario("Cerrar y continuar");
        btnCerrar.setMaximumSize(new Dimension(340, 40));
        btnCerrar.setAlignmentX(CENTER_ALIGNMENT);

        JDialog dlg = new JDialog(this, true);
        dlg.setUndecorated(true);
        dlg.getRootPane().setBorder(BorderFactory.createLineBorder(new Color(220, 215, 240)));
        dlg.setContentPane(body);

        btnContrato.addActionListener(e -> JOptionPane.showMessageDialog(dlg, "Generando contrato..."));
        btnCerrar.addActionListener(e -> {
            dlg.dispose();
            control.reiniciarFlujo();
        });

        body.add(btnContrato);
        body.add(Box.createVerticalStrut(8));
        body.add(btnCerrar);

        dlg.pack();
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    public void mostrarErrorAsignacion(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void limpiarConfirmacion() {
        mostrarPlaceholderConfirmacion();
    }

    public void limpiarTodo() {
        tarjetasResidentes.forEach(c -> c.setSeleccionado(false));
        jPanelHabitacion.removeAll();
        jPanelHabitacion.revalidate();
        jPanelHabitacion.repaint();
        mostrarPlaceholderConfirmacion();
    }

    private String formatPiso(Integer p) {
        if (p == null) {
            return "";
        }
        return switch (p) {
            case 1 ->
                "1er piso";
            case 2 ->
                "2do piso";
            case 3 ->
                "3er piso";
            default ->
                p + "° piso";
        };
    }

    private JLabel etiq(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        l.setForeground(TEXTO_SEC);
        return l;
    }

    private JLabel val(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(TEXTO_PRINCIPAL);
        return l;
    }

    private JLabel lblCentrado(String t, Font f, Color c) {
        JLabel l = new JLabel(t, SwingConstants.CENTER);
        l.setFont(f);
        l.setForeground(c);
        l.setAlignmentX(CENTER_ALIGNMENT);
        return l;
    }

    private JPanel panelRedondeado(Color fondo, int radio) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(fondo);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radio, radio);
                g2.dispose();
            }

            {
                setOpaque(false);
            }
        };
    }

    private JButton botonPrimario(String texto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? MORADO_HOVER : MORADO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        return btn;
    }

    private JButton botonSecundario(String texto) {
        JButton btn = botonPrimario(texto);
        btn.putClientProperty("fondo", new Color(245, 242, 255));
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
                setFont(new Font("Segoe UI", Font.PLAIN, 12));
                setForeground(MORADO);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setFocusPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setMaximumSize(new Dimension(340, 40));
                setAlignmentX(CENTER_ALIGNMENT);
            }
        };
    }

    private static class ScrollBarDelgada extends BasicScrollBarUI {

        @Override
        protected void configureScrollBarColors() {
            thumbColor = new Color(180, 160, 220, 160);
            trackColor = new Color(0, 0, 0, 0);
        }

        @Override
        protected JButton createDecreaseButton(int o) {
            return vacio();
        }

        @Override
        protected JButton createIncreaseButton(int o) {
            return vacio();
        }

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

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
        }
    }

    private static class BordereRedondeado extends AbstractBorder {

        private final Color color;
        private final int radio;

        BordereRedondeado(Color c, int r) {
            color = c;
            radio = r;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, w - 1, h - 1, radio, radio);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(4, 8, 4, 8);
        }
    }

    private static class ComboUI extends BasicComboBoxUI {

        @Override
        protected JButton createArrowButton() {
            JButton b = new JButton("▾");
            b.setBackground(FONDO_INPUT);
            b.setForeground(new Color(120, 80, 200));
            b.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            b.setBorderPainted(false);
            b.setFocusPainted(false);
            b.setContentAreaFilled(false);
            return b;
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(FONDO_INPUT);
            g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 10, 10);
            g2.dispose();
            super.paint(g, c);
        }
        private static final Color FONDO_INPUT = new Color(250, 249, 255);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelSideBar = new javax.swing.JPanel();
        jPanelSeleccion = new javax.swing.JPanel();
        jComboBoxFiltroHabitacion = new javax.swing.JComboBox<>();
        jComboBoxFiltroResidentes = new javax.swing.JComboBox<>();
        buscadorResidentes = new javax.swing.JTextField();
        jScrollPaneHabitacion = new javax.swing.JScrollPane();
        jPanelHabitacion = new javax.swing.JPanel();
        jScrollPaneResidentes = new javax.swing.JScrollPane();
        jPanelResidentes = new javax.swing.JPanel();
        jLabelLogo = new javax.swing.JLabel();
        jLabelFondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1280, 832));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout jPanelSideBarLayout = new javax.swing.GroupLayout(jPanelSideBar);
        jPanelSideBar.setLayout(jPanelSideBarLayout);
        jPanelSideBarLayout.setHorizontalGroup(
            jPanelSideBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 250, Short.MAX_VALUE)
        );
        jPanelSideBarLayout.setVerticalGroup(
            jPanelSideBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 610, Short.MAX_VALUE)
        );

        getContentPane().add(jPanelSideBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 220, 250, 610));

        javax.swing.GroupLayout jPanelSeleccionLayout = new javax.swing.GroupLayout(jPanelSeleccion);
        jPanelSeleccion.setLayout(jPanelSeleccionLayout);
        jPanelSeleccionLayout.setHorizontalGroup(
            jPanelSeleccionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 280, Short.MAX_VALUE)
        );
        jPanelSeleccionLayout.setVerticalGroup(
            jPanelSeleccionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 560, Short.MAX_VALUE)
        );

        getContentPane().add(jPanelSeleccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 230, 280, 560));

        jComboBoxFiltroHabitacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(jComboBoxFiltroHabitacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 260, 270, 40));

        jComboBoxFiltroResidentes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxFiltroResidentes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxFiltroResidentesActionPerformed(evt);
            }
        });
        getContentPane().add(jComboBoxFiltroResidentes, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 260, 280, 40));

        buscadorResidentes.setText("jTextField1");
        getContentPane().add(buscadorResidentes, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 310, 280, 40));

        jScrollPaneHabitacion.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        javax.swing.GroupLayout jPanelHabitacionLayout = new javax.swing.GroupLayout(jPanelHabitacion);
        jPanelHabitacion.setLayout(jPanelHabitacionLayout);
        jPanelHabitacionLayout.setHorizontalGroup(
            jPanelHabitacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 284, Short.MAX_VALUE)
        );
        jPanelHabitacionLayout.setVerticalGroup(
            jPanelHabitacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 484, Short.MAX_VALUE)
        );

        jScrollPaneHabitacion.setViewportView(jPanelHabitacion);

        getContentPane().add(jScrollPaneHabitacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 310, 290, 480));

        jScrollPaneResidentes.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        javax.swing.GroupLayout jPanelResidentesLayout = new javax.swing.GroupLayout(jPanelResidentes);
        jPanelResidentes.setLayout(jPanelResidentesLayout);
        jPanelResidentesLayout.setHorizontalGroup(
            jPanelResidentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 274, Short.MAX_VALUE)
        );
        jPanelResidentesLayout.setVerticalGroup(
            jPanelResidentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 464, Short.MAX_VALUE)
        );

        jScrollPaneResidentes.setViewportView(jPanelResidentes);

        getContentPane().add(jScrollPaneResidentes, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 360, 280, 430));

        jLabelLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/LogoResidencias.png"))); // NOI18N
        getContentPane().add(jLabelLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, -1, -1));

        jLabelFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AsignarHabitacionFondo.png"))); // NOI18N
        getContentPane().add(jLabelFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxFiltroResidentesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxFiltroResidentesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxFiltroResidentesActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField buscadorResidentes;
    private javax.swing.JComboBox<String> jComboBoxFiltroHabitacion;
    private javax.swing.JComboBox<String> jComboBoxFiltroResidentes;
    private javax.swing.JLabel jLabelFondo;
    private javax.swing.JLabel jLabelLogo;
    private javax.swing.JPanel jPanelHabitacion;
    private javax.swing.JPanel jPanelResidentes;
    private javax.swing.JPanel jPanelSeleccion;
    private javax.swing.JPanel jPanelSideBar;
    private javax.swing.JScrollPane jScrollPaneHabitacion;
    private javax.swing.JScrollPane jScrollPaneResidentes;
    // End of variables declaration//GEN-END:variables
}
