package app.domain.models;

import app.domain.enums.ProductApproval;
import app.domain.enums.ProductCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class BankProduct {
    private Long productId;
    private String productName;
    private ProductCategory category;
    private ProductApproval status;
}
