package fcse.soa.orders.orders.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProductsOrderRequest {
    private Long userBalance;
    private List<ProductItem> productItems;
}
