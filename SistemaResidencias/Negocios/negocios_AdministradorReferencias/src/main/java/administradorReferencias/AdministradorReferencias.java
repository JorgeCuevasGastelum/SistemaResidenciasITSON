package administradorReferencias;

import dtos.ReferenciasPagoDTO;
import objetosnegocio.ReferenciaPagoBO;

public class AdministradorReferencias implements IAdministradorReferencias {

    private final ReferenciaPagoBO referenciaPagoBO = ReferenciaPagoBO.getInstance();

    @Override
    public Long guardarReferencia(ReferenciasPagoDTO dto) {
        return referenciaPagoBO.guardarReferencia(dto);
    }
}
