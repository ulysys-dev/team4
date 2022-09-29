package team.domain;

import team.domain.*;
import team.infra.AbstractEvent;
import lombok.*;
import java.util.*;
@Data
@ToString
public class FlowerWrapped extends AbstractEvent {

    private Long id;
    private Long flowerId;
    private String flowerName;
    private Integer flowerCnt;
    private Long orderId;
}


