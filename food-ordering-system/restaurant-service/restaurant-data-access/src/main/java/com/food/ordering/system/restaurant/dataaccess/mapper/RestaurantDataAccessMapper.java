package com.food.ordering.system.restaurant.dataaccess.mapper;

import com.food.ordering.system.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.dataaccess.restaurant.exception.RestaurantDataAccessException;
import com.food.ordering.system.order.domain.common.valueobject.OrderId;
import com.food.ordering.system.order.domain.common.valueobject.ProductId;
import com.food.ordering.system.order.domain.common.valueobject.RestaurantId;
import com.food.ordering.system.restaurant.dataaccess.entity.OrderApprovalEntity;
import com.food.ordering.system.restaurant.domain.core.entity.OrderApproval;
import com.food.ordering.system.restaurant.domain.core.entity.OrderDetail;
import com.food.ordering.system.restaurant.domain.core.entity.Product;
import com.food.ordering.system.restaurant.domain.core.entity.Restaurant;
import com.food.ordering.system.restaurant.domain.core.valueobject.OrderApprovalId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantDataAccessMapper {

    public List<UUID> restaurantToRestaurantProducts(Restaurant restaurant) {
        return restaurant.getOrderDetail().getProducts().stream()
                .map(product -> product.getId().getValue())
                .collect(Collectors.toList());
    }

    public Restaurant restaurantEntityToRestaurant(List<RestaurantEntity> restaurantEntities) {
        RestaurantEntity restaurantEntity =
                restaurantEntities.stream().findFirst().orElseThrow(() ->
                        new RestaurantDataAccessException("No restaurants found!"));

        List<Product> restaurantProducts = restaurantEntities.stream().map(entity ->
                        Product.builder()
                                .setProductId(new ProductId(entity.getProductId()))
                                .setProductName(entity.getProductName())
                                .setPrice(entity.getProductPrice())
                                .setAvailable(entity.getProductAvailable())
                                .build())
                .collect(Collectors.toList());

        return Restaurant.builder()
                .setRestaurantId(new RestaurantId(restaurantEntity.getRestaurantId()))
                .setOrderDetail(OrderDetail.builder()
                        .setProducts(restaurantProducts)
                        .build())
                .setActive(restaurantEntity.getRestaurantActive())
                .build();
    }

    public OrderApprovalEntity orderApprovalToOrderApprovalEntity(OrderApproval orderApproval) {
        return OrderApprovalEntity.builder()
                .id(orderApproval.getId().getValue())
                .restaurantId(orderApproval.getRestaurantId().getValue())
                .orderId(orderApproval.getOrderId().getValue())
                .status(orderApproval.getOrderApprovalStatus())
                .build();
    }

    public OrderApproval orderApprovalEntityToOrderApproval(OrderApprovalEntity orderApprovalEntity) {
        return OrderApproval.builder()
                .setOrderApprovalId(new OrderApprovalId(orderApprovalEntity.getId()))
                .setRestaurantId(new RestaurantId(orderApprovalEntity.getRestaurantId()))
                .setOrderId(new OrderId(orderApprovalEntity.getOrderId()))
                .setOrderApprovalStatus(orderApprovalEntity.getStatus())
                .build();
    }

}
