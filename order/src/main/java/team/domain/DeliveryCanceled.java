package team.domain;

import team.infra.AbstractEvent;
import lombok.Data;
import java.util.*;
import team.domain.*;
import lombok.*;

@Data
@ToString
public class DeliveryCanceled extends AbstractEvent {

    private Long id;
    private Long orderId;
    private Date deliveryCancelDate;

}
