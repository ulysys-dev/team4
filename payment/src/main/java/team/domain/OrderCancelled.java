package team.domain;

import team.domain.*;
import team.infra.AbstractEvent;
import lombok.*;
import java.util.*;
@Data
@ToString
public class OrderCancelled extends AbstractEvent {

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
}


