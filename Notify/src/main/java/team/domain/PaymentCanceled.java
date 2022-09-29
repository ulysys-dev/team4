package team.domain;

import team.domain.*;
import team.infra.AbstractEvent;
import lombok.*;
import java.util.*;
@Data
@ToString
public class PaymentCanceled extends AbstractEvent {

    private Long id;
    private Long flowerId;
    private Float price;
    private Date payDate;
    private String cardNo;
    private Long orderId;
    private Integer qty;
}


