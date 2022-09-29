package team.infra;
import team.domain.*;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityModel;

@Component
public class PayMgmtHateoasProcessor implements RepresentationModelProcessor<EntityModel<PayMgmt>>  {

    @Override
    public EntityModel<PayMgmt> process(EntityModel<PayMgmt> model) {

        
        return model;
    }
    
}
