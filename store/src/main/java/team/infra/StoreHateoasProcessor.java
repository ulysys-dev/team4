package team.infra;
import team.domain.*;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityModel;

@Component
public class StoreHateoasProcessor implements RepresentationModelProcessor<EntityModel<Store>>  {

    @Override
    public EntityModel<Store> process(EntityModel<Store> model) {

        
        return model;
    }
    
}
