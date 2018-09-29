# spring-aspectj-sockets

## Intro

_Spring 5_, _AspectJ 1.9.1_ based minimal example with _Sockets_.
Quick example for Stack Overflow [question >>](https://stackoverflow.com/questions/52517497/around-aspect-blocks-execution-in-thread)

To start tomcat server run next command from the repository root:

```sh
$ mvn org.apache.tomcat.maven:tomcat7-maven-plugin:2.2:run
```

Then open in browser:

1. `http://localhost:8080/server` - this will initialize the thread, which will listen a socket
2. `http://localhost:8080/client/message1` - this will initialize connect to the socket and send `message1` message
3. `http://localhost:8080/client/message2` - this will send `message2` message

In the log you will see two entries related to calls of `MyService.doCall()`, i.e. _AspectJ_ related logic:

```
[INFO] 01:44:23.155 [Thread-4] com.stackoverflow.MyServiceImpl === message = message1
[INFO] 01:44:23.157 [Thread-4] com.stackoverflow.MyServiceAspect === Took 2 milliseconds
[INFO] 01:44:25.015 [Thread-4] com.stackoverflow.MyServiceImpl === message = message2
[INFO] 01:44:25.016 [Thread-4] com.stackoverflow.MyServiceAspect === Took 1 milliseconds
```

---

## Debugging

To debug application using _Remote Debugging_, before starting tomcat server set up `MAVEN_OPTS` environment variable:

 * For _Linux_ / _Unix_:
 `export MAVEN_OPTS="-Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000"`
 * For _Windows_:
 `set MAVEN_OPTS=-Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000`

Then set up _Remote Debugging_ in the _IDE_ using appropriate manual (remote port to use is `8000`).
