package fcse.soa.products;

import fcse.soa.common.ProductsOrderRequest;
import fcse.soa.common.ProductsOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
