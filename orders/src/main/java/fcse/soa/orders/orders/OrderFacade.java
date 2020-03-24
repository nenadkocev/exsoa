package fcse.soa.orders.orders;

import fcse.soa.orders.orders.model.CheckoutRequest;
import fcse.soa.orders.orders.model.ProductsOrderRequest;
import fcse.soa.orders.orders.model.ProductsOrderResponse;
import fcse.soa.orders.orders.persistence.OrderDbEntity;
import fcse.soa.orders.orders.persistence.OrderRepository;
import fcse.soa.orders.products.ProductService;
import fcse.soa.users.persistence.UserDbEntity;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@Slf4j
public class OrderFacade {

    private final ProductService productService;
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final CircuitBreaker green;

    public OrderFacade(ProductService productService, UserService userService, OrderRepository orderRepository,
                       CircuitBreakerFactory cbf) {
        this.productService = productService;
        this.userService = userService;
        this.orderRepository  = orderRepository;
        this.green = cbf.create("green");
    }

    public ProductsOrderResponse processCheckoutRequest(CheckoutRequest checkoutDto) {
        UserDbEntity user = green.run(() -> {
            suceedOrNot(checkoutDto.getUsername());
            return userService.getUserByUsername(checkoutDto.getUsername());
        }, this::fallbackFromService);

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

    @SneakyThrows
    private void suceedOrNot(String username) {
        if(username.equals("exception")) {
            throw new RuntimeException("User service does not work");
        }
        if(username.equals("slowCall")) {
            Thread.sleep(5000);
        }
    }

    private UserDbEntity fallbackFromService(Throwable throwable) {
        log.error("Fallback method triggered by green circuit breaker", throwable);
        return null;
    }
}
