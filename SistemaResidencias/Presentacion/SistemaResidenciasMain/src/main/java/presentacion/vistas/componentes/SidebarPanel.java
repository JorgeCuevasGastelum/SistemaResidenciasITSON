package presentacion.vistas.componentes;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class SidebarPanel extends JPanel {

    static final Color BG = new Color(42, 47, 59);
    static final Color ACTIVO = new Color(60, 66, 81);
    static final Color HOVER_BG = new Color(63, 86, 152);
    static final Color TEXTO = new Color(187, 187, 187);
    static final Color TEXTO_ACT = Color.WHITE;
    static final Color SEP_COLOR = new Color(60, 52, 90);
    static final Color LOGO_COLOR = new Color(120, 80, 200);

    private final List<EntradaMenu> entradas = new ArrayList<>();
    private EntradaMenu entradaActiva = null;
    private Consumer<String> onNavegacion;

    public SidebarPanel(List<SidebarItem> items) {
        setBackground(BG);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(250, 0));
        setBorder(new EmptyBorder(0, 0, 0, 0));

        for (SidebarItem item : items) {
            EntradaMenu entrada = new EntradaMenu(item);
            entradas.add(entrada);
            add(entrada);
            if (item.isSeparadorAbajo()) {
                add(crearSeparador());
            }
        }

        add(Box.createVerticalGlue());
    }

    public SidebarPanel(String nombreApp,
            String subtituloApp,
            String inicialLogo,
            List<SidebarItem> items) {
        setBackground(BG);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(190, 0));
        setBorder(new EmptyBorder(0, 0, 20, 0));

        add(crearLogo(nombreApp, subtituloApp, inicialLogo));
        add(Box.createVerticalStrut(8));

        for (SidebarItem item : items) {
            EntradaMenu entrada = new EntradaMenu(item);
            entradas.add(entrada);
            add(entrada);
            if (item.isSeparadorAbajo()) {
                add(crearSeparador());
            }
        }

        add(Box.createVerticalGlue());
    }

    public void setOnNavegacion(Consumer<String> callback) {
        this.onNavegacion = callback;
    }

    public void setActivo(String clavePantalla) {
        for (EntradaMenu entrada : entradas) {
            if (entrada.item.getClavePantalla().equals(clavePantalla)) {
                activar(entrada);
                return;
            }
        }
    }

    private void activar(EntradaMenu nueva) {
        if (entradaActiva != null) {
            entradaActiva.desactivar();
        }
        entradaActiva = nueva;
        nueva.activar();
    }

    private JPanel crearLogo(String nombre, String subtitulo, String inicial) {
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 14));
        logoPanel.setBackground(BG);
        logoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        logoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel circulo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(LOGO_COLOR);
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(inicial)) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(inicial, x, y);
                g2.dispose();
            }
        };
        circulo.setOpaque(false);
        circulo.setPreferredSize(new Dimension(36, 36));

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setOpaque(false);

        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblNombre.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel(subtitulo);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        lblSub.setForeground(new Color(160, 150, 190));

        textos.add(lblNombre);
        textos.add(lblSub);

        logoPanel.add(circulo);
        logoPanel.add(textos);
        return logoPanel;
    }

    private JSeparator crearSeparador() {
        JSeparator sep = new JSeparator();
        sep.setForeground(SEP_COLOR);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }

    private class EntradaMenu extends JPanel {

        final SidebarItem item;
        private boolean hover = false;
        private boolean activo = false;
        private final JLabel lblTexto;

        EntradaMenu(SidebarItem sidebarItem) {
            this.item = sidebarItem;

            setLayout(new BorderLayout());
            setBackground(BG);
            setOpaque(true);
            setPreferredSize(new Dimension(239, 56));
            setMinimumSize(new Dimension(239, 56));
            setMaximumSize(new Dimension(239, 56));
            setAlignmentX(Component.LEFT_ALIGNMENT);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            JPanel barra = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    if (activo) {
                        g.setColor(Color.WHITE);
                        g.fillRect(0, 12, getWidth(), getHeight() - 24);
                    }
                }
            };
            barra.setOpaque(false);
            barra.setPreferredSize(new Dimension(3, 0));

            lblTexto = new JLabel(item.getEtiqueta());
            lblTexto.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblTexto.setForeground(TEXTO);
            lblTexto.setBorder(new EmptyBorder(0, 18, 0, 10));
            lblTexto.setOpaque(false);

            add(barra, BorderLayout.WEST);
            add(lblTexto, BorderLayout.CENTER);

            registrarMouse();
        }

        private void registrarMouse() {
            MouseAdapter ma = new MouseAdapter() {
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
                    SidebarPanel.this.activar(EntradaMenu.this);
                    if (onNavegacion != null) {
                        onNavegacion.accept(item.getClavePantalla());
                    }
                }
            };
            addMouseListener(ma);
            lblTexto.addMouseListener(ma);
        }

        void activar() {
            activo = true;
            setBackground(ACTIVO);
            lblTexto.setForeground(TEXTO_ACT);
            lblTexto.setFont(new Font("Segoe UI", Font.BOLD, 12));
            repaint();
        }

        void desactivar() {
            activo = false;
            setBackground(BG);
            lblTexto.setForeground(TEXTO);
            lblTexto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (!activo && hover) {
                g.setColor(HOVER_BG);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}
