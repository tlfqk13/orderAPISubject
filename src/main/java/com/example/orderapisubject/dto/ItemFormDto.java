package com.example.orderapisubject.dto;

import com.example.orderapisubject.constant.ItemSellStatus;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다")
    private String itemName;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "이름은 필수 입력 값입니다")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;



}
