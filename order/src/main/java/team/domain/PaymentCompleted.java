package team.domain;

import team.infra.AbstractEvent;
import lombok.Data;
import java.util.*;

@Data
public class PaymentCompleted extends AbstractEvent {

    private Long id;
    private Long flowerId;
    private Float price;
    private Date payDate;
    private String cardNo;
    private Long orderId;
    private Integer qty;
}
