package team.domain;

import team.domain.*;
import team.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class OrderPlaced extends AbstractEvent {

    private Long id;
    private Long flowerId;
    private Integer qty;
    private String address;
    private Date orderDate;
    private Boolean isOffline;
    private Date orderCancelDate;
    private String cardNo;
    private String phoneNumber;
    private Double price;

    public OrderPlaced(Order aggregate){
        super(aggregate);
    }
    public OrderPlaced(){
        super();
    }
}
