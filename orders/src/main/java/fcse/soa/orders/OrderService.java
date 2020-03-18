package fcse.soa.orders;

import fcse.soa.common.ProductsOrderRequest;
import fcse.soa.common.ProductsOrderResponse;
import fcse.soa.orders.model.CheckoutRequest;
import fcse.soa.users.persistence.UserDbEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductService productService;
    private final UserService userService;

    public ProductsOrderResponse processCheckoutRequest(CheckoutRequest checkoutDto) {
        String orderId = UUID.randomUUID().toString();
        UserDbEntity user = userService.getUserByUsername(checkoutDto.getUsername());
        ProductsOrderRequest request = ProductsOrderRequest.builder()
                .orderId(orderId)
                .userBalance(user.getBalance())
                .productItems(checkoutDto.getProductItems())
                .build();

        return productService.handleProductsOrderRequest(request);
    }
}
