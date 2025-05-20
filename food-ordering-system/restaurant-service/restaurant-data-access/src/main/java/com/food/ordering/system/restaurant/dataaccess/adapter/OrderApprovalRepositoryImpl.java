package com.food.ordering.system.restaurant.dataaccess.adapter;
import com.food.ordering.system.restaurant.application.service.port.output.repository.OrderApprovalRepository;
import com.food.ordering.system.restaurant.dataaccess.mapper.RestaurantDataAccessMapper;
import com.food.ordering.system.restaurant.dataaccess.repository.OrderApprovalJpaRepository;
import com.food.ordering.system.restaurant.domain.core.entity.OrderApproval;
import org.springframework.stereotype.Component;

@Component
public class OrderApprovalRepositoryImpl implements OrderApprovalRepository {

    private final OrderApprovalJpaRepository orderApprovalJpaRepository;
    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    public OrderApprovalRepositoryImpl(OrderApprovalJpaRepository orderApprovalJpaRepository,
                                       RestaurantDataAccessMapper restaurantDataAccessMapper) {
        this.orderApprovalJpaRepository = orderApprovalJpaRepository;
        this.restaurantDataAccessMapper = restaurantDataAccessMapper;
    }

    @Override
    public OrderApproval save(OrderApproval orderApproval) {
        return restaurantDataAccessMapper
                .orderApprovalEntityToOrderApproval(orderApprovalJpaRepository
                        .save(restaurantDataAccessMapper.orderApprovalToOrderApprovalEntity(orderApproval)));
    }

}
