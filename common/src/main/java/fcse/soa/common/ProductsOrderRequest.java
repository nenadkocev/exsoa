package fcse.soa.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ProductsOrderRequest {
    private String orderId;
    private Long userBalance;
    private List<ProductItem> productItems;
}
