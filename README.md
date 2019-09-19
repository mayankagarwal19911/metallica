# metallica
This Git Repo holds microservices for trade application

This is the POC named as Metallica that comprises of multiple 
microservices each independent of each other

For registering services - Eureka server and eureka client
For Api gateway - Zuul
For tracing requests - Zipkin
Spring Cloud Configuration server for Config server
DB - H2

trade-service = Serves as a entry point that consumes trade details to buy and put the trade status with it's id in QUEUE
market-data-service = Gets live data from third party api to fetch price against commodit passed in trade-service
        pls note : MDS is fetching price data from database for now
notification-service = It takes each trade put in the queue by trade-service and mark status as "processed" and update 
        it in trade-service DB
zuul-api-gateway-server = API gateway through whioch eac requests goes in. We can use it for any kind of authentication etc.
          For now in this application we are just logging each request coming via gateway
eureka-server =  each service registers itself with this server to communicate.
spring-cloud-config-server = Config server to read configuration from centralised repository for multiple environments.


