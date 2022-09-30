package team.infra;
import team.domain.*;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityModel;

@Component
public class NotifyHateoasProcessor implements RepresentationModelProcessor<EntityModel<Notify>>  {

    @Override
    public EntityModel<Notify> process(EntityModel<Notify> model) {

        
        return model;
    }
    
}
