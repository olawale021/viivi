package com.example.viivi.models.orders;

import com.example.viivi.models.orders.OrdersModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<OrdersModel, Long> {
    List<OrdersModel> findByUserId(Long userId);
}
