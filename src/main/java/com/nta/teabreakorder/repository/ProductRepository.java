package com.nta.teabreakorder.repository;

import com.nta.teabreakorder.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Transactional
    @Modifying
    @Query(value = "delete from Product p where p.id in ?1")
    void deletes(List<Long> ids);


    @Query(value = "select p.id, p.name, p.base_price, p.price, p.code, p.type\n" +
            "from products p\n" +
            "where p.id not in (select p1.id\n" +
            "                   from warhouse w\n" +
            "                            inner join products p1 on p1.id = w.product_id)\n", nativeQuery = true)
    List<Product> getAllNotUsedInWarHouse();

    @Query(value = "select p.id, p.name, p.base_price, p.price, p.code, p.type\n" +
            "from products p", nativeQuery = true)
    List<Product> getAll();
}
