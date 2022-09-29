package team.domain;

import java.util.*;
import lombok.*;
import team.domain.*;
import team.infra.AbstractEvent;

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
}
