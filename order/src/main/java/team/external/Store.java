package team.external;

import lombok.Data;
import java.util.Date;
@Data
public class Store {

    private Long id;
    private Long flowerId;
    private String flowerName;
    private Integer flowerCnt;
    private Boolean packingYn;
    private String isOffline;
    private Long orderId;
    private Double flowerPrice;
}


