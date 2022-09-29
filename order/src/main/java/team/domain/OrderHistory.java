package team.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Date;
import lombok.Data;

@Entity
@Table(name="OrderHistory_table")
@Data
public class OrderHistory {

        @Id
        //@GeneratedValue(strategy=GenerationType.AUTO)
        private Long id;
        private Long orderId;
        private Long flowerId;
        private String flowerName;
        private Boolean isOffline;
        private Boolean isDelivered;
        private Boolean isCanceled;
        private Integer qty;
        private Double price;


}