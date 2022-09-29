package team.domain;

import team.domain.*;
import team.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class FlowerSold extends AbstractEvent {

    private Long id;
    private Long flowerId;
    private String flowerName;
    private Integer flowerCnt;
    private Long orderId;

    public FlowerSold(Store aggregate){
        super(aggregate);
    }
    public FlowerSold(){
        super();
    }
}
