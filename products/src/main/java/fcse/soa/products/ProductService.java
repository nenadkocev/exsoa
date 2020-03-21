package fcse.soa.products;

import fcse.soa.common.ProductItem;
import fcse.soa.common.ProductsOrderRequest;
import fcse.soa.common.ProductsOrderResponse;
import fcse.soa.products.persistence.ProductDbEntity;
import fcse.soa.products.persistence.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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

        List<ProductDbEntity> products = productRepository.findAllByName((String[]) request.getProductItems().stream()
                .map(ProductItem::getProductName).toArray());

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

    @PostConstruct
    private void populateTable() {
        var product1 = new ProductDbEntity();
        product1.setName("Cola-cola");
        product1.setPrice(65L);
        product1.setQuantity(120L);

        var product2 = new ProductDbEntity();
        product2.setName("Macaroni");
        product2.setPrice(55L);
        product2.setQuantity(250L);

        var product3 = new ProductDbEntity();
        product3.setName("Toilet Paper");
        product3.setPrice(105L);
        product3.setQuantity(120L);

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
    }
}
