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
    private static final Color COLOR_FONDO_HOVER = new Color(248, 249, 250);
    private static final Color COLOR_FONDO_SELECCIONADO = new Color(240, 244, 255);
    private static final Color COLOR_BORDE_NORMAL = new Color(200, 200, 210);
    private static final Color COLOR_BORDE_SELECCIONADO = new Color(120, 80, 200);

    private static final Color COLOR_FEMENINO_BG = new Color(208, 105, 186);
    private static final Color COLOR_MASCULINO_BG = new Color(63, 84, 150);
    private static final Color COLOR_CHIP_TEXT = Color.WHITE;

    private static final Color COLOR_NOMBRE = new Color(40, 45, 50);
    private static final Color COLOR_CARRERA = new Color(80, 80, 90);
    private static final Color COLOR_LINK = new Color(28, 62, 175);

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
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(12, 15, 12, 15));
        setPreferredSize(new Dimension(256, 85));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);

        JLabel lblNombre = new JLabel(nombreCompleto());
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblNombre.setForeground(COLOR_NOMBRE);

        JLabel lblCarrera = new JLabel(residente.getCarrera() != null ? residente.getCarrera() : "Sin carrera");
        lblCarrera.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCarrera.setForeground(COLOR_CARRERA);

        topPanel.add(lblNombre);
        topPanel.add(Box.createVerticalStrut(2));
        topPanel.add(lblCarrera);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        JLabel chipGenero = crearChipGenero();

        JLabel lblVista = new JLabel(" Vista rapida");
        lblVista.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblVista.setForeground(COLOR_LINK);
        lblVista.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        java.net.URL urlIcono = getClass().getResource("/OjoIcon.png");

        if (urlIcono != null) {
            lblVista.setIcon(new ImageIcon(urlIcono));
        } else {
            System.out.println("No se encontró la imagen en la ruta especificada.");
        }

        bottomPanel.add(chipGenero, BorderLayout.WEST);
        bottomPanel.add(lblVista, BorderLayout.EAST);

        add(topPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
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
                && residente.getGenero().name().equalsIgnoreCase("MUJER");
    }

    private boolean esHombre() {
        return residente.getGenero() != null
                && residente.getGenero().name().equalsIgnoreCase("HOMBRE");
    }

    private JLabel crearChipGenero() {
        String textoGenero = "Otro";
        Color bgColor = new Color(150, 150, 160);

        if (residente.getGenero() != null) {
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

        chip.setForeground(COLOR_CHIP_TEXT);
        chip.setFont(new Font("Segoe UI", Font.BOLD, 11));
        chip.setBorder(new EmptyBorder(3, 12, 3, 12));
        chip.setOpaque(false);
        chip.setHorizontalAlignment(SwingConstants.CENTER);

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

    public ResidenteDTO getResidente() {
        return residente;
    }

    public void setOnSeleccion(Runnable callback) {
        this.onSeleccion = callback;
    }
}
