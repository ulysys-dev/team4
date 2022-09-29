package team.domain;

import team.infra.AbstractEvent;
import lombok.Data;
import java.util.*;

@Data
public class DeliveryCompleted extends AbstractEvent {

    private Long id;
    private Long orderId;
    private Date deliveryCompleteDate;
import team.domain.*;
import lombok.*;
@ToString
}
