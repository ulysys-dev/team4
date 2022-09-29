package team.domain;

import team.domain.*;
import team.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class PaymentCompleted extends AbstractEvent {

    private Long id;
    private Long flowerId;
    private Float price;
    private Date payDate;
    private String cardNo;
    private Long orderId;
    private Integer qty;

    public PaymentCompleted(Payment aggregate){
        super(aggregate);
    }
    public PaymentCompleted(){
        super();
    }
}
