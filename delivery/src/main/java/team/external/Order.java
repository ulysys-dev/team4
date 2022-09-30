package team.external;

import lombok.Data;
import java.util.Date;
@Data
public class Order {

    private Long id;
    private Long flowerId;
    private Integer qty;
    private String address;
    private Date orderDate;
    private Boolean isOffline;
    private Date orderCancelDate;
    private String phoneNumber;
    private Double price;
}


