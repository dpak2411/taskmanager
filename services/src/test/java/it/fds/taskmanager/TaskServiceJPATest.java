package it.fds.taskmanager;

import java.util.Calendar;
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
    Calendar date= Calendar.getInstance();
    
    @Test
    @DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
    public void writeAndReadOnDB() {
        TaskDTO t = new TaskDTO();       
        t.setTitle("Test task1");
        t.setStatus(TaskState.NEW.toString().toUpperCase());
        t.setDescription("this is a test message");
        t.setPriority("3");
        t.setCreatedat(date);
        t.setUpdatedat(date);
        t.setPostponedat(date);
        t.setPostponedtime(null);
        TaskDTO t1 = taskService.saveTask(t);
        TaskDTO tOut = taskService.findOne(t1.getUuid());
        assertEquals("Test task1", tOut.getTitle());
        assertEquals("this is a test message",tOut.getDescription());
        assertEquals("3",tOut.getPriority());
        assertEquals(date,tOut.getCreatedat());
        assertEquals(date,tOut.getUpdatedat());
        assertEquals(date,tOut.getPostponedat());
        assertEquals(null,tOut.getPostponedtime());
        List<TaskDTO> list = taskService.showList();
        assertEquals(1, list.size());
    }
 
    /**
     * to test show list when a task has been set to postponed.
     */
    @Test
    @DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
    public void testshowlist()
    {
    	List<Task> taskslist = tasksRepo.findAllExcludePostponed();
    	assertEquals(0,taskslist.size());
    	TaskDTO t = new TaskDTO();
    	t.setTitle("Test task3");
        t.setStatus(TaskState.NEW.toString().toUpperCase());
        taskService.saveTask(t);
        List<Task> taskslist1 = tasksRepo.findAllExcludePostponed();
        assertEquals(1,taskslist1.size());
        t.setStatus(TaskState.POSTPONED.toString().toUpperCase());
        TaskDTO t2 = taskService.saveTask(t);
        assertEquals(t2.getStatus(),"POSTPONED");      
        List<TaskDTO> list = taskService.showList();
        assertEquals(0, list.size());
    }
    
    
    /**
     * to test show list when a task has been set to new.
     */
    @Test
    @DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
    public void testshowlistnew()
    {
    	List<Task> taskslist = tasksRepo.findAllExcludePostponed();
    	assertEquals(0,taskslist.size());
    	TaskDTO t = new TaskDTO();
    	t.setTitle("Test task3");
        t.setStatus(TaskState.NEW.toString().toUpperCase());
        taskService.saveTask(t);
        List<TaskDTO> list = taskService.showList();
        assertEquals(1, list.size());
    }
    
    
    
    /**
     * Test to find a task which has been saved
     */
    @Test
    @DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
    public void savetask() {
        TaskDTO t = new TaskDTO();
        t.setTitle("Test task2");
        t.setStatus(TaskState.NEW.toString().toUpperCase());
        TaskDTO t1 = taskService.saveTask(t);
        TaskDTO tOut = taskService.findOne(t1.getUuid());
        assertEquals("Test task2", tOut.getTitle()); 
        assertEquals("NEW",tOut.getStatus());
    }
    
    
    /**
     * test to postpone a task
     * @author admin
     *
     */
    @Test
    @DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
    public void postponetask() {
        TaskDTO t = new TaskDTO();
        t.setTitle("Test task3");
        t.setStatus(TaskState.NEW.toString().toUpperCase());
        TaskDTO t1 = taskService.saveTask(t);
        assertTrue(taskService.postponeTask(t1.getUuid(), 2));        
        TaskDTO tOut = taskService.findOne(t1.getUuid());
        assertEquals("POSTPONED",tOut.getStatus());
        List<TaskDTO> list = taskService.showList();
        List<Task> taskslist = tasksRepo.findAllExcludePostponed();
        assertEquals(0,taskslist.size());
        assertEquals(0, list.size());            
    }
    
    /**
     * To test postpone with priority 3
     */
    @Test
    @DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
    public void postponetaskwithpriority() {
    	TaskDTO t = new TaskDTO();
        t.setTitle("Test task3");
        t.setStatus(TaskState.NEW.toString().toUpperCase());
        t.setPriority("3");
        TaskDTO t1 = taskService.saveTask(t);
        taskService.postponeTask(t1.getUuid(), 2);
        List<TaskDTO> list = taskService.showList();
        assertEquals(0, list.size());
    
    }
    
    /**
     * To test if the task has been updated.
     * @author admin
     *
     */
    @Test
    @DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
    public void testupdate()
    {
    	TaskDTO t = new TaskDTO();
        t.setTitle("Test task3");
        t.setStatus(TaskState.NEW.toString().toUpperCase());
        TaskDTO t1 = taskService.saveTask(t);
        assertEquals(null,t1.getDescription());
        t1.setDescription("This is a test message");
        t1.setPriority("1");
        t1.setCreatedat(date);
        t1.setUpdatedat(date);
        t1.setPostponedat(date);
        t1.setPostponedtime(null);     
        TaskDTO t2=taskService.updateTask(t1);
        assertEquals("This is a test message",t2.getDescription());  
        assertEquals("1",t1.getPriority());
        assertEquals(date,t1.getCreatedat());
        assertEquals(date,t1.getUpdatedat());
        assertEquals(date,t1.getPostponedat());
        assertEquals(null,t1.getPostponedtime());
        List<TaskDTO> list = taskService.showList();
        assertEquals(1, list.size());
    }
    
    /**
     * To test if a task has been restored 
     * @author admin
     *
     */
    @Test
    @DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
    public void testrestore()
    {
    	TaskDTO t = new TaskDTO();
        t.setTitle("Test task3");
        t.setStatus(TaskState.NEW.toString().toUpperCase());
        TaskDTO t1 = taskService.saveTask(t);
        boolean t2= taskService.postponeTask(t1.getUuid(), 2);
        List<Task> list = tasksRepo.findTaskToRestore();
        assertEquals(0,list.size());
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, -2);
		t1.setPostponedat(c);
		List<Task> list1 = tasksRepo.findTaskToRestore();
	    assertEquals(0,list1.size()); 
	    t1.setStatus(TaskState.POSTPONED.toString().toUpperCase());
	    taskService.updateTask(t1);
	    List<Task> list2 = tasksRepo.findTaskToRestore();
	    assertEquals(1, list2.size()); 
	    t1.setStatus(TaskState.RESTORED.toString().toUpperCase());
	    taskService.updateTask(t1);
	    List<Task> list3 = tasksRepo.findTaskToRestore();
	    assertEquals(0, list3.size());	    
	    t1.setStatus(TaskState.POSTPONED.toString().toUpperCase());
	    taskService.updateTask(t1);
	    taskService.unmarkPostoned();
	    List<Task> list4 = tasksRepo.findTaskToRestore();
	    assertEquals(0, list4.size());
    }
    
    
    /**
     * To test if the task can be resolved
     * @author admin
     *
     */
    @Test
    @DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
    public void testresolved()
    {
    	TaskDTO t = new TaskDTO();
        t.setTitle("Test task3");
        t.setStatus(TaskState.NEW.toString().toUpperCase());
        TaskDTO t1 = taskService.saveTask(t);
        assertTrue(taskService.resolveTask(t1.getUuid()));
        TaskDTO tOut = taskService.findOne(t1.getUuid());
        assertNotNull(tOut.getResolvedat());        
    }
    
    
    @EnableJpaRepositories
    @Configuration
    @SpringBootApplication
    public static class EndpointsMain{}
}
