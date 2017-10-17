package com.iba.schedule.web.implementations;

import com.iba.schedule.manager.AbstractManager;
import com.iba.schedule.manager.TaskManager;
import com.iba.schedule.model.TaskResponseModel;
import com.iba.schedule.web.abstracts.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task")
public class TaskController extends AbstractController<TaskResponseModel>{

    private AbstractManager<TaskResponseModel> abstractManager;

    public TaskController(AbstractManager<TaskResponseModel> abstractManager) {this.abstractManager = abstractManager ;}

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<String> create(@RequestBody TaskResponseModel task)
    {
        TaskResponseModel taskResponse = abstractManager.createTaskModel(task.getBody(), task.getCurrentStatus());
        return new ResponseEntity<String>(taskResponse.getId(), HttpStatus.CREATED);
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getCurrentState(@PathVariable String id)
    {
        String state = abstractManager.getTaskState(id);
        return new ResponseEntity<String>(state, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteTask(@PathVariable String id)
    {
        abstractManager.deleteTask(id);
        return new ResponseEntity<String>("Deleted", HttpStatus.OK);
    }

    @RequestMapping(value = "/threadsJVM", method = RequestMethod.GET)
    public void getAllJVMThreads() {
        abstractManager.getJVMThreads();
    }
}
