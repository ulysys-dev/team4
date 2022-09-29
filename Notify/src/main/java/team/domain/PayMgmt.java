package team.domain;

import team.NotifyApplication;
import javax.persistence.*;
import java.util.List;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name="PayMgmt_table")
@Data

public class PayMgmt  {

    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    
    
    
    
    
    private Long id;
    
    
    
    
    
    private String flowerName;
    
    
    
    
    
    private String phoneNumber;
    
    
    
    
    
    private String message;
    
    
    
    
    
    private Integer flowerCnt;


    public static PayMgmtRepository repository(){
        PayMgmtRepository payMgmtRepository = NotifyApplication.applicationContext.getBean(PayMgmtRepository.class);
        return payMgmtRepository;
    }




    public static void notifyMessage(PaymentCompleted paymentCompleted){

        /** Example 1:  new item 
        PayMgmt payMgmt = new PayMgmt();
        repository().save(payMgmt);

        */

        /** Example 2:  finding and process
        
        repository().findById(paymentCompleted.get???()).ifPresent(payMgmt->{
            
            payMgmt // do something
            repository().save(payMgmt);


         });
        */

        
    }
    public static void notifyMessage(DeliveryCompleted deliveryCompleted){

        /** Example 1:  new item 
        PayMgmt payMgmt = new PayMgmt();
        repository().save(payMgmt);

        */

        /** Example 2:  finding and process
        
        repository().findById(deliveryCompleted.get???()).ifPresent(payMgmt->{
            
            payMgmt // do something
            repository().save(payMgmt);


         });
        */

        
    }
    public static void notifyMessage(DeliveryStarted deliveryStarted){

        /** Example 1:  new item 
        PayMgmt payMgmt = new PayMgmt();
        repository().save(payMgmt);

        */

        /** Example 2:  finding and process
        
        repository().findById(deliveryStarted.get???()).ifPresent(payMgmt->{
            
            payMgmt // do something
            repository().save(payMgmt);


         });
        */

        
    }
    public static void notifyMessage(PaymentCanceled paymentCanceled){

        /** Example 1:  new item 
        PayMgmt payMgmt = new PayMgmt();
        repository().save(payMgmt);

        */

        /** Example 2:  finding and process
        
        repository().findById(paymentCanceled.get???()).ifPresent(payMgmt->{
            
            payMgmt // do something
            repository().save(payMgmt);


         });
        */

        
    }
    public static void notifyMessage(OrderPlaced orderPlaced){

        /** Example 1:  new item 
        PayMgmt payMgmt = new PayMgmt();
        repository().save(payMgmt);

        */

        /** Example 2:  finding and process
        
        repository().findById(orderPlaced.get???()).ifPresent(payMgmt->{
            
            payMgmt // do something
            repository().save(payMgmt);


         });
        */

        
    }
    public static void notifyMessage(DeliveryCanceled deliveryCanceled){

        /** Example 1:  new item 
        PayMgmt payMgmt = new PayMgmt();
        repository().save(payMgmt);

        */

        /** Example 2:  finding and process
        
        repository().findById(deliveryCanceled.get???()).ifPresent(payMgmt->{
            
            payMgmt // do something
            repository().save(payMgmt);


         });
        */

        
    }


}
