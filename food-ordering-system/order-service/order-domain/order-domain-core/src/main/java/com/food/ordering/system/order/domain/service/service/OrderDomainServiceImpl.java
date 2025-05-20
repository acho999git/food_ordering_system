package com.food.ordering.system.order.domain.service.service;

import com.food.ordering.system.order.domain.common.event.publisher.DomainEventPublisher;
import com.food.ordering.system.order.domain.common.valueobject.ProductId;
import com.food.ordering.system.order.domain.service.entity.Order;
import com.food.ordering.system.order.domain.service.entity.OrderItem;
import com.food.ordering.system.order.domain.service.entity.Product;
import com.food.ordering.system.order.domain.service.entity.Restaurant;
import com.food.ordering.system.order.domain.service.event.OrderCancelledEvent;
import com.food.ordering.system.order.domain.service.event.OrderCreatedEvent;
import com.food.ordering.system.order.domain.service.event.OrderPaidEvent;
import com.food.ordering.system.order.domain.service.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.food.ordering.system.order.domain.common.constant.DomainConstants.UTC;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {


    @Override
    public OrderCreatedEvent createOrder(final Order order,
                                         final Restaurant restaurant,
                                         final DomainEventPublisher<OrderCreatedEvent> orderCreatedEventPublisher) {
        validateRestaurant(restaurant);
        setOrderProductInfo(order, restaurant);
        order.initializeOrder();
        order.validateOrder();
        log.info("Order created event orderId {}", order.getId().getValue());
        return OrderCreatedEvent.builder()
                .order(order)
                .timestamp(ZonedDateTime.now(ZoneId.of(UTC)))
                .domainEventPublisher(orderCreatedEventPublisher)
                .build();
    }

    @Override
    public OrderPaidEvent payOrder(final Order order,
                                   final DomainEventPublisher<OrderPaidEvent> orderPaidEventPublisher) {
        order.pay();
        log.info("Order payed event orderId {}", order.getId().getValue());
        return OrderPaidEvent.builder()
                .order(order)
                .timestamp(ZonedDateTime.now(ZoneId.of(UTC)))
                .domainEventPublisher(orderPaidEventPublisher)
                .build();
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(final Order order,
                                                  final List<String> failureMessages,
                                                  final DomainEventPublisher<OrderCancelledEvent> orderCanceledEventPublisher) {
        order.initCancel(failureMessages);
        log.info("Order payment canceled for order - orderId {}", order.getId().getValue());
        return OrderCancelledEvent.builder()
                .order(order)
                .timestamp(ZonedDateTime.now(ZoneId.of(UTC)))
                .domainEventPublisher(orderCanceledEventPublisher)
                .build();
    }

    @Override
    public void cancelOrder(final Order order, final List<String> failureMessages) {
        order.cancel(failureMessages);
        log.info("Order canceled - orderId {}", order.getId().getValue());
    }

    @Override
    public void approveOrder(final Order order) {
        order.approve();
        log.info("Order approved - orderId {}", order.getId().getValue());
    }

    private void validateRestaurant(final Restaurant restaurant) {
        if (!restaurant.isActive()) {
            throw new OrderDomainException("Restaurant is not active");
        }
    }

    private void setOrderProductInfo(final Order order, final Restaurant restaurant) {
        final Map<ProductId, Product> restaurantProducts = groupRestaurantProductsGroupByProductId(restaurant);
        setProductNameAndPrice(order, restaurantProducts);
    }

    private void setProductNameAndPrice(final Order order, final Map<ProductId, Product> restaurantProducts) {
        order.getItems().forEach(item -> {
            final Product currentProduct = getCurrentProduct(restaurantProducts, item);
            updateProduct(item, currentProduct);
        });
    }

    private static void updateProduct(final OrderItem item, final Product currentProduct) {
        item.getProduct().updateProductWithConfirmedNameAndPrice(currentProduct.getName(), item.getPrice());
    }

    private Product getCurrentProduct(final Map<ProductId, Product> restaurantProducts, final OrderItem item) {
        return restaurantProducts.get(item.getProduct().getId());
    }

    private Map<ProductId, Product> groupRestaurantProductsGroupByProductId(final Restaurant restaurant) {
        return restaurant.getProducts().stream()
                .collect(Collectors.toMap(Product::getId, product -> product));
    }
}
