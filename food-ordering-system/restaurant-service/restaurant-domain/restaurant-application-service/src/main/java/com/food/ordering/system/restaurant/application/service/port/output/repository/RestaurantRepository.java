package com.food.ordering.system.restaurant.application.service.port.output.repository;

import com.food.ordering.system.restaurant.domain.core.entity.Restaurant;

import java.util.Optional;

public interface RestaurantRepository {
    Optional<Restaurant> findRestaurantInformation(final Restaurant restaurant);
}
