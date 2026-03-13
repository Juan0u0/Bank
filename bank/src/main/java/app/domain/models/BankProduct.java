package app.domain.models;

import app.domain.enums.ProductApproval;
import app.domain.enums.ProductCategory;

public class BankProduct {
    private Long productId;
    private String productName;
    private ProductCategory category;
    private ProductApproval status;
}
