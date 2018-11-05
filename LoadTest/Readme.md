## This test is analysis the throughput of the rest service \List

# Requirements: 
Jmeter

# Steps to run the performance test:
* Follow the steps in https://github.com/Damianofds/taskmanager to get the scheduler running.
* Run the scheduler for a while, so that you have tasks >1000 in the task table.
* Take a sql dump of the task table using pg_dump (pg_dump -U <username> db > db_dump)
* Restore to sql dump
* Start Jmeter from \apache-jmeter-5.0\bin
* Open "Test_List_TaskManager.jmx" in Jmeter
* Set the number of users, ramp up period & loop count in the thread group "To check the throughput for \List"
* Then Run (Ctrl+R) to start the tests.
* Monitor the graph for the throughput.

Below is a sample analysis of the load tests.

 | Users | Ramp-UP | Loop | Throughput    |
 | ----- | ------- | ---- | ------------- |
 | 100   |   0     | 10   | 331.996/minute| 
 | 100   |  100    | 10   | 316.436/minute|
 | 100   |  1000   | 10   | 60.408/minute |
 
 Attached screenshot ![/taskmanager/LoadTest/Screenshots/100T_0R_10L.jpg]
 
The following statistics, represented in colors:
  * Black: The total number of current samples sent.
  * Blue: The current average of all samples sent.
  * Red: The current standard deviation.
  * Green: Throughput rate that represents the number of requests per minute the server handled


To analyze the performance of the service under test, these two parameters are more essential
 * Throughput
 * Deviation

The Throughput is the most important parameter. It represents the ability of the server to handle a heavy load.  
The higher the Throughput is, the better is the server performance.
