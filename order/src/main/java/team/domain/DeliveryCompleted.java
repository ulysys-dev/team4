package team.domain;

import team.infra.AbstractEvent;
import lombok.Data;
import java.util.*;
import lombok.ToString;



@Data
@ToString
public class DeliveryCompleted extends AbstractEvent {

    private Long id;
    private Long orderId;
    private Date deliveryCompleteDate;


}
