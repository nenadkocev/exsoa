package fcse.soa.orders.orders.model;

import lombok.Data;

import java.util.List;

@Data
public class CheckoutRequest {

    private String username;
    private List<ProductItem> productItems;

}
