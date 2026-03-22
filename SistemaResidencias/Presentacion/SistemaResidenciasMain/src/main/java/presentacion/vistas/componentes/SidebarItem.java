package presentacion.vistas.componentes;

/**
 * Representa una entrada del menú lateral (sidebar).
 */
public class SidebarItem {

    private final String  etiqueta;
    private final String  clavePantalla;
    private final String  icono;
    private final boolean separadorAbajo;

    public SidebarItem(String etiqueta, String clavePantalla, String icono, boolean separadorAbajo) {
        this.etiqueta        = etiqueta;
        this.clavePantalla   = clavePantalla;
        this.icono           = icono != null ? icono : "";
        this.separadorAbajo  = separadorAbajo;
    }

    public SidebarItem(String etiqueta, String clavePantalla, String icono) {
        this(etiqueta, clavePantalla, icono, false);
    }

    public SidebarItem(String etiqueta, String clavePantalla) {
        this(etiqueta, clavePantalla, "", false);
    }

    public String  getEtiqueta()       { return etiqueta; }
    public String  getClavePantalla()  { return clavePantalla; }
    public String  getIcono()          { return icono; }
    public boolean isSeparadorAbajo()  { return separadorAbajo; }
}