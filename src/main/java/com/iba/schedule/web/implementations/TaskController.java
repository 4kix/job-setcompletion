package com.iba.schedule.web.implementations;

import com.iba.schedule.manager.AbstractManager;
import com.iba.schedule.manager.TaskManager;
import com.iba.schedule.model.TaskResponseModel;
import com.iba.schedule.web.abstracts.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/task")
public class TaskController extends AbstractController<TaskResponseModel>{

    public TaskController(@Autowired TaskManager taskManager) {
        super(taskManager);
    }

}
