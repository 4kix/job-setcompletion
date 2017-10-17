package com.iba.schedule.web.abstracts;
import com.iba.schedule.manager.AbstractManager;
import com.iba.schedule.model.BaseModel;
import com.iba.schedule.model.TaskResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


public class AbstractController<T extends TaskResponseModel> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractController.class);

    private AbstractManager<T> abstractManager;

    public AbstractController(AbstractManager<T> abstractManager) {this.abstractManager = abstractManager ;}

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<String> create(@RequestBody T task)
    {
        T taskResponse = abstractManager.createTaskModel(task.getBody(), task.getCurrentStatus());
        logger.info("Created model: " + taskResponse.getId());
        return new ResponseEntity<String>(taskResponse.getId(), HttpStatus.CREATED);
    }

    @RequestMapping(value="/{id}", method = RequestMethod.POST)
    public ResponseEntity startTask(@PathVariable String id)
    {
        abstractManager.runTask(id);
        logger.info("Run task: " + id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getCurrentState(@PathVariable String id)
    {
        String state = abstractManager.getTaskState(id);
        logger.info("State os task: " + id +" : " + state);
        return new ResponseEntity<String>(state, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteTask(@PathVariable String id)
    {
        abstractManager.deleteTask(id);
        logger.info("Stop task: " + id);
        return new ResponseEntity<String>("Deleted", HttpStatus.OK);
    }

}
