package com.iba.schedule.web.abstracts;
import com.iba.schedule.manager.AbstractManager;
import com.iba.schedule.model.BaseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


public abstract class AbstractController<T extends BaseModel> {

    private AbstractManager<T> abstractManager;

    public AbstractController(AbstractManager<T> abstractManager) {this.abstractManager = abstractManager;}

    public abstract ResponseEntity<String> create(@RequestHeader String UUID);

    public abstract ResponseEntity<String> getCurrentState(@RequestHeader String UUID);

    public abstract ResponseEntity<Void> stopTask(@RequestHeader String UUID);


}
