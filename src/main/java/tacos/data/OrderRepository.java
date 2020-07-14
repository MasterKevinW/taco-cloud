package tacos.data;

import tacos.dto.Order;

public interface OrderRepository {

    Order save(Order order);
}
