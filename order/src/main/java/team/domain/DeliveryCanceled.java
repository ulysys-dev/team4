package team.domain;

import team.infra.AbstractEvent;
import lombok.Data;
import java.util.*;

@Data
public class DeliveryCanceled extends AbstractEvent {

    private Long id;
    private Long orderId;
    private Date deliveryCancelDate;
import team.domain.*;
import lombok.*;
@ToString
}
