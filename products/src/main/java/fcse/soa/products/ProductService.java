package fcse.soa.products;

import fcse.soa.common.ProductItem;
import fcse.soa.common.ProductsOrderRequest;
import fcse.soa.common.ProductsOrderResponse;
import fcse.soa.products.persistence.ProductDbEntity;
import fcse.soa.products.persistence.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    ProductsOrderResponse handleProductsOrderRequest(ProductsOrderRequest request) {
        ProductsOrderResponse response = new ProductsOrderResponse();
        response.setOrderId(request.getOrderId());

        var productNames = request.getProductItems().stream()
                .map(ProductItem::getProductName)
                .collect(Collectors.toList());
        List<ProductDbEntity> products = productRepository.findAllByName(productNames);

        AtomicLong totalPrice = new AtomicLong();
        request.getProductItems().forEach(productItem -> {
            ProductDbEntity productDbEntity = products.stream()
                    .filter(p -> p.getName().equals(productItem.getProductName()))
                    .findFirst().get();
            if(productItem.getQuantity() > productDbEntity.getQuantity()) {
                response.getErrors().add(String.format("Product %s does not have the required quantity in stock.", productItem.getProductName()));
            }
            productDbEntity.setQuantity(productDbEntity.getQuantity() - productItem.getQuantity());
            totalPrice.addAndGet(productDbEntity.getPrice() * productItem.getQuantity());
        });

        response.setTotalPrice(totalPrice.get());
        if(response.getTotalPrice() < request.getUserBalance() && response.getErrors().size() == 0) {
            productRepository.saveAll(products);
        }
        return response;
    }
}
