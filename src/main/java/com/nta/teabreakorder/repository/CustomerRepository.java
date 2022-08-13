package com.nta.teabreakorder.repository;

import com.nta.teabreakorder.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Modifying
    @Transactional
    @Query(value = "delete from Customer c where c.id in ?1")
    void deletes(List<Long> ids);
}
