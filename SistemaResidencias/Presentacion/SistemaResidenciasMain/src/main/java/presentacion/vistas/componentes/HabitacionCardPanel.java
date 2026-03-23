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
    private static final Color COLOR_FONDO_HOVER = new Color(248, 249, 250);
    private static final Color COLOR_FONDO_SELECCIONADO = new Color(240, 244, 255);
    private static final Color COLOR_BORDE_NORMAL = new Color(200, 200, 210);
    private static final Color COLOR_BORDE_SELECCIONADO = new Color(120, 80, 200);

    private static final Color COLOR_FEMENINO_BG = new Color(208, 105, 186);
    private static final Color COLOR_MASCULINO_BG = new Color(63, 84, 150);
    private static final Color COLOR_TEXTO_BLANCO = Color.WHITE;

    private static final Color COLOR_DISPONIBLE_BG = new Color(75, 210, 100);

    private static final Color COLOR_NUMERO = new Color(40, 45, 50);
    private static final Color COLOR_SECUNDARIO = new Color(80, 80, 90);

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
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(12, 15, 12, 15));
        setPreferredSize(new Dimension(256, 105));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel mainWrapper = new JPanel();
        mainWrapper.setLayout(new BoxLayout(mainWrapper, BoxLayout.Y_AXIS));
        mainWrapper.setOpaque(false);

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);

        JLabel lblNumero = new JLabel(String.valueOf(habitacion.getNumero_habitacion()));
        lblNumero.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNumero.setForeground(COLOR_NUMERO);

        topRow.add(lblNumero, BorderLayout.WEST);
        topRow.add(crearBadgeDisponible(), BorderLayout.EAST);

        JPanel midRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        midRow.setOpaque(false);

        String textoPiso = " " + formatarPiso(habitacion.getPiso());
        JLabel lblPiso = new JLabel(textoPiso);
        lblPiso.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblPiso.setForeground(COLOR_NUMERO);

        java.net.URL urlUbi = getClass().getResource("/UbiIcon.png"); 
        if (urlUbi != null) {
            ImageIcon iconoUbi = new ImageIcon(urlUbi);
            java.awt.Image imgUbi = iconoUbi.getImage().getScaledInstance(14, 14, java.awt.Image.SCALE_SMOOTH);
            lblPiso.setIcon(new ImageIcon(imgUbi));
        }

        JLabel spacer = new JLabel("      ");

        String textoOcupacion = " " + habitacion.getOcupacionActual() + "/" + habitacion.getCapacidad(); 
        JLabel lblOcupacion = new JLabel(textoOcupacion);
        lblOcupacion.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblOcupacion.setForeground(COLOR_NUMERO);

        java.net.URL urlPerson = getClass().getResource("/PersonIcon.png");
        if (urlPerson != null) {
            ImageIcon iconoPerson = new ImageIcon(urlPerson);
            java.awt.Image imgPerson = iconoPerson.getImage().getScaledInstance(14, 14, java.awt.Image.SCALE_SMOOTH);
            lblOcupacion.setIcon(new ImageIcon(imgPerson));
        }

        midRow.add(lblPiso);
        midRow.add(spacer);
        midRow.add(lblOcupacion);

        JPanel botRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        botRow.setOpaque(false);
        botRow.add(crearChipGenero());

        mainWrapper.add(topRow);
        mainWrapper.add(Box.createVerticalStrut(4));
        mainWrapper.add(midRow);
        mainWrapper.add(Box.createVerticalStrut(8));
        mainWrapper.add(botRow);

        add(mainWrapper, BorderLayout.CENTER);
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
                && habitacion.getGenero().name().equalsIgnoreCase("MUJER");
    }

    private boolean esHombre() {
        return habitacion.getGenero() != null
                && habitacion.getGenero().name().equalsIgnoreCase("HOMBRE");
    }

    private JLabel crearChipGenero() {
        String textoGenero = "otro";
        Color bgColor = new Color(150, 150, 160);

        if (habitacion.getGenero() != null) {
            if (esFemenino()) {
                textoGenero = "femenino";
                bgColor = COLOR_FEMENINO_BG;
            } else if (esHombre()) {
                textoGenero = "masculino";
                bgColor = COLOR_MASCULINO_BG;
            }
        }

        final Color colorFinal = bgColor;

        JLabel chip = new JLabel(textoGenero) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(colorFinal);

                int radius = getHeight();
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        chip.setForeground(COLOR_TEXTO_BLANCO);
        chip.setFont(new Font("Segoe UI", Font.BOLD, 11));
        chip.setBorder(new EmptyBorder(3, 12, 3, 12));
        chip.setOpaque(false);
        return chip;
    }

    private JLabel crearBadgeDisponible() {
        JLabel badge = new JLabel("Disponible") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_DISPONIBLE_BG);

                int radius = getHeight();
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        badge.setForeground(COLOR_TEXTO_BLANCO);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setBorder(new EmptyBorder(3, 10, 3, 10));
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

        RoundRectangle2D rr = new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 8, 8);
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
