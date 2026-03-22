package presentacion.vistas.componentes;

import dtos.HabitacionDTO;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * JPanel que funciona como botón seleccionable para una Habitación.
 */

public class HabitacionCardPanel extends JPanel {

    private static final Color COLOR_FONDO_NORMAL = Color.WHITE;
    private static final Color COLOR_FONDO_HOVER = new Color(240, 255, 245);
    private static final Color COLOR_FONDO_SELECCIONADO = new Color(220, 248, 232);
    private static final Color COLOR_BORDE_NORMAL = new Color(220, 220, 225);
    private static final Color COLOR_BORDE_SELECCIONADO = new Color(40, 180, 100);
    private static final Color COLOR_DISPONIBLE = new Color(40, 180, 100);
    private static final Color COLOR_NUMERO = new Color(30, 30, 40);
    private static final Color COLOR_SECUNDARIO = new Color(100, 100, 115);
    private static final Color COLOR_FEMENINO = new Color(255, 100, 150);
    private static final Color COLOR_MASCULINO = new Color(80, 140, 220);

    private final HabitacionDTO habitacion;
    private boolean seleccionado = false;
    private boolean hover = false;

    private Runnable onSeleccion;

    public HabitacionCardPanel(HabitacionDTO habitacion) {
        this.habitacion = habitacion;
        construirUI();
        configurarInteracciones();
    }

    private void construirUI() {
        setLayout(new BorderLayout(8, 0));
        setOpaque(false);
        setBorder(new EmptyBorder(8, 10, 8, 10));
        setPreferredSize(new Dimension(0, 68));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel lblNumero = new JLabel(String.valueOf(habitacion.getNumero_habitacion()));
        lblNumero.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblNumero.setForeground(COLOR_NUMERO);
        lblNumero.setPreferredSize(new Dimension(50, 40));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        String textoPiso = formatarPiso(habitacion.getPiso());
        JLabel lblPiso = new JLabel(textoPiso);
        lblPiso.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblPiso.setForeground(COLOR_SECUNDARIO);

        String textoOcupacion = habitacion.getOcupacionActual() + " / " + habitacion.getCapacidad();
        JLabel lblOcupacion = new JLabel(textoOcupacion);
        lblOcupacion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblOcupacion.setForeground(COLOR_SECUNDARIO);

        JLabel chipGenero = crearChipGenero();

        infoPanel.add(lblPiso);
        infoPanel.add(Box.createVerticalStrut(2));
        infoPanel.add(lblOcupacion);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(chipGenero);

        JPanel derechaPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        derechaPanel.setOpaque(false);
        derechaPanel.add(crearBadgeDisponible());

        add(lblNumero, BorderLayout.WEST);
        add(infoPanel, BorderLayout.CENTER);
        add(derechaPanel, BorderLayout.EAST);
    }

    private String formatarPiso(Integer piso) {
        if (piso == null) {
            return "";
        }
        return switch (piso) {
            case 1 ->
                "1er piso";
            case 2 ->
                "2do piso";
            case 3 ->
                "3er piso";
            default ->
                piso + "° piso";
        };
    }

    private boolean esFemenino() {
        return habitacion.getGenero() != null
                && habitacion.getGenero().name().equalsIgnoreCase("FEMENINO");
    }

    private JLabel crearChipGenero() {
        String textoGenero = habitacion.getGenero() != null
                ? habitacion.getGenero().name().toLowerCase()
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

    private JLabel crearBadgeDisponible() {
        JLabel badge = new JLabel("Disponible") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(220, 248, 232));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        badge.setForeground(COLOR_DISPONIBLE);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setBorder(new EmptyBorder(3, 8, 3, 8));
        badge.setOpaque(false);
        return badge;
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

    public HabitacionDTO getHabitacion() {
        return habitacion;
    }

    public void setOnSeleccion(Runnable callback) {
        this.onSeleccion = callback;
    }
}
