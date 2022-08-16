package com.nta.teabreakorder.repository;

import com.nta.teabreakorder.model.Product;
import com.nta.teabreakorder.model.WarHouseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface WarHouseItemRepository extends JpaRepository<WarHouseItem, Long> {
    @Modifying
    @Transactional
    @Query(value = "delete from WarHouseItem w where  w.id in ?1")
    void deletes(List<Long> ids);

    WarHouseItem getByProductAndQuantityGreaterThanEqual(Product product, int quantity);

    List<WarHouseItem> getAllByQuantityIsLessThanEqual(int quantity);

}
