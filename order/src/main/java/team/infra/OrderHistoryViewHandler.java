package team.infra;

import team.domain.*;
import team.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class OrderHistoryViewHandler {


    @Autowired
    private OrderHistoryRepository orderHistoryRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenPaymentCompleted_then_CREATE_1 (@Payload PaymentCompleted paymentCompleted) {
        try {

            if (!paymentCompleted.validate()) return;

            // view 객체 생성
            OrderHistory orderHistory = new OrderHistory();
            // view 객체에 이벤트의 Value 를 set 함
            orderHistory.setId(paymentCompleted.getId());
            orderHistory.setOrderId(paymentCompleted.getOrderId());
            orderHistory.setFlowerId(paymentCompleted.getFlowerId());
            orderHistory.setQty(paymentCompleted.getQty());
            orderHistory.setPrice(paymentCompleted.getPrice());
            // view 레파지 토리에 save
            orderHistoryRepository.save(orderHistory);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whenDeliveryCompleted_then_UPDATE_1(@Payload DeliveryCompleted deliveryCompleted) {
        try {
            if (!deliveryCompleted.validate()) return;
                // view 객체 조회

                List<OrderHistory> orderHistoryList = orderHistoryRepository.findByOrderId(deliveryCompleted.getOrderId());
                for(OrderHistory orderHistory : orderHistoryList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    orderHistory.setIsDelivered(true);
                // view 레파지 토리에 save
                orderHistoryRepository.save(orderHistory);
                }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenDeliveryCanceled_then_UPDATE_2(@Payload DeliveryCanceled deliveryCanceled) {
        try {
            if (!deliveryCanceled.validate()) return;
                // view 객체 조회

                List<OrderHistory> orderHistoryList = orderHistoryRepository.findByOrderId(deliveryCanceled.getOrderId());
                for(OrderHistory orderHistory : orderHistoryList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    orderHistory.setIsCanceled(true);
                // view 레파지 토리에 save
                orderHistoryRepository.save(orderHistory);
                }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

