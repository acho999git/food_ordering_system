package com.food.ordering.system.order.domain.dataaccess.restaurant.mapper;

import com.food.ordering.system.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.dataaccess.restaurant.exception.RestaurantDataAccessException;
import com.food.ordering.system.order.domain.service.entity.Product;
import com.food.ordering.system.order.domain.service.entity.Restaurant;
import com.food.ordering.system.order.domain.common.valueobject.ProductId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantMapper {

    public List<UUID> getProductIds(final Restaurant restaurant) {
        return restaurant.getProducts().stream()
                .map(item -> item.getId().getValue())
                .collect(Collectors.toList());
    }

    public Restaurant toRestaurant(final List<RestaurantEntity> entities) {
        final RestaurantEntity restaurantEntity = getRestaurantEntity(entities);
        List<Product> products = getProducts(entities);
        return new Restaurant(products, restaurantEntity.getRestaurantActive(),
                restaurantEntity.getRestaurantId());
    }

    private RestaurantEntity getRestaurantEntity(List<RestaurantEntity> entities) {
        final RestaurantEntity restaurantEntity = entities.isEmpty() ? null : entities.get(0);
        if (restaurantEntity == null) {
            throw new RestaurantDataAccessException("Restaurant not found");
        }
        return restaurantEntity;
    }

    private List<Product> getProducts(final List<RestaurantEntity> entities) {
        return entities.stream()
                .map(item -> new Product(new ProductId(item.getProductId())))
                .toList();
    }
}
