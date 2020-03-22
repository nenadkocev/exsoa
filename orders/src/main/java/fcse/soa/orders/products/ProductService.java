package fcse.soa.orders.products;

import fcse.soa.orders.orders.model.ProductItem;
import fcse.soa.orders.orders.model.ProductsOrderRequest;
import fcse.soa.orders.orders.model.ProductsOrderResponse;
import fcse.soa.orders.products.persistence.ProductDbEntity;
import fcse.soa.orders.products.persistence.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductsOrderResponse handleProductsOrderRequest(ProductsOrderRequest request) {
        ProductsOrderResponse response = new ProductsOrderResponse();

        Set<ProductDbEntity> products = getProductsByName(request.getProductItems());

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
        if(response.getTotalPrice() > request.getUserBalance()) {
            response.getErrors().add("Not enough funds...");
        }
        if(response.getErrors().size() == 0) {
            productRepository.saveAll(products);
        }
        response.setProducts(products);
        return response;
    }

    private Set<ProductDbEntity> getProductsByName(List<ProductItem> productItems) {
        return productItems.stream()
                .map(item -> productRepository.findByName(item.getProductName()))
                .collect(Collectors.toSet());
    }

    @PostConstruct
    private void populateTable() {
        var product1 = new ProductDbEntity();
        product1.setName("Coca-cola");
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
