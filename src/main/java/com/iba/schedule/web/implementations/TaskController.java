package com.iba.schedule.web.implementations;

import com.iba.schedule.manager.AbstractManager;
import com.iba.schedule.manager.TaskManager;
import com.iba.schedule.model.TaskResponseModel;
import com.iba.schedule.web.abstracts.AbstractController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/task")
public class TaskController extends AbstractController<TaskResponseModel>{

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private AbstractManager<TaskResponseModel> abstractManager;

    public TaskController(AbstractManager<TaskResponseModel> abstractManager) {this.abstractManager = abstractManager ;}

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<String> create(@RequestBody TaskResponseModel task)
    {
        TaskResponseModel taskResponse = abstractManager.createTaskModel(task.getBody(), task.getCurrentStatus());
        logger.info("Run task: " + taskResponse.getId());
        return new ResponseEntity<String>(taskResponse.getId(), HttpStatus.CREATED);
    }

    @RequestMapping(value="", method = RequestMethod.GET)
    public ResponseEntity<String> getCurrentState(@RequestHeader(value="Id") String id)
    {
        String state = abstractManager.getTaskState(id);
        logger.info("State os task: " + id +" : " + state);
        return new ResponseEntity<String>(state, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteTask(@RequestHeader(value="Id") String id)
    {
        abstractManager.deleteTask(id);
        logger.info("Stop task: " + id);
        return new ResponseEntity<String>("Deleted", HttpStatus.OK);
    }

}
