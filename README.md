# project1
project1 of CS542
This is the branch edited by Susan

CS 542 Team Description


Team Organization and members
Yan Wang
Zishan Qin

Project Decision
Deep-Programming option

Projects Description
1	A values store (a simplified version of a key-value store). The value store has just three interfaces:
    void Put(int key, byte[] data); stores data under the given key,
    byte[] Get(int key); retrieves the data and
    void Remove(int key); deletes the key.
The value byte array can be arbitrarily large, up to 1 GB.
2	An indexing mechanism. Our index has just three interfaces:
    void Put(string key, Number data_value); or void Put(string key, string data_value);  adds the index entry.
    string Get(Number data_value); or string Get(string data_value); retrieves the key given the index and
    void Remove(string key); deletes the index.
3	A query execution engine. You may have realized that the functions of exercise 1 are actually methods of a Relation class. In a real database, we will have multiple instances of Relation, each representing one table. Modify your answer from exercise 1 to create the class and also add open(), getNext() and close() methods to it. Use the class to create and populate city and country tables (schema and data to be provided during January). Use the class to find all cities whose population is more than 40% of the population of their entire country.
4	Undo/Redo Logging. Over time, the populations of cities and countries changes. We will change the population of each by 2% to represent the passage of a year. The purpose of this programming assignment is to programmatically make this change in all records and to generate undo/redo logs as we are doing it. Then we will move those logs to another machine that has a copy of the same data and apply the logs and observe that the data has changed.



