package com.nta.teabreakorder.repository;

import com.nta.teabreakorder.enums.OrderType;
import com.nta.teabreakorder.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Modifying
    @Transactional
    @Query(value = "delete from Order o where o.id in ?1")
    void deletes(List<Long> ids);

    @Query(value="select distinct c from Order c join fetch c.orderDetailList od where c.createdAt between ?1 and ?2 and c.orderType=?3")
    List<Order> getAllByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, OrderType type);

    @Query("select o.createdAt from Order o where o.id=?1")
    LocalDateTime getCreatedAtBy(Long id);
}
