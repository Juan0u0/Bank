package app.application.adapters.api.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BulkTransferRequest {
    
    @NotNull(message = "La lista de transferencias es obligatoria")
    @NotEmpty(message = "Debe incluir al menos una transferencia")
    private List<TransferRequest> transfers;
}
