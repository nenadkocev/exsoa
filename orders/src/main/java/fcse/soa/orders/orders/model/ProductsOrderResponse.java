package fcse.soa.orders.orders.model;

import fcse.soa.orders.products.persistence.ProductDbEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ProductsOrderResponse {
    private Long totalPrice;
    private Set<ProductDbEntity> products;
    private List<String> errors = new ArrayList<>();
}
