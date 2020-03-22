package fcse.soa.orders;

import fcse.soa.common.EndpointsConfigurationProperties;
import fcse.soa.common.ProductsOrderRequest;
import fcse.soa.common.ProductsOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class ProductService {

    public static final String ORDER = "/product/order";

    private final EndpointsConfigurationProperties endpoints;
    private final RestTemplate restTemplate;

    public ProductsOrderResponse handleProductsOrderRequest(ProductsOrderRequest request) {
        String url = endpoints.getProductsUrl() + ORDER;
        ResponseEntity<ProductsOrderResponse> response = restTemplate.postForEntity(url, request, ProductsOrderResponse.class);
        return response.getBody();
    }
}
