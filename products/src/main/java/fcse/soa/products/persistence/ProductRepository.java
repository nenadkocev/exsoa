package fcse.soa.products.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<ProductDbEntity, Long> {

    ProductDbEntity findByName(String name);

}
