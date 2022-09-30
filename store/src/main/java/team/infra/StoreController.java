package team.infra;
import team.domain.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@RestController
// @RequestMapping(value="/stores")
@Transactional
public class StoreController {
    @Autowired
    StoreRepository storeRepository;



    @RequestMapping(value = "stores/{id}/wrap",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8")
    public Store wrap(@PathVariable(value = "id") Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
            System.out.println("##### /store/wrap  called #####");
            Optional<Store> optionalStore = storeRepository.findById(id);
            
            optionalStore.orElseThrow(()-> new Exception("No Entity Found"));
            Store store = optionalStore.get();
            store.wrap();
            
            storeRepository.save(store);
            return store;
            
    }
    



}
