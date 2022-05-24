package com.example.orderapisubject.service;

import com.example.orderapisubject.dto.OrderDto;
import com.example.orderapisubject.dto.OrderHistDto;
import com.example.orderapisubject.dto.OrderItemDto;
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
import org.thymeleaf.util.StringUtils;

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
        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new); // 주문할 상품을 조회합니다
        Member member = memberRepository.findByEmail(email); // 현재 로그인한 회원의 이메일 정보를 이용해서 회원 정보를 조회

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount()); // 주문할 상품 엔티티와 주문 수량을 이용하여 주문 상품 엔티티 생성
        orderItemList.add(orderItem);

        Order order = Order.createOrder(member,orderItemList); // 회원 정보와 주문할 상품 리스트 정보를 이용하여 주문 엔티티 생성
        orderRepository.save(order);

        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable){

        List<Order> orders = orderRepository.findOrders(email,pageable); // 유저의 아이디와  페이징 조건을 이용하여 주문 목록을 조회
        Long totalCount = orderRepository.countOrder(email); // 유저의 총 주문 갯수

        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        for(Order order : orders){ // 주문 리스트를 돌면서 구매 이력 페이지에 전달할 DTO 생성
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for(OrderItem orderItem : orderItems){
                OrderItemDto orderItemDto = new OrderItemDto(orderItem);
                orderHistDto.addOrderItemDto(orderItemDto);
            }
            orderHistDtos.add(orderHistDto);
        }

        return new PageImpl<OrderHistDto>(orderHistDtos,pageable,totalCount);
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email){ // 현재 로그인한 사용자와 주문 데이터를 생성한 사용자가 같은지 검사
        Member curMember = memberRepository.findByEmail(email);
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        Member savedMember = order.getMember();

        if(!StringUtils.equals(curMember.getEmail(),savedMember.getEmail())){
            return false;
        }

        return true;
    }

    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }



}
