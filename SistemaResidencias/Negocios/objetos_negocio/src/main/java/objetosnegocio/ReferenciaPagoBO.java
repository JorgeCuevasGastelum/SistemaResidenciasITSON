package objetosnegocio;

import dtos.ReferenciasPagoDTO;
import implementaciones.AccesoDatos;
import interfaz.IAccesoDatos;

public class ReferenciaPagoBO {

    private static ReferenciaPagoBO instance;

    public static ReferenciaPagoBO getInstance() {
        if (instance == null) {
            instance = new ReferenciaPagoBO();
        }
        return instance;
    }

    public Long guardarReferencia(ReferenciasPagoDTO dto) {
        IAccesoDatos accesoDatos = new AccesoDatos();
        return accesoDatos.guardarReferenciaPago(dto);
    }
}
