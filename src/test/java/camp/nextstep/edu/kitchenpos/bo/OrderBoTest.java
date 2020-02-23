package camp.nextstep.edu.kitchenpos.bo;

import camp.nextstep.edu.kitchenpos.dao.OrderDao;
import camp.nextstep.edu.kitchenpos.dao.OrderLineItemDao;
import camp.nextstep.edu.kitchenpos.dao.OrderTableDao;
import camp.nextstep.edu.kitchenpos.dao.TableGroupDao;
import camp.nextstep.edu.kitchenpos.model.Order;
import camp.nextstep.edu.kitchenpos.model.OrderLineItem;
import camp.nextstep.edu.kitchenpos.model.OrderTable;
import camp.nextstep.edu.kitchenpos.model.TableGroup;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.context.annotation.Description;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderBoTest {
    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @InjectMocks
    private OrderBo orderBo;

    @Description("주문을 성공적으로 한다.")
    @Test
    void create() {
        // given
        OrderLineItem orderLineItem = new OrderLineItem();
        List<OrderLineItem> orderLineItems = Lists.newArrayList(orderLineItem);

        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);

        OrderTable orderTable = new OrderTable();
        orderTable.setId(2L);
        orderTable.setTableGroupId(tableGroup.getId());

        Order order = new Order();
        order.setOrderLineItems(orderLineItems);
        order.setOrderTableId(orderTable.getId());

        given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.of(orderTable));

        given(tableGroupDao.findById(anyLong()))
                .willReturn(Optional.of(tableGroup));

        given(orderTableDao.findAllByTableGroupId(anyLong()))
                .willReturn(Lists.list(orderTable));

        given(orderDao.save(any(Order.class)))
                .willAnswer((Answer<Order>) invocation -> {
                    order.setId(1L);
                    return order;
                });

        given(orderLineItemDao.save(any(OrderLineItem.class)))
                .willAnswer((Answer<OrderLineItem>) invocation -> {
                    orderLineItem.setSeq(11L);
                    return orderLineItem;
                });

        // when
        Order result = orderBo.create(order);

        // then
        assertThat(result).isEqualTo(order);
    }
}
