### Docker setup
1. Build the parent module - it will generate executable jar for orders and basic jar for users
2. Add spring-boot-maven-plugin to users/pom.xml and build it - it will generate executable jar for users     
3. Run [/~]$ docker-compose up                  

### Intro

![Untitled Diagram](https://user-images.githubusercontent.com/32780138/77488166-beffa080-6e34-11ea-9215-bf18e01cf6f2.png)


User service is running on port 9090. 
Orders service is running on port 8080.
User service has one circuit breaker for the database.
Orders service has two circuit breakers: one for the user service and one for the database.
