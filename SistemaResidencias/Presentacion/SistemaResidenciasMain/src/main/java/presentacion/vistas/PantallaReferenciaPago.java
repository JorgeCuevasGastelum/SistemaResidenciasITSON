package presentacion.vistas;

import dtos.ResidenteDTO;
import dtos.ReferenciasPagoDTO;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import presentacion.control.ReferenciasPagoControl;

public class PantallaReferenciaPago extends JPanel {

    private static final Color AZUL_PRIMARIO  = new Color(55, 75, 190);
    private static final Color AZUL_HOVER     = new Color(45, 60, 160);
    private static final Color AZUL_DISABLED  = new Color(155, 165, 220);
    private static final Color TEXTO_PRINCIPAL = new Color(30, 30, 40);
    private static final Color TEXTO_SEC       = new Color(110, 110, 125);
    private static final Color BORDE_INPUT     = new Color(200, 195, 225);
    private static final Color FONDO_INPUT     = new Color(250, 249, 255);
    private static final Color FONDO_PANEL     = new Color(243, 243, 247);
    private static final Color FONDO_CARD      = Color.WHITE;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private ReferenciasPagoControl control;

    // Búsqueda
    private JTextField campoBusqueda;
    private JPopupMenu popupResultados;
    private JList<ResidenteDTO> listaResultados;
    private DefaultListModel<ResidenteDTO> modeloLista;
    private Timer timerBusqueda;
    private JPanel panelResidenteSeleccionado;

    // Datos referencia
    private JComboBox<String> comboPlan;
    private JTextField campoCiclo;
    private JTextField campoMonto;
    private JTextField campoFechaLimite;

    // Vista previa
    private JPanel panelPreviewContent;
    private JLabel lblPlaceholder;
    private JButton btnGenerar;
    private JButton btnImprimir;

    private ReferenciasPagoDTO referenciaActual;
    private File pdfActual;

    public PantallaReferenciaPago() {
        setOpaque(true);
        setBackground(FONDO_PANEL);
        setLayout(new BorderLayout());
        construirUI();
    }

    public void setControl(ReferenciasPagoControl control) {
        this.control = control;
        control.setVista(this);
        inicializarDatosAuto();
    }

    // ─── CONSTRUCCIÓN DE UI ───────────────────────────────────────────────────

    private void construirUI() {
        add(construirCabecera(), BorderLayout.NORTH);
        add(construirContenido(), BorderLayout.CENTER);
    }

    private JPanel construirCabecera() {
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(28, 36, 16, 36));

        JLabel titulo = new JLabel("Generar Referencia de Pago");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(AZUL_PRIMARIO);
        titulo.setAlignmentX(LEFT_ALIGNMENT);

        JLabel subtitulo = new JLabel("Crea referencias de pago para residentes");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitulo.setForeground(TEXTO_SEC);
        subtitulo.setAlignmentX(LEFT_ALIGNMENT);
        subtitulo.setBorder(new EmptyBorder(3, 0, 0, 0));

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(215, 212, 235));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        header.add(titulo);
        header.add(subtitulo);
        header.add(Box.createVerticalStrut(14));
        header.add(sep);
        return header;
    }

    private JPanel construirContenido() {
        JPanel contenido = new JPanel(new GridLayout(1, 2, 20, 0));
        contenido.setOpaque(false);
        contenido.setBorder(new EmptyBorder(18, 28, 28, 28));

        contenido.add(construirColumnaIzquierda());
        contenido.add(construirColumnaDerecha());
        return contenido;
    }

    // ─── COLUMNA IZQUIERDA ────────────────────────────────────────────────────

    private JPanel construirColumnaIzquierda() {
        JPanel col = new JPanel(new GridBagLayout());
        col.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 16, 0);

        gbc.gridy = 0;
        gbc.weighty = 0.0;
        col.add(construirCardSeleccion(), gbc);

        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        col.add(construirCardDatosReferencia(), gbc);

        return col;
    }

    private JPanel construirCardSeleccion() {
        JPanel card = cardBlanco(14);
        card.setLayout(new GridBagLayout());
        card.setBorder(new EmptyBorder(22, 24, 22, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;

        gbc.gridy = 0; gbc.insets = new Insets(0, 0, 16, 0);
        card.add(tituloCard("Selección del Residente"), gbc);

        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 0, 0);
        card.add(construirCampoBusqueda(), gbc);

        gbc.gridy = 2; gbc.insets = new Insets(10, 0, 0, 0);
        panelResidenteSeleccionado = construirPanelResidenteVacio();
        card.add(panelResidenteSeleccionado, gbc);

        return card;
    }

    private JPanel construirCampoBusqueda() {
        JPanel wrap = new JPanel(new BorderLayout(8, 0));
        wrap.setOpaque(true);
        wrap.setBackground(FONDO_INPUT);
        wrap.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(BORDE_INPUT, 10),
                new EmptyBorder(0, 12, 0, 12)));
        wrap.setPreferredSize(new Dimension(0, 46));
        wrap.setMinimumSize(new Dimension(0, 46));

        campoBusqueda = new JTextField("Buscar por nombre o ID...");
        campoBusqueda.setForeground(new Color(175, 165, 210));
        campoBusqueda.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoBusqueda.setBackground(FONDO_INPUT);
        campoBusqueda.setBorder(BorderFactory.createEmptyBorder());
        campoBusqueda.setOpaque(false);

        campoBusqueda.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (campoBusqueda.getText().equals("Buscar por nombre o ID...")) {
                    campoBusqueda.setText("");
                    campoBusqueda.setForeground(TEXTO_PRINCIPAL);
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (campoBusqueda.getText().isBlank()) {
                    campoBusqueda.setText("Buscar por nombre o ID...");
                    campoBusqueda.setForeground(new Color(175, 165, 210));
                }
            }
        });

        configurarBusqueda();

        wrap.add(campoBusqueda, BorderLayout.CENTER);
        return wrap;
    }

    private JPanel construirPanelResidenteVacio() {
        JPanel p = new JPanel();
        p.setOpaque(true);
        p.setBackground(new Color(240, 244, 255));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(AZUL_DISABLED, 8),
                new EmptyBorder(12, 16, 12, 16)));
        p.setVisible(false);
        return p;
    }

    private JPanel construirCardDatosReferencia() {
        JPanel card = cardBlanco(14);
        card.setLayout(new GridBagLayout());
        card.setBorder(new EmptyBorder(22, 24, 22, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;

        gbc.gridy = 0; gbc.insets = new Insets(0, 0, 18, 0);
        card.add(tituloCard("Datos de Referencia"), gbc);

        // Plan de pago
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 4, 0);
        card.add(etiquetaCampo("Plan de pago"), gbc);

        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 16, 0);
        comboPlan = new JComboBox<>();
        comboPlan.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboPlan.setBackground(FONDO_INPUT);
        comboPlan.setForeground(TEXTO_PRINCIPAL);
        comboPlan.setPreferredSize(new Dimension(0, 46));
        comboPlan.setMinimumSize(new Dimension(0, 46));
        estilizarCombo(comboPlan);
        card.add(comboPlan, gbc);

        // Ciclo lectivo
        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 4, 0);
        card.add(etiquetaCampo("Ciclo lectivo"), gbc);

        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 16, 0);
        campoCiclo = campoTextoReadonly();
        card.add(campoCiclo, gbc);

        // Monto
        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 4, 0);
        card.add(etiquetaCampo("Monto"), gbc);

        gbc.gridy = 6; gbc.insets = new Insets(0, 0, 16, 0);
        card.add(construirCampoMonto(), gbc);

        // Fecha límite
        gbc.gridy = 7; gbc.insets = new Insets(0, 0, 4, 0);
        card.add(etiquetaCampo("Fecha Límite"), gbc);

        gbc.gridy = 8; gbc.weighty = 0.0; gbc.insets = new Insets(0, 0, 0, 0);
        campoFechaLimite = campoTextoReadonly();
        card.add(campoFechaLimite, gbc);

        // Filler para empujar todo hacia arriba
        gbc.gridy = 9; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(0, 0, 0, 0);
        card.add(Box.createVerticalGlue(), gbc);

        // Listeners
        comboPlan.addActionListener(e -> {
            int idx = comboPlan.getSelectedIndex();
            if (idx >= 0 && control != null) {
                double monto = control.getMontoParaPlan(idx);
                campoMonto.setText(String.format("%.2f", monto));
            }
            actualizarVistaPrevia();
        });

        campoMonto.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { actualizarVistaPrevia(); }
            public void removeUpdate(DocumentEvent e) { actualizarVistaPrevia(); }
            public void changedUpdate(DocumentEvent e) { actualizarVistaPrevia(); }
        });

        return card;
    }

    private JPanel construirCampoMonto() {
        JPanel wrap = new JPanel(new BorderLayout(6, 0));
        wrap.setOpaque(true);
        wrap.setBackground(FONDO_INPUT);
        wrap.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(BORDE_INPUT, 10),
                new EmptyBorder(0, 12, 0, 12)));
        wrap.setPreferredSize(new Dimension(0, 46));
        wrap.setMinimumSize(new Dimension(0, 46));

        JLabel lblDolar = new JLabel("$");
        lblDolar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDolar.setForeground(TEXTO_SEC);

        campoMonto = new JTextField();
        campoMonto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoMonto.setForeground(TEXTO_PRINCIPAL);
        campoMonto.setBackground(FONDO_INPUT);
        campoMonto.setBorder(BorderFactory.createEmptyBorder());
        campoMonto.setOpaque(false);

        wrap.add(lblDolar, BorderLayout.WEST);
        wrap.add(campoMonto, BorderLayout.CENTER);
        return wrap;
    }

    // ─── COLUMNA DERECHA ──────────────────────────────────────────────────────

    private JPanel construirColumnaDerecha() {
        JPanel card = cardBlanco(14);
        card.setLayout(new GridBagLayout());
        card.setBorder(new EmptyBorder(22, 24, 22, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;

        gbc.gridy = 0; gbc.insets = new Insets(0, 0, 18, 0);
        card.add(tituloCard("Vista previa"), gbc);

        // Área de preview
        gbc.gridy = 1; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 18, 0);
        panelPreviewContent = new JPanel();
        panelPreviewContent.setOpaque(true);
        panelPreviewContent.setBackground(new Color(248, 248, 252));
        panelPreviewContent.setLayout(new BoxLayout(panelPreviewContent, BoxLayout.Y_AXIS));
        panelPreviewContent.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(new Color(210, 205, 230), 10),
                new EmptyBorder(18, 18, 18, 18)));

        lblPlaceholder = new JLabel(
                "<html><center>Selecciona un residente<br>para ver la vista previa</center></html>",
                SwingConstants.CENTER);
        lblPlaceholder.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblPlaceholder.setForeground(TEXTO_SEC);
        lblPlaceholder.setAlignmentX(CENTER_ALIGNMENT);

        panelPreviewContent.add(Box.createVerticalGlue());
        panelPreviewContent.add(lblPlaceholder);
        panelPreviewContent.add(Box.createVerticalGlue());
        card.add(panelPreviewContent, gbc);

        // Botones
        gbc.gridy = 2; gbc.weighty = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 10, 0);
        btnGenerar = botonPrimario("Generar referencia");
        btnGenerar.setEnabled(false);
        btnGenerar.addActionListener(e -> accionGenerarReferencia());
        card.add(btnGenerar, gbc);

        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 0, 0);
        btnImprimir = botonSecundario("Imprimir");
        btnImprimir.setEnabled(false);
        btnImprimir.addActionListener(e -> accionImprimir());
        card.add(btnImprimir, gbc);

        return card;
    }

    // ─── BÚSQUEDA ─────────────────────────────────────────────────────────────

    private void configurarBusqueda() {
        modeloLista = new DefaultListModel<>();
        listaResultados = new JList<>(modeloLista);
        listaResultados.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        listaResultados.setFixedCellHeight(38);
        listaResultados.setBackground(Color.WHITE);
        listaResultados.setCellRenderer(new ResidenteListRenderer());
        listaResultados.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int idx = listaResultados.locationToIndex(e.getPoint());
                if (idx >= 0) {
                    ResidenteDTO sel = modeloLista.get(idx);
                    popupResultados.setVisible(false);
                    if (control != null) control.seleccionarResidente(sel);
                }
            }
        });

        JScrollPane scroll = new JScrollPane(listaResultados);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUI(new ScrollBarDelgada());
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(5, 0));

        popupResultados = new JPopupMenu();
        popupResultados.setFocusable(false);
        popupResultados.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 205, 230), 1, true),
                BorderFactory.createEmptyBorder(4, 0, 4, 0)));
        popupResultados.add(scroll);

        timerBusqueda = new Timer(200, e -> ejecutarBusqueda());
        timerBusqueda.setRepeats(false);

        campoBusqueda.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { timerBusqueda.restart(); }
            public void removeUpdate(DocumentEvent e) { timerBusqueda.restart(); }
            public void changedUpdate(DocumentEvent e) { timerBusqueda.restart(); }
        });
    }

    private void ejecutarBusqueda() {
        String texto = campoBusqueda.getText().trim();
        if (texto.equals("Buscar por nombre o ID...") || texto.length() < 2) {
            popupResultados.setVisible(false);
            return;
        }
        if (control == null) return;

        SwingWorker<List<ResidenteDTO>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<ResidenteDTO> doInBackground() {
                return control.buscarResidentes(texto);
            }
            @Override
            protected void done() {
                try {
                    List<ResidenteDTO> lista = get();
                    modeloLista.clear();
                    if (lista == null || lista.isEmpty()) {
                        popupResultados.setVisible(false);
                        return;
                    }
                    for (ResidenteDTO r : lista) modeloLista.addElement(r);
                    int alto = Math.min(lista.size() * 38 + 8, 220);
                    Component anchor = campoBusqueda.getParent();
                    popupResultados.setPreferredSize(new Dimension(anchor.getWidth(), alto));
                    popupResultados.show(anchor, 0, anchor.getHeight());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    // ─── MOSTRAR RESIDENTE ────────────────────────────────────────────────────

    public void mostrarResidenteSeleccionado(ResidenteDTO residente, Integer numeroHabitacion) {
        panelResidenteSeleccionado.removeAll();

        String hab = numeroHabitacion != null ? String.valueOf(numeroHabitacion) : "—";
        String nombre = residente.getNombre() + " " + residente.getApellido_paterno()
                + (residente.getApellido_materno() != null ? " " + residente.getApellido_materno() : "");

        JLabel lblTag = new JLabel("Residente seleccionado:");
        lblTag.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTag.setForeground(AZUL_PRIMARIO);
        lblTag.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblNombre = new JLabel(nombre.trim());
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNombre.setForeground(TEXTO_PRINCIPAL);
        lblNombre.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblInfo = new JLabel("ID: " + residente.getId() + "  ·  Hab. " + hab);
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfo.setForeground(TEXTO_SEC);
        lblInfo.setAlignmentX(LEFT_ALIGNMENT);

        panelResidenteSeleccionado.add(lblTag);
        panelResidenteSeleccionado.add(Box.createVerticalStrut(3));
        panelResidenteSeleccionado.add(lblNombre);
        panelResidenteSeleccionado.add(Box.createVerticalStrut(3));
        panelResidenteSeleccionado.add(lblInfo);
        panelResidenteSeleccionado.setVisible(true);

        campoBusqueda.setText(nombre.trim());
        campoBusqueda.setForeground(TEXTO_PRINCIPAL);

        btnGenerar.setEnabled(true);
        actualizarVistaPrevia();

        panelResidenteSeleccionado.revalidate();
        panelResidenteSeleccionado.repaint();
    }

    public void mostrarErrorSinHabitacion() {
        JOptionPane.showMessageDialog(this,
                "El residente no tiene una habitación asignada.\nAsigna una habitación primero.",
                "Sin habitación", JOptionPane.WARNING_MESSAGE);
    }

    // ─── VISTA PREVIA ─────────────────────────────────────────────────────────

    private void actualizarVistaPrevia() {
        if (control == null || !btnGenerar.isEnabled()) return;

        int planIdx = comboPlan.getSelectedIndex();
        String plan = planIdx >= 0 ? (String) comboPlan.getSelectedItem() : "";
        String ciclo = campoCiclo.getText();
        double monto = 0;
        try { monto = Double.parseDouble(campoMonto.getText().replace(",", "")); } catch (NumberFormatException ignored) {}
        LocalDate fechaLimite = control.calcularFechaLimite();

        referenciaActual = control.construirReferencia(plan, ciclo, monto, fechaLimite);
        if (referenciaActual == null) return;

        mostrarPreviewConDatos(referenciaActual);
    }

    private void mostrarPreviewConDatos(ReferenciasPagoDTO dto) {
        panelPreviewContent.removeAll();
        panelPreviewContent.setLayout(new GridBagLayout());

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1.0;
        int r = 0;

        // Título e institución
        g.gridy = r++; g.insets = new Insets(0, 0, 2, 0);
        JLabel lTitulo = new JLabel("Referencia de Pago", SwingConstants.CENTER);
        lTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lTitulo.setForeground(TEXTO_PRINCIPAL);
        panelPreviewContent.add(lTitulo, g);

        g.gridy = r++; g.insets = new Insets(0, 0, 12, 0);
        JLabel lInst = new JLabel("Sistema de Residencias Estudiantiles", SwingConstants.CENTER);
        lInst.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lInst.setForeground(TEXTO_SEC);
        panelPreviewContent.add(lInst, g);

        // Separador
        g.gridy = r++; g.insets = new Insets(0, 0, 12, 0);
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(210, 205, 230));
        panelPreviewContent.add(sep, g);

        // Filas de datos
        g.insets = new Insets(0, 0, 6, 0);
        agregarFilaPreview(r++, "Residente:", dto.getNombreResidente(), g);
        agregarFilaPreview(r++, "Habitacion:", String.valueOf(dto.getNumeroHabitacion()), g);
        agregarFilaPreview(r++, "Concepto:", dto.getConcepto(), g);
        agregarFilaPreview(r++, "Ciclo:", dto.getCicloLectivo(), g);

        // Total
        g.gridy = r++; g.insets = new Insets(12, 0, 2, 0);
        JLabel lTotalEtiq = new JLabel("Total a pagar:");
        lTotalEtiq.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lTotalEtiq.setForeground(TEXTO_SEC);
        panelPreviewContent.add(lTotalEtiq, g);

        g.gridy = r++; g.insets = new Insets(0, 0, 12, 0);
        JLabel lTotal = new JLabel(String.format("$%,.2f", dto.getMonto()));
        lTotal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lTotal.setForeground(AZUL_PRIMARIO);
        panelPreviewContent.add(lTotal, g);

        // Caja referencia bancaria
        g.gridy = r++; g.insets = new Insets(0, 0, 0, 0);
        panelPreviewContent.add(construirCajaBancaria(dto.getReferenciaBancaria()), g);

        // Filler
        g.gridy = r; g.weighty = 1.0; g.fill = GridBagConstraints.VERTICAL;
        panelPreviewContent.add(Box.createVerticalGlue(), g);

        panelPreviewContent.revalidate();
        panelPreviewContent.repaint();
    }

    private void agregarFilaPreview(int row, String etiq, String valor, GridBagConstraints g) {
        g.gridy = row;
        String v = valor != null ? valor : "—";
        JLabel lbl = new JLabel(
                "<html><font color='#6e6e7b'>" + etiq + "</font>&nbsp;&nbsp;<b>" + v + "</b></html>");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelPreviewContent.add(lbl, g);
    }

    private JPanel construirCajaBancaria(String referencia) {
        JPanel caja = new JPanel() {
            @Override
            protected void paintComponent(Graphics g2d) {
                Graphics2D g2 = (Graphics2D) g2d.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(238, 238, 244));
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g2d);
            }
        };
        caja.setOpaque(false);
        caja.setLayout(new GridBagLayout());
        caja.setBorder(new EmptyBorder(12, 14, 12, 14));

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1.0;

        gc.gridy = 0; gc.insets = new Insets(0, 0, 4, 0);
        JLabel lEtiq = new JLabel("Referencia bancaria", SwingConstants.CENTER);
        lEtiq.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lEtiq.setForeground(TEXTO_SEC);
        caja.add(lEtiq, gc);

        gc.gridy = 1; gc.insets = new Insets(0, 0, 0, 0);
        JLabel lRef = new JLabel(referencia, SwingConstants.CENTER);
        lRef.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lRef.setForeground(TEXTO_PRINCIPAL);
        caja.add(lRef, gc);

        return caja;
    }

    // ─── ACCIONES DE BOTONES ──────────────────────────────────────────────────

    private void accionGenerarReferencia() {
        if (control == null || referenciaActual == null) return;

        double monto;
        try {
            monto = Double.parseDouble(campoMonto.getText().replace(",", ""));
            if (monto <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingresa un monto válido.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        referenciaActual.setMonto(monto);

        control.guardarReferencia(referenciaActual);

        pdfActual = control.generarPdf(referenciaActual);
        if (pdfActual == null) {
            JOptionPane.showMessageDialog(this, "Error al generar el PDF.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean enviado = control.enviarPorCorreo(referenciaActual, pdfActual);
        if (enviado) {
            JOptionPane.showMessageDialog(this,
                    "Referencia generada y enviada a: " + referenciaActual.getCorreoResidente(),
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Referencia guardada correctamente.\n(No se pudo enviar el correo al residente)",
                    "Listo", JOptionPane.INFORMATION_MESSAGE);
        }

        btnImprimir.setEnabled(true);
    }

    private void accionImprimir() {
        if (pdfActual == null && referenciaActual != null) {
            pdfActual = control.generarPdf(referenciaActual);
        }
        if (pdfActual == null) return;
        control.imprimirPdf(pdfActual);
    }

    // ─── INICIALIZACIÓN ───────────────────────────────────────────────────────

    private void inicializarDatosAuto() {
        if (control == null) return;

        for (String p : control.getPlanes()) comboPlan.addItem(p);
        comboPlan.setSelectedIndex(2);

        campoCiclo.setText(control.calcularCicloLectivo());

        double montoInicial = control.getMontoParaPlan(2);
        campoMonto.setText(String.format("%.2f", montoInicial));

        LocalDate fl = control.calcularFechaLimite();
        campoFechaLimite.setText(fl.format(FMT));
    }

    public void reiniciar() {
        campoBusqueda.setText("Buscar por nombre o ID...");
        campoBusqueda.setForeground(new Color(175, 165, 210));
        panelResidenteSeleccionado.setVisible(false);
        panelPreviewContent.removeAll();
        panelPreviewContent.setLayout(new BoxLayout(panelPreviewContent, BoxLayout.Y_AXIS));
        panelPreviewContent.add(Box.createVerticalGlue());
        lblPlaceholder.setAlignmentX(CENTER_ALIGNMENT);
        panelPreviewContent.add(lblPlaceholder);
        panelPreviewContent.add(Box.createVerticalGlue());
        panelPreviewContent.revalidate();
        panelPreviewContent.repaint();
        btnGenerar.setEnabled(false);
        btnImprimir.setEnabled(false);
        referenciaActual = null;
        pdfActual = null;
        if (control != null) control.limpiarSeleccion();
    }

    // ─── HELPERS DE UI ────────────────────────────────────────────────────────

    private JPanel cardBlanco(int radio) {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FONDO_CARD);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radio, radio);
                g2.setColor(new Color(220, 217, 238));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radio, radio);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        return p;
    }

    private JLabel tituloCard(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.BOLD, 16));
        l.setForeground(TEXTO_PRINCIPAL);
        return l;
    }

    private JLabel etiquetaCampo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(TEXTO_PRINCIPAL);
        return l;
    }

    private JTextField campoTextoReadonly() {
        JTextField f = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FONDO_INPUT);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        f.setEditable(false);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setForeground(TEXTO_PRINCIPAL);
        f.setBackground(FONDO_INPUT);
        f.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(BORDE_INPUT, 10),
                new EmptyBorder(0, 12, 0, 12)));
        f.setPreferredSize(new Dimension(0, 46));
        f.setMinimumSize(new Dimension(0, 46));
        f.setOpaque(false);
        return f;
    }

    private JLabel centrado(String texto, Font fuente, Color color) {
        JLabel l = new JLabel(texto, SwingConstants.CENTER);
        l.setFont(fuente);
        l.setForeground(color);
        l.setAlignmentX(CENTER_ALIGNMENT);
        return l;
    }

    private JButton botonPrimario(String texto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color fondo = isEnabled()
                        ? (getModel().isRollover() ? AZUL_HOVER : AZUL_PRIMARIO)
                        : AZUL_DISABLED;
                g2.setColor(fondo);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 46));
        btn.setMinimumSize(new Dimension(0, 46));
        return btn;
    }

    private JButton botonSecundario(String texto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color fondo = isEnabled() ? new Color(238, 236, 255) : new Color(230, 230, 235);
                g2.setColor(fondo);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(AZUL_PRIMARIO);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 46));
        btn.setMinimumSize(new Dimension(0, 46));
        return btn;
    }

    private void estilizarCombo(JComboBox<String> cb) {
        cb.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton b = new JButton("▾");
                b.setBackground(FONDO_INPUT);
                b.setForeground(new Color(120, 80, 200));
                b.setFont(new Font("Segoe UI", Font.PLAIN, 11));
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
                g2.fillRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 10, 10);
                g2.setColor(BORDE_INPUT);
                g2.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 10, 10);
                g2.dispose();
                super.paint(g, c);
            }
        });
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean hasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, hasFocus);
                lbl.setBorder(new EmptyBorder(8, 12, 8, 12));
                lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                lbl.setBackground(isSelected ? new Color(240, 245, 255) : Color.WHITE);
                lbl.setForeground(isSelected ? AZUL_PRIMARIO : TEXTO_PRINCIPAL);
                return lbl;
            }
        });
    }

    // ─── CLASES INTERNAS ──────────────────────────────────────────────────────

    private static class RoundBorder extends AbstractBorder {
        private final Color color;
        private final int radio;
        RoundBorder(Color c, int r) { color = c; radio = r; }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, w - 1, h - 1, radio, radio);
            g2.dispose();
        }
        @Override
        public Insets getBorderInsets(Component c) { return new Insets(6, 12, 6, 12); }
    }

    private static class ScrollBarDelgada extends BasicScrollBarUI {
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
        protected void paintThumb(Graphics g, JComponent c, java.awt.Rectangle r) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);
            g2.fillRoundRect(r.x + 1, r.y, r.width - 2, r.height, 5, 5);
            g2.dispose();
        }
        @Override protected void paintTrack(Graphics g, JComponent c, java.awt.Rectangle r) {}
    }

    private static class ResidenteListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean hasFocus) {
            JLabel lbl = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, hasFocus);
            if (value instanceof ResidenteDTO r) {
                String nombre = r.getNombre() + " " + r.getApellido_paterno()
                        + (r.getApellido_materno() != null ? " " + r.getApellido_materno() : "");
                lbl.setText("<html><b>" + nombre.trim() + "</b>&nbsp;&nbsp;"
                        + "<font color='#6e6e78'>" + r.getId() + "</font></html>");
            }
            lbl.setBorder(new EmptyBorder(5, 14, 5, 14));
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lbl.setBackground(isSelected ? new Color(240, 245, 255) : Color.WHITE);
            return lbl;
        }
    }
}
