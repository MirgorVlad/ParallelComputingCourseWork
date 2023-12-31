# Project Name: Client-Server Inverted index application

## Overview
This project is implementation of my course fork for Parallel Computing. I have developed inverted concurrent inverted index data structure with client-server functionality.

## Prerequisites
- Java JDK 11 or higher
- Apache Maven 3.6.3 or higher

## Installation
1. Clone the repository:
    ```bash
    git clone https://github.com/MirgorVlad/ParallelComputingCourseWork.git
    ```

2. Navigate to the project directory:
    ```bash
    cd ParallelComputingCourseWork
    ```

3. Build the project using Maven:
    ```bash
    mvn clean install
    ```

## Usage
1. After building the project, run the application:
    ```bash
    mvn exec:java -Dexec.mainClass="server.Server"
    mvn exec:java -Dexec.mainClass="client.Client"
    ```
    This will start Server and Client instances.

#Application protocol 

Client -> Server: establishing a connection
Server->Client: connection success message
Server->Client: request for command input (UTF-8)
Client->Server: ‘build {path to directory}’ command (build index from
documents in directory)
Server->Client: 1 byte status message (1 – success, 0 - error)
Client->Server: ‘status’ command (return result of execution in %)
Server->Client: Long message (percent status of execution)
Client->Server: search {search string} command (get search string)
Server->Client: UTF data (result of search in inverted index)
Client->Server: time command
Server->Client: Long data (time of index building in ms)
Client->Server: ‘exit’ command (close connection)


## Dependencies
- Lombok 1.18.30

## Contribution
Contributions are welcome! Please fork the repository, create a new branch, and submit a pull request for review.

## Contact Information
For further assistance, contact: mirgorodskiy295@gmail.com
