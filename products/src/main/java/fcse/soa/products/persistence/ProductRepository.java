package fcse.soa.products.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductDbEntity, Long> {

    ProductDbEntity findByName(String name);

    List<ProductDbEntity> findAllByName(String[] names);
}
