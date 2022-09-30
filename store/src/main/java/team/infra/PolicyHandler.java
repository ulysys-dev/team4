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
    @Autowired StoreRepository storeRepository;
    
    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}

    @StreamListener(value=KafkaProcessor.INPUT, condition="headers['type']=='PaymentCompleted'")
    public void wheneverPaymentCompleted_IfOnlineOrder(@Payload PaymentCompleted paymentCompleted){

        PaymentCompleted event = paymentCompleted;
        System.out.println("\n\n##### listener IfOnlineOrder : " + paymentCompleted + "\n\n");


        if(!event.getIsOffline()){
            // Sample Logic //
            Store.ifOnlineOrder(event);
        }       

        

    }

    @StreamListener(value=KafkaProcessor.INPUT, condition="headers['type']=='PaymentCompleted'")
    public void wheneverPaymentCompleted_IfOfflineOrder(@Payload PaymentCompleted paymentCompleted){

        PaymentCompleted event = paymentCompleted;
        System.out.println("\n\n##### listener IfOfflineOrder : " + paymentCompleted + "\n\n");


        if(event.getIsOffline()){
            // Sample Logic //
            Store.ifOfflineOrder(event);
        }         

        

    }

}


