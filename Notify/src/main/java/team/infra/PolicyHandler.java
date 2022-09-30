package team.infra;

import javax.naming.NameParser;

import javax.naming.NameParser;
import javax.transaction.Transactional;

import team.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import team.domain.*;


@Service
@Transactional
public class PolicyHandler{
    @Autowired NotifyRepository notifyRepository;
    
    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}

    @StreamListener(value=KafkaProcessor.INPUT, condition="headers['type']=='PaymentCompleted'")
    public void wheneverPaymentCompleted_NotifyMessage(@Payload PaymentCompleted paymentCompleted){

        PaymentCompleted event = paymentCompleted;
        System.out.println("\n\n##### listener NotifyMessage : " + paymentCompleted + "\n\n");


        

        // Sample Logic //
        Notify.notifyMessage(event);
        

    }
    @StreamListener(value=KafkaProcessor.INPUT, condition="headers['type']=='DeliveryCompleted'")
    public void wheneverDeliveryCompleted_NotifyMessage(@Payload DeliveryCompleted deliveryCompleted){

        DeliveryCompleted event = deliveryCompleted;
        System.out.println("\n\n##### listener NotifyMessage : " + deliveryCompleted + "\n\n");


        

        // Sample Logic //
        Notify.notifyMessage(event);
        

    }
    @StreamListener(value=KafkaProcessor.INPUT, condition="headers['type']=='DeliveryStarted'")
    public void wheneverDeliveryStarted_NotifyMessage(@Payload DeliveryStarted deliveryStarted){

        DeliveryStarted event = deliveryStarted;
        System.out.println("\n\n##### listener NotifyMessage : " + deliveryStarted + "\n\n");


        

        // Sample Logic //
        Notify.notifyMessage(event);
        

    }
    @StreamListener(value=KafkaProcessor.INPUT, condition="headers['type']=='PaymentCanceled'")
    public void wheneverPaymentCanceled_NotifyMessage(@Payload PaymentCanceled paymentCanceled){

        PaymentCanceled event = paymentCanceled;
        System.out.println("\n\n##### listener NotifyMessage : " + paymentCanceled + "\n\n");


        

        // Sample Logic //
        Notify.notifyMessage(event);
        

    }
    @StreamListener(value=KafkaProcessor.INPUT, condition="headers['type']=='OrderPlaced'")
    public void wheneverOrderPlaced_NotifyMessage(@Payload OrderPlaced orderPlaced){

        OrderPlaced event = orderPlaced;
        System.out.println("\n\n##### listener NotifyMessage : " + orderPlaced + "\n\n");
       

        // Sample Logic //
        Notify.notifyMessage(event);
        

    }
    @StreamListener(value=KafkaProcessor.INPUT, condition="headers['type']=='DeliveryCanceled'")
    public void wheneverDeliveryCanceled_NotifyMessage(@Payload DeliveryCanceled deliveryCanceled){

        DeliveryCanceled event = deliveryCanceled;
        System.out.println("\n\n##### listener NotifyMessage : " + deliveryCanceled + "\n\n");


        

        // Sample Logic //
        Notify.notifyMessage(event);
        

    }

}


