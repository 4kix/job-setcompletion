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

    @PutMapping
    public ResponseEntity<String> create(@RequestBody TaskResponseModel task)
    {
        TaskResponseModel taskResponse = abstractManager.createTaskModel(task.getBody(), task.getCurrentStatus());
        logger.info("Run task: " + taskResponse.getUUID());
        return new ResponseEntity<String>(taskResponse.getUUID(), HttpStatus.ACCEPTED);
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestHeader String UUID)
    {
        abstractManager.createTaskModel(UUID);
        logger.info("Run task: " + UUID);
        return new ResponseEntity<String>(UUID, HttpStatus.ACCEPTED);
    }

    @GetMapping
    public ResponseEntity<String> getCurrentState(@RequestHeader(value="UUID") String UUID)
    {
        //String state = abstractManager.getTaskState(UUID);
        logger.info("State os task: " + UUID +" : " );
        if (abstractManager.getTaskState(UUID).equals("RUNNING"))
        {
            return new ResponseEntity<>(abstractManager.getTaskState(UUID), HttpStatus.PROCESSING);
        }
        return new ResponseEntity<>(abstractManager.getTaskState(UUID), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteTask(@RequestHeader(value="UUID") String UUID)
    {
        abstractManager.deleteTask(UUID);
        logger.info("Stop task: " + UUID);
        return new ResponseEntity<String>("Deleted", HttpStatus.OK);
    }

}
