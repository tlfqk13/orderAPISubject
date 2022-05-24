# orderAPISubject

* 주문, 주문이력 조회, 주문 취소 기능 구현을 통해서 주문 API 과제를 수행.

* 공통 기능 - BaseEntity와 BaseTimeEnity를 통해 엔티티들의 생성시간 수정시간을 관리 코드 중복 제거

* 주문 기능 구현하기

    - 주문 기능 요구 사항 : 고객이 상품 주문 -> 재고에서 주문 수량 감소,
                         재고가 없을때 주문 못하게 막아야함,
    - 주문 수량 보다 재고의 수가 적을 경우 -> OutOfStockException 클래스에서 처리 (RuntimeException 상속)
    - 주문 로직 -> 주문할 상품 조회
               -> 현재 로그인한 회원의 아이디 정보를 이용해서 회원 정보 조회
               -> 주문할 상품 엔티티와 주문 수량을 이용해 주문 상품 엔티티 생성
               -> 회원 정보와 주문할 상품 리스트 정보를 이용하여 주문 엔티티 생성
               -> 생성한 주문 엔티티 저장

* 주문 이력 조회하기

    - 조회한 주문 데이터를 화면에 보낼 때 사용할 OrderItemDto 생성
    - 주문 정보를 담을 OrderHistDto 생성
    - 주문 목록 조회 로직 -> 유저의 아이디와 페이징 조건을 이용하여 주문 목록을 조회
                       -> 유저의 주문 총 개수를 구함
                       -> 주문 리스트를 반복문 돌면서 구매 이력 페이지에 전달할 DTO 생성(OrderHistDto)
                       -> 주문한 상품의 대표 이미지 조회
                       -> 페이지 구현 객체를 생성하여 리턴

* 주문 취소하기

    - 주문 취소 요구 사항 : 주문을 취소하면 주문 상태 변경, 주문 할 때 감소했던 주문의 재고를 복구
    - 주문 취소 로직 -> 현재 로그인한 사용자와 주문 데이터를 생성한 사용자가 같은지 유효성 검사