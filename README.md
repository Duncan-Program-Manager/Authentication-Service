# Basic-SpringAPI
Basic Spring API with JPA-Hibernate-Security

DON'T FORGET TOO CHANGE THE PORTS FOR YOUR RUNNING SERVICE

For Using this template: This Spring boot template wont run out of the box, you have "2 main options":

```
adding @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class}) ontop of the starting class. This will disable a datasource and no database will have to be specified
adding a source in the resources/application.yml file. for an example:
#spring:
#  datasource:
#    url: "jdbc:mysql://localhost:3306/test"
#    username: "root"
#    password:
in the application.properties you can also put in a server port : 
# server:
#    port: 8081
```

Order of starting services:
```
- Discorvery Service / Eureka
- Gateway Service
- Other Services
