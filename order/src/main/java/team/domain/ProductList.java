package team.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Date;
import lombok.Data;

@Entity
@Table(name="ProductList_table")
@Data
public class ProductList {

        @Id
        //@GeneratedValue(strategy=GenerationType.AUTO)
        private Long id;


}