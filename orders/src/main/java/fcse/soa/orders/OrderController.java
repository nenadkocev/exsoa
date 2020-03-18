package fcse.soa.orders;

import fcse.soa.orders.model.CheckoutRequest;
import fcse.soa.orders.model.CheckoutResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<CheckoutResponse> checkout(@RequestBody CheckoutRequest checkoutDto) {
        return new ResponseEntity<>(orderService.processCheckoutRequest(checkoutDto), HttpStatus.OK);
    }
}
