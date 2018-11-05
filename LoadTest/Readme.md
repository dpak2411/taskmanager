This test is analysis the throughput of the rest service \List

Requirements: 
Jmeter

Steps to run the performance test:
* Follow the steps in https://github.com/Damianofds/taskmanager to get the scheduler running.
* Run the scheduler for a while, so that you have tasks >1000 in the task table.
* Take a sql dump of the task table using pg_dump (pg_dump -U <username> db > db_dump)
* Restore to sql dump
* Start Jmeter from \apache-jmeter-5.0\bin
* Open "Test_List_TaskManager.jmx" in Jmeter
* Set the number of users, ramp up period & loop count in the thread group "To check the throughput for \List"
* Then Run (Ctrl+R) to start the tests.
* Monitor the graph for the throughput.