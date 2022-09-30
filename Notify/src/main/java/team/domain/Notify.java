package team.domain;

import team.NotifyApplication;
import javax.persistence.*;
import java.util.List;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name="Notify_table")
@Data

public class Notify  {

    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    
    
    
    
    
    private Long id;
    
    
    
    
    
    private String flowerName;
    
    
    
    
    
    private String phoneNumber;
    
    
    
    
    
    private String message;
    
    
    
    
    
    private Integer flowerCnt;


    public static NotifyRepository repository(){
        NotifyRepository notifyRepository = NotifyApplication.applicationContext.getBean(NotifyRepository.class);
        return notifyRepository;
    }




    public static void notifyMessage(PaymentCompleted paymentCompleted){

        /** Example 1:  new item 
        Notify notify = new Notify();
        repository().save(notify);

        */

        /** Example 2:  finding and process
        
        repository().findById(paymentCompleted.get???()).ifPresent(notify->{
            
            notify // do something
            repository().save(notify);


         });
        */

        
    }
    public static void notifyMessage(DeliveryCompleted deliveryCompleted){

        /** Example 1:  new item 
        Notify notify = new Notify();
        repository().save(notify);

        */

        /** Example 2:  finding and process
        
        repository().findById(deliveryCompleted.get???()).ifPresent(notify->{
            
            notify // do something
            repository().save(notify);


         });
        */

        
    }
    public static void notifyMessage(DeliveryStarted deliveryStarted){

        /** Example 1:  new item 
        Notify notify = new Notify();
        repository().save(notify);

        */

        /** Example 2:  finding and process
        
        repository().findById(deliveryStarted.get???()).ifPresent(notify->{
            
            notify // do something
            repository().save(notify);


         });
        */

        
    }
    public static void notifyMessage(PaymentCanceled paymentCanceled){

        /** Example 1:  new item 
        Notify notify = new Notify();
        repository().save(notify);

        */

        /** Example 2:  finding and process
        
        repository().findById(paymentCanceled.get???()).ifPresent(notify->{
            
            notify // do something
            repository().save(notify);


         });
        */

        
    }
    public static void notifyMessage(OrderPlaced orderPlaced){

        /** Example 1:  new item 
        Notify notify = new Notify();
        repository().save(notify);

        */

        /** Example 2:  finding and process
        
        repository().findById(orderPlaced.get???()).ifPresent(notify->{
            
            notify // do something
            repository().save(notify);


         });
        */

        
    }
    public static void notifyMessage(DeliveryCanceled deliveryCanceled){

        /** Example 1:  new item 
        Notify notify = new Notify();
        repository().save(notify);

        */

        /** Example 2:  finding and process
        
        repository().findById(deliveryCanceled.get???()).ifPresent(notify->{
            
            notify // do something
            repository().save(notify);


         });
        */

        
    }


}
