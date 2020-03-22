package fcse.soa.orders.products;

import fcse.soa.orders.orders.model.ProductsOrderRequest;
import fcse.soa.orders.orders.model.ProductsOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping(value = "/order")
    ResponseEntity<ProductsOrderResponse> orderProducts(@RequestBody ProductsOrderRequest request) {
        return new ResponseEntity<>(productService.handleProductsOrderRequest(request), HttpStatus.OK);
    }
}
