I would like for you to try again bearing in mind I am using spring boot 3.5.9, maven and already intialized db with these settings 

<server:
  port: 8080

spring:
  application:
    name: bsapi
  datasource:
    url: jdbc:postgresql://localhost:5432/bsdb
    username: bsdbu
    password: bsdbp2Pass&1!
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: none
    properties:
      hibernate:
        format-sql: true
        jdbc:
          time-zone: UTC
    open-in-view: false

management:
  endpoints:
    web:
      exposure:
        include: health,mappings,info
  endpoint:
    health:
      show-details: when_authorized

logging:
  level:
    root: INFO
>

 I also expect bluestron branding colors implemented with tailwind defaults only, strict typescript conventions adhered to (never use any or unknown cause typescript will scream) and nextjs practices maintained (seo + speed first approches like next/image intead of imr src). 
 
 I would also like to know if this additional input would casue any problems/ derail you or lead to complications I would like for you to tell me should you be vexed so we build clean, smooth (no errors) and stack up on a good foundation (we can ignore speed so long as we keep moving forward with near perfect code -- keeping to bluestron as the bible, but utilizing the plan like follwoing moses through the desert and taking opinionated stands like jesus).