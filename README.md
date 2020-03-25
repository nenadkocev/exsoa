### Docker setup
1. Build the parent module - it will generate executable jar for orders and basic jar for users
2. Add spring-boot-maven-plugin to users/pom.xml and build it - it will generate executable jar for users     
3. Run [/~]$ docker-compose up                  
4. Check if the services are running: http://localhost:9090/actuator/health http://localhost:8080/actuator/health

### Intro

![Untitled Diagram](https://user-images.githubusercontent.com/32780138/77488166-beffa080-6e34-11ea-9215-bf18e01cf6f2.png)


User service is running on port 9090. 
Orders service is running on port 8080.
User service has one circuit breaker for the database.
Orders service has two circuit breakers: one for the user service and one for the database.

### Configuration
Configuring the circuit breaker which protects calls to users database.
```
@Bean
    public Customizer<Resilience4JCircuitBreakerFactory> customizer() {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .slidingWindowSize(6)
                .minimumNumberOfCalls(6)
                .failureRateThreshold(50)
                .slowCallRateThreshold(100)
                .slowCallDurationThreshold(Duration.ofSeconds(4))
                .permittedNumberOfCallsInHalfOpenState(3)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .build();
        return factory -> factory.configure(builder ->
                builder.circuitBreakerConfig(circuitBreakerConfig).build(), "red");
    }
```
Circuit breakers for users service and products db have nearly identical configuration.
### Test the circuit breakers
1. Circuit breaker which protect the calls from orders to users service:
execute a post request to http://localhost:8080/order with content type application/json and with body
```
{
	"username": "exception",
	"productItems": [
			{
			"productName": "Coca-cola",
			"quantity": "1"
		}
	]
}
```
to simulate throwing exception or with body
```
{
	"username": "slowCall",
	"productItems": [
			{
			"productName": "Coca-cola",
			"quantity": "1"
		}
	]
}
```
to simulate slow call to users service.</br></br>
2. Circuit breaker which protects the calls from orders service to products database:
execute a post request to http://localhost:8080/order with content type application/json and with body
```
{
	"username": "user",
	"productItems": [
			{
			"productName": "exception",
			"quantity": "1"
		}
	]
}
```
to simulate throwing exception or with body
```
{
	"username": "user",
	"productItems": [
			{
			"productName": "slowCall",
			"quantity": "1"
		}
	]
}
```
to simulate long running query to database.</br></br>
3. Circuit breaker which protects the calls from users service to users database:
execute a post request to http://localhost:8080/order with content type application/json and with body
```
{
	"username": "dbException",
	"productItems": [
			{
			"productName": "Coca-cola",
			"quantity": "1"
		}
	]
}
```
to silumate throwing exception or with body 
```
{
	"username": "dbSlowCall",
	"productItems": [
			{
			"productName": "Coca-cola",
			"quantity": "1"
		}
	]
}
```
to simulate slow query to users database. This call will also open the circuit breaker for protecting againts the users service.
