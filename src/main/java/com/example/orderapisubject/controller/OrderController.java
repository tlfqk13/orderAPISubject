package com.example.orderapisubject.controller;

import com.example.orderapisubject.dto.OrderDto;
import com.example.orderapisubject.dto.OrderHistDto;
import com.example.orderapisubject.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping(value = "/order")
    public @ResponseBody
    ResponseEntity order(@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult, Principal principal) {

        if (bindingResult.hasErrors()) { // 주문 정보를 받는 orderDto 객체에 데이터 바인딩 시 에러 검사
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName(); // 현재 로그인 유저의 정보를 얻기 위해서 principal 객체를 넘겨 줄 경우 해당 객체에 직접 접근 가능.
        Long orderId;

        try {
            orderId = orderService.order(orderDto, email); // 화면으로부터 넘어오는 주문 정보와 회원의 이메일 정보를 이용하여 주문 로직을 호출
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    @GetMapping(value = {"/orders","/orders/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,4 );

        Page<OrderHistDto> orderHistDtoList = orderService.getOrderList(principal.getName(),pageable);

        model.addAttribute("orders",orderHistDtoList);
        model.addAttribute("page",pageable.getPageNumber());
        model.addAttribute("maxPage",5);

        return "order/orderHist";
    }

    @PostMapping("/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder(@PathVariable("orderId")Long orderId, Principal principal){

        if(!orderService.validateOrder(orderId,principal.getName())){
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.",HttpStatus.FORBIDDEN);
        }
        orderService.cancelOrder(orderId);
        return new ResponseEntity<Long>(orderId,HttpStatus.OK);
    }
}
