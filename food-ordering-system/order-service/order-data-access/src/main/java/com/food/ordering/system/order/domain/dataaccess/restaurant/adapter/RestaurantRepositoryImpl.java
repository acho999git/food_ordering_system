package com.food.ordering.system.order.domain.dataaccess.restaurant.adapter;

import com.food.ordering.system.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.dataaccess.restaurant.repository.RestaurantJpaRepository;
import com.food.ordering.system.order.domain.dataaccess.restaurant.mapper.RestaurantMapper;
import com.food.ordering.system.order.domain.service.entity.Restaurant;
import com.food.ordering.system.order.domain.service.ports.output.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.List.of;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component// output adapter
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantMapper restaurantMapper;

    @Autowired
    public RestaurantRepositoryImpl(RestaurantJpaRepository restaurantJpaRepository, RestaurantMapper restaurantMapper) {
        this.restaurantJpaRepository = restaurantJpaRepository;
        this.restaurantMapper = restaurantMapper;
    }

    @Override
    public Optional<Restaurant> findRestaurantInformation(final Restaurant restaurant) {
        final List<UUID> productIds = restaurantMapper.getProductIds(restaurant);
        final Optional<List<RestaurantEntity>> optionalRestaurant =
                restaurantJpaRepository.findByRestaurantIdAndProductIdIn(restaurant.getId().getValue(), productIds);
        return optionalRestaurant.map(restaurantMapper::toRestaurant);
    }

}
