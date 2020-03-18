package fcse.soa.common;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductsOrderResponse {
    private String orderId;
    private Long totalPrice;
    private List<String> errors = new ArrayList<>();
}
