package team.domain;

import team.domain.PaymentCompleted;
import team.domain.PaymentCanceled;
import team.PaymentApplication;
import javax.persistence.*;
import java.util.List;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name="Payment_table")
@Data

public class Payment  {

    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    
    
    
    
    
    private Long id;
    
    
    
    
    
    private Long flowerId;
    
    
    
    
    
    private Double price;
    
    
    
    
    
    private Date payDate;
    
    
    
    
    
    private String cardNo;
    
    
    
    
    
    private Long orderId;
    
    
    
    
    
    private Integer qty;
    
    
    
    
    
    private String status;
    
    
    
    
    
    private Boolean isOffline;

    @PostPersist
    public void onPostPersist(){


        PaymentCompleted paymentCompleted = new PaymentCompleted(this);
        paymentCompleted.publishAfterCommit();



        PaymentCanceled paymentCanceled = new PaymentCanceled(this);
        paymentCanceled.publishAfterCommit();

    }

    public static PaymentRepository repository(){
        PaymentRepository paymentRepository = PaymentApplication.applicationContext.getBean(PaymentRepository.class);
        return paymentRepository;
    }




    public static void cancelPay(OrderCancelled orderCancelled){

        /** Example 1:  new item 
        Payment payment = new Payment();
        repository().save(payment);

        PaymentCanceled paymentCanceled = new PaymentCanceled(payment);
        paymentCanceled.publishAfterCommit();
        */

        /** Example 2:  finding and process
        
        repository().findById(orderCancelled.get???()).ifPresent(payment->{
            
            payment // do something
            repository().save(payment);

            PaymentCanceled paymentCanceled = new PaymentCanceled(payment);
            paymentCanceled.publishAfterCommit();

         });
        */

        
    }
    public static void pay(OrderPlaced orderPlaced){

        /** Example 1:  new item 
        Payment payment = new Payment();
        repository().save(payment);

        PaymentCompleted paymentCompleted = new PaymentCompleted(payment);
        paymentCompleted.publishAfterCommit();
        */

        /** Example 2:  finding and process
        
        repository().findById(orderPlaced.get???()).ifPresent(payment->{
            
            payment // do something
            repository().save(payment);

            PaymentCompleted paymentCompleted = new PaymentCompleted(payment);
            paymentCompleted.publishAfterCommit();

         });
        */

        
    }


}
