# Java Client/Server programs

I have decided to use sockets because it allowed me to create a communication between applications in both ways.</br>
The client sends messages to the server and the server replies with the file name, so the client understands that the data transmission was successful, and it can delete the file.

#### SERVER program

#### How to run a SERVER program:
Go to the _client_server/src/main/java_ folder and run two commands in a terminal:</br></br>
`javac denissmolov/server/*.java`</br>
`java denissmolov.server.ServerService /Users/denissmolov/IdeaProjects/client_server_files/server_config`<br></br>
The argument is the path to the config file. You can find the example of this file in the _files_ folder.</br>
Please note that one instance of the server program is capable of handling messages sent by multiple clients simultaneously (we have a ThreadPool with a capacity of 4 threads).

![alt text](https://github.com/Denis-Smolov/client_server/blob/master/src/main/resources/screenshot.png?raw=true)

#### How to run a CLIENT program:
`javac denissmolov/client/*.java`</br>
`java denissmolov.client.ClientService /Users/denissmolov/IdeaProjects/client_server_files/client_config`<br></br>
The argument is the path to the config file. You can find the example of this file in the _files_ folder.
I have included four config files, so you can run four clients almost at the same time.
Please note that in my application only one client program monitors only one folder. It is not intended to run several clients on one folder.<br>
The client program runs as a scheduled single thread application that constantly monitors the directory.
