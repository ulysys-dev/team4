package team.domain;

import team.domain.*;
import java.util.Optional;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="stores", path="stores")
public interface StoreRepository extends PagingAndSortingRepository<Store, Long>{


    Optional<Store> findByFlowerId(Long flowerId); 
}
