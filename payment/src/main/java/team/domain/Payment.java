package team.domain;

import team.domain.PaymentCompleted;
import team.domain.PaymentCanceled;
import team.PaymentApplication;
import javax.persistence.*;
import java.util.List;
import java.util.Optional;

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
    
    private Boolean isOffline;

    private String status;

    @PostPersist
    public void onPostPersist(){

        /* 
        PaymentCompleted paymentCompleted = new PaymentCompleted(this);
        paymentCompleted.publishAfterCommit();

        PaymentCanceled paymentCanceled = new PaymentCanceled(this);
        paymentCanceled.publishAfterCommit();
        */
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


        /** Example 2:  finding and process
        
        repository().findById(orderCancelled.get???()).ifPresent(payment->{
            
            payment // do something
            repository().save(payment);

            PaymentCanceled paymentCanceled = new PaymentCanceled(payment);
            paymentCanceled.publishAfterCommit();

        });
        
                // Payment = Payment.findByOrderId(orderCancelled.getId());
        
               
        
                // PaymentCanceled paymentCanceled = new PaymentCanceled();        
                // paymentCanceled = paymentCanceled.findByOrderId(orderCancelled.getId());
                // paymentCanceled.setFlowerId(orderCancelled.getFlowerId());
                // paymentCanceled.setPrice(orderCancelled.getPrice());
                // paymentCanceled.setPayDate(orderCancelled.getOrderDate());
                // paymentCanceled.setCardNo(null);
                // paymentCanceled.setOrderId(orderCancelled.getId());
                // paymentCanceled.setQty(orderCancelled.getQty());
                // paymentCanceled.setStatus("CANCELED");
        
                // repository().save(paymentCanceled, paymentCanceled.getId());
        */
            
     
        // Payment payment = repository().findByOrderId(orderCancelled.getId());
        // payment.setStatus("CANCELED");
        
        // PaymentCanceled paymentCanceled = new PaymentCanceled(payment);
        // paymentCanceled.publishAfterCommit();


        repository().findById(orderCancelled.getId()).ifPresent(payment->{
            
            payment.setStatus("CANCELED");
            repository().save(payment);

            PaymentCanceled paymentCanceled = new PaymentCanceled(payment);
            paymentCanceled.publishAfterCommit();

        });
    
    }       

    public static void pay(OrderPlaced orderPlaced){

        /** Example 1:  new item */
        Payment payment = new Payment();
        //repository().save(payment);
               
        
        //Payment payment = new Payment();
        payment.setFlowerId(orderPlaced.getFlowerId());
        payment.setPrice(orderPlaced.getPrice());
        payment.setPayDate(orderPlaced.getOrderDate());
        payment.setCardNo(null);
        payment.setOrderId(orderPlaced.getId());
        payment.setQty(orderPlaced.getQty());
        payment.setStatus("PAYED");
        payment.setIsOffline(orderPlaced.getIsOffline());
        
        repository().save(payment);

        PaymentCompleted paymentCompleted = new PaymentCompleted(payment);
        paymentCompleted.publishAfterCommit();

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
