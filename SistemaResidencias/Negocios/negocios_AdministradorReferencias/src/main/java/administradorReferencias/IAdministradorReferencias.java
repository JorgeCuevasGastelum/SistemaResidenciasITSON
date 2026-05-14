package administradorReferencias;

import dtos.ReferenciasPagoDTO;

public interface IAdministradorReferencias {
    Long guardarReferencia(ReferenciasPagoDTO dto);
}
