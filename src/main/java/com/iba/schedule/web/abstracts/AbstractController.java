package com.iba.schedule.web.abstracts;


import com.iba.schedule.manager.AbstractManager;
import com.iba.schedule.model.BaseModel;
import com.iba.schedule.model.TaskResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public abstract class AbstractController<T extends BaseModel> {

    private AbstractManager<T> abstractManager;

    public AbstractController() {}

    public AbstractController(AbstractManager<T> abstractManager) {this.abstractManager = abstractManager ;}

    public abstract ResponseEntity<String> create(@RequestBody T task);

    public abstract ResponseEntity<String> getCurrentState(@PathVariable String id);

    public abstract ResponseEntity<String> deleteTask(@PathVariable String id);

}
