package fcse.soa.orders.orders;

import fcse.soa.orders.orders.model.CheckoutRequest;
import fcse.soa.orders.orders.model.ProductsOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderFacade orderFacade;

    @PostMapping
    public ResponseEntity<ProductsOrderResponse> checkout(@RequestBody CheckoutRequest checkoutDto) {
        return new ResponseEntity<>(orderFacade.processCheckoutRequest(checkoutDto), HttpStatus.OK);
    }
}
