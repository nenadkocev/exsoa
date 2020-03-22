package fcse.soa.orders.orders;

import fcse.soa.orders.orders.model.CheckoutRequest;
import fcse.soa.orders.orders.model.ProductsOrderRequest;
import fcse.soa.orders.orders.model.ProductsOrderResponse;
import fcse.soa.orders.orders.persistence.OrderDbEntity;
import fcse.soa.orders.orders.persistence.OrderRepository;
import fcse.soa.orders.products.ProductService;
import fcse.soa.users.persistence.UserDbEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class OrderFacade {

    private final ProductService productService;
    private final UserService userService;
    private final OrderRepository orderRepository;

    public ProductsOrderResponse processCheckoutRequest(CheckoutRequest checkoutDto) {
        UserDbEntity user = userService.getUserByUsername(checkoutDto.getUsername());
        ProductsOrderRequest request = ProductsOrderRequest.builder()
                .userBalance(user.getBalance())
                .productItems(checkoutDto.getProductItems())
                .build();

        ProductsOrderResponse response = productService.handleProductsOrderRequest(request);
        if(response.getErrors().size() == 0) {
            String orderId = UUID.randomUUID().toString();
            userService.updateUsersBalance(user.getUsername(), user.getBalance() - response.getTotalPrice());
            OrderDbEntity orderDbEntity = new OrderDbEntity();
            orderDbEntity.setUserId(user.getId());
            orderDbEntity.setProducts(response.getProducts());
            orderDbEntity.setUuid(orderId);
            orderRepository.save(orderDbEntity);
        }

        return response;
    }
}
