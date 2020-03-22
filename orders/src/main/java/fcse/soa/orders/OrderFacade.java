package fcse.soa.orders;

import fcse.soa.common.ProductsOrderRequest;
import fcse.soa.common.ProductsOrderResponse;
import fcse.soa.users.persistence.UserDbEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class OrderFacade {

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

        ProductsOrderResponse response = productService.handleProductsOrderRequest(request);
        if(response.getErrors().size() == 0) {
            userService.updateUsersBalance(user.getUsername(), user.getBalance() - response.getTotalPrice());
        }

        return response;
    }
}
