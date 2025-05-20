package com.food.ordering.system.order.domain.service.ports.output.repository;

import com.food.ordering.system.order.domain.service.entity.Restaurant;

import java.util.Optional;

//Output port
public interface RestaurantRepository {

    Optional<Restaurant> findRestaurantInformation(final Restaurant restaurant);

}
