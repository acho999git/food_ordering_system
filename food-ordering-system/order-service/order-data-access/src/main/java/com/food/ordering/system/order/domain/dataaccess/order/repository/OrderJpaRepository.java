package com.food.ordering.system.order.domain.dataaccess.order.repository;

import com.food.ordering.system.order.domain.dataaccess.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository//output port/adapter to the db
public interface OrderJpaRepository extends JpaRepository<OrderEntity, UUID> {

    Optional<OrderEntity> findByTrackingId(final UUID trackingId);

}
