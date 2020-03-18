package fcse.soa.orders.model;

import fcse.soa.common.ProductItem;
import lombok.Data;

import java.util.List;

@Data
public class CheckoutRequest {

    private String username;
    private List<ProductItem> productItems;

}
