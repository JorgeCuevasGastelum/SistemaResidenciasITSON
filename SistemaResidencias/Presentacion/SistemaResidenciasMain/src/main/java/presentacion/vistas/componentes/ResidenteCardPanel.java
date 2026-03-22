package presentacion.vistas.componentes;

import dtos.ResidenteDTO;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * JPanel que funciona como botón seleccionable para un Residente.
 */
public class ResidenteCardPanel extends JPanel {

    private static final Color COLOR_FONDO_NORMAL = Color.WHITE;
    private static final Color COLOR_FONDO_HOVER = new Color(243, 240, 255);
    private static final Color COLOR_FONDO_SELECCIONADO = new Color(230, 220, 255);
    private static final Color COLOR_BORDE_NORMAL = new Color(220, 220, 225);
    private static final Color COLOR_BORDE_SELECCIONADO = new Color(120, 80, 200);
    private static final Color COLOR_FEMENINO = new Color(255, 100, 150);
    private static final Color COLOR_MASCULINO = new Color(80, 140, 220);
    private static final Color COLOR_NOMBRE = new Color(30, 30, 40);
    private static final Color COLOR_CARRERA = new Color(100, 100, 115);
    private static final Color COLOR_LINK = new Color(120, 80, 200);

    private final ResidenteDTO residente;
    private boolean seleccionado = false;
    private boolean hover = false;

    private Runnable onSeleccion;

    public ResidenteCardPanel(ResidenteDTO residente) {
        this.residente = residente;
        construirUI();
        configurarInteracciones();
    }

    private void construirUI() {
        setLayout(new BorderLayout(8, 0));
        setOpaque(false);
        setBorder(new EmptyBorder(8, 10, 8, 10));
        setPreferredSize(new Dimension(0, 62));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel lblNombre = new JLabel(nombreCompleto());
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNombre.setForeground(COLOR_NOMBRE);

        JLabel lblCarrera = new JLabel(residente.getCarrera() != null ? residente.getCarrera() : "");
        lblCarrera.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCarrera.setForeground(COLOR_CARRERA);

        infoPanel.add(lblNombre);
        infoPanel.add(Box.createVerticalStrut(2));
        infoPanel.add(lblCarrera);

        add(infoPanel, BorderLayout.CENTER);

        JPanel derechaPanel = new JPanel();
        derechaPanel.setLayout(new BoxLayout(derechaPanel, BoxLayout.Y_AXIS));
        derechaPanel.setOpaque(false);

        JLabel chipGenero = crearChipGenero();
        chipGenero.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel lblVista = new JLabel("Vista rapida");
        lblVista.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblVista.setForeground(COLOR_LINK);
        lblVista.setAlignmentX(Component.RIGHT_ALIGNMENT);

        derechaPanel.add(chipGenero);
        derechaPanel.add(Box.createVerticalStrut(4));
        derechaPanel.add(lblVista);

        add(derechaPanel, BorderLayout.EAST);
    }

    private String nombreCompleto() {
        StringBuilder sb = new StringBuilder();
        if (residente.getNombre() != null) {
            sb.append(residente.getNombre()).append(" ");
        }
        if (residente.getApellido_paterno() != null) {
            sb.append(residente.getApellido_paterno()).append(" ");
        }
        if (residente.getApellido_materno() != null) {
            sb.append(residente.getApellido_materno());
        }
        return sb.toString().trim();
    }

    private boolean esFemenino() {
        return residente.getGenero() != null
                && residente.getGenero().name().equalsIgnoreCase("FEMENINO");
    }

    private JLabel crearChipGenero() {
        String textoGenero = residente.getGenero() != null
                ? residente.getGenero().name().toLowerCase()
                : "";

        JLabel chip = new JLabel(textoGenero) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = esFemenino() ? new Color(255, 220, 235) : new Color(210, 230, 255);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        chip.setForeground(esFemenino() ? COLOR_FEMENINO : COLOR_MASCULINO);
        chip.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        chip.setBorder(new EmptyBorder(2, 6, 2, 6));
        chip.setOpaque(false);
        return chip;
    }

    private void configurarInteracciones() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (onSeleccion != null) {
                    onSeleccion.run();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color fondo = seleccionado ? COLOR_FONDO_SELECCIONADO : (hover ? COLOR_FONDO_HOVER : COLOR_FONDO_NORMAL);
        Color borde = seleccionado ? COLOR_BORDE_SELECCIONADO : COLOR_BORDE_NORMAL;
        float grosor = seleccionado ? 2f : 1f;

        RoundRectangle2D rr = new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 12, 12);
        g2.setColor(fondo);
        g2.fill(rr);
        g2.setColor(borde);
        g2.setStroke(new BasicStroke(grosor));
        g2.draw(rr);
        g2.dispose();

        super.paintComponent(g);
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
        repaint();
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public ResidenteDTO getResidente() {
        return residente;
    }

    public void setOnSeleccion(Runnable callback) {
        this.onSeleccion = callback;
    }
}
