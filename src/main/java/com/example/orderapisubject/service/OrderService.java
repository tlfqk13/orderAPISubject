package com.example.orderapisubject.service;

import com.example.orderapisubject.dto.OrderDto;
import com.example.orderapisubject.entity.Item;
import com.example.orderapisubject.entity.Member;
import com.example.orderapisubject.entity.Order;
import com.example.orderapisubject.entity.OrderItem;
import com.example.orderapisubject.repository.ItemRepository;
import com.example.orderapisubject.repository.MemberRepository;
import com.example.orderapisubject.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    public Long order(OrderDto orderDto, String email){
        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

        Order order = Order.createOrder(member,orderItemList);
        orderRepository.save(order);

        return order.getId();
    }
}
