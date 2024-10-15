package com.example.viivi.models.orders;

import com.example.viivi.models.orders.OrderItemsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItemsModel, Long> {
    List<OrderItemsModel> findByOrderId(Long orderId);
}
