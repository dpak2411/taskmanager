package it.fds.taskmanager;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.fds.taskmanager.dto.TaskDTO;
import it.fds.taskmanager.model.Task;
import it.fds.taskmanager.repository.TasksRepository;
/**
 * Basic test suite to test the service layer, it uses an in-memory H2 database. 
 * 
 * TODO Add more and meaningful tests! :)
 *
 * @author <a href="mailto:damiano@searchink.com">Damiano Giampaoli</a>
 * @since 10 Jan. 2018
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class TaskServiceJPATest extends Assert{

    @Autowired
    TaskService taskService;
    @Autowired
    private TasksRepository tasksRepo;
    
    //
    
    @Test
    @DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
    public void writeAndReadOnDB() {
        TaskDTO t = new TaskDTO();
        t.setTitle("Test task1");
        t.setStatus(TaskState.NEW.toString().toUpperCase());
        TaskDTO t1 = taskService.saveTask(t);
        TaskDTO tOut = taskService.findOne(t1.getUuid());
        assertEquals("Test task1", tOut.getTitle());
        List<TaskDTO> list = taskService.showList();
        assertEquals(1, list.size());
    }
    
    /**
     * Test to find a task which has been saved
     */
    @Test
    @DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
    public void findtask() {
        TaskDTO t = new TaskDTO();
        t.setTitle("Test task1");
        t.setStatus(TaskState.NEW.toString().toUpperCase());
        TaskDTO t1 = taskService.saveTask(t);
        TaskDTO tOut = taskService.findOne(t1.getUuid());
        assertEquals("Test task1", tOut.getTitle());    
        
    }
    
    
   
    
    /**
     *To test if a status of the postponed task is set to POSTPONED 
     * Bug in the method, the status remains as NEW , not set to postponed.
     */
    @Test
    @DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
    public void PostponeTask() {
        TaskDTO t = new TaskDTO();
        t.setTitle("Test task2");
        t.setStatus(TaskState.NEW.toString().toUpperCase());
        TaskDTO t1 = taskService.saveTask(t);      
        Boolean t2=taskService.postponeTask(t1.getUuid(), 10);
        assertEquals(t2,true);        
    }
    
    /**
     * To check if the task is postponed
     * @author admin
     *
     */
    @Test
    @DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
    public void ListPostoneTasks() {
        TaskDTO t = new TaskDTO();       
        t.setTitle("Test task3");
        t.setStatus(TaskState.NEW.toString().toUpperCase());
        TaskDTO t1 = taskService.saveTask(t);
        taskService.postponeTask(t1.getUuid(), 10);
        List<Task> list = tasksRepo.findTaskToRestore(); 
        assertEquals(0, list.size());
        t.setTitle("Test task4");
        t.setStatus(TaskState.NEW.toString().toUpperCase());
        TaskDTO t2 = taskService.saveTask(t);
        List<Task>  tnotpostponed= tasksRepo.findAllExcludePostponed();
        assertEquals(1, tnotpostponed.size());             
    }
    

    @EnableJpaRepositories
    @Configuration
    @SpringBootApplication
    public static class EndpointsMain{}
}
