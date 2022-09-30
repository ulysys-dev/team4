package team.domain;

import team.domain.DeliveryStarted;
import team.external.Order;
import team.external.OrderService;
import team.domain.DeliveryCompleted;
import team.domain.DeliveryCanceled;
import team.DeliveryApplication;
import javax.persistence.*;

import org.springframework.beans.BeanUtils;

import java.util.List;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name="Delivery_table")
@Data

public class Delivery  {

    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    
    private Long id;
    
    private Long orderId;
    
    private Date deliveryStartDate;
    
    private Date deliveryCompleteDate;
    
    private Date deliveryCancelDate;
  
    private String address;
    
    private String status;

    @PostPersist
    public void onPostPersist(){


        DeliveryStarted deliveryStarted = new DeliveryStarted(this);
        deliveryStarted.publishAfterCommit();

        // DeliveryCompleted deliveryCompleted = new DeliveryCompleted(this);
        // deliveryCompleted.publishAfterCommit();

        // DeliveryCanceled deliveryCanceled = new DeliveryCanceled(this);
        // deliveryCanceled.publishAfterCommit();

    }

    @PreRemove      // 주문 취소  
    public void onPreRemove(){
        DeliveryCanceled deliveryCanceled = new DeliveryCanceled();
        BeanUtils.copyProperties(this, deliveryCanceled);
        deliveryCanceled.publishAfterCommit();        // 카프카에 pub 
    }


    public static DeliveryRepository repository(){
        DeliveryRepository deliveryRepository = DeliveryApplication.applicationContext.getBean(DeliveryRepository.class);
        return deliveryRepository;
    }


    public static void notifyOrder(FlowerWrapped flowerWrapped, OrderService orderService){

        /** Example 1:  new item         */

        Delivery delivery = new Delivery();
        delivery.setOrderId(Long.valueOf(flowerWrapped.getOrderId()));

        Order order = orderService.getOrder(Long.valueOf(flowerWrapped.getOrderId()));  // REST로 address 호출
        delivery.setAddress(order.getAddress());

        repository().save(delivery);
     
        /** Example 2:  finding and process
        
        repository().findById(flowerWrapped.get???()).ifPresent(delivery->{
            
            delivery // do something
            repository().save(delivery);


         });
        */

    }



    public static void deliveryCancel(PaymentCanceled paymentCanceled){

        /** Example 1:  new item 
        Delivery delivery = new Delivery();
        repository().save(delivery);

        DeliveryCanceled deliveryCanceled = new DeliveryCanceled(delivery);
        deliveryCanceled.publishAfterCommit();
        */

        /** Example 2:  finding and process         */
        
        // repository().findByOrderId(paymentCanceled.getOrderId()).ifPresent(delivery->{
            
        //     delivery // do something
        //     repository().save(delivery);

        //     DeliveryCanceled deliveryCanceled = new DeliveryCanceled(delivery);
        //     deliveryCanceled.publishAfterCommit();

        //  });


        
    }


}
