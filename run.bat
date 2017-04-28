call mvn clean
call mvn install
call mvn package
call java -jar target/dependency/jetty-runner.jar target/web-app.war