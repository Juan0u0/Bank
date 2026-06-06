package app.application.adapters.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankProductResponse {
    private Long productId;
    private String productName;
    private String category;
    private String status;
}
