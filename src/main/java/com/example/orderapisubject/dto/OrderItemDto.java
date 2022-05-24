package com.example.orderapisubject.dto;

import com.example.orderapisubject.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 조회한 주문 데이터를 화면에 보낼 때 사용할 DTO 클래스
public class OrderItemDto {

    public OrderItemDto(OrderItem orderItem){
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
    }

    private String itemNm; // 상품명

    private int count; // 주문 수량

    private int orderPrice;
}
