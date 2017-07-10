# fx-validation
Simple spring boot rest app for fx-validation

## About the project

  The project was built using Spring(Boot, MVC, Repository, Cache), JPA and lombok. The JPA is used in 
conjunction with spring repositories and spring services with a derby embedded db. The spring service 
has some cacheable operations. Some of the operations have an eternal cache, and some others a less 
durable cache. The cache configuration is at BeanConfig class. 
  Because we call an external system (fixer.io) to get the business dates, the first time we call
the operations are slower. After some time, it will be fast as the business dates are cached forever.

## Architectural decisions
The web application was build to be a shared nothing style, totally stateless. This allows to 
scale the application just by adding more machines to the cluster. For this, it is needed a
load balancer. Another decision I made that might sound akward, was the rules engine. I thought
about using Drools, but my personal experience with it was not the best. It is more difficult
to debug, and the fact that we might not have it versioned (due the possiblity of business people
be able to change it) is not very good in my opinion.

## Rest documentation and Client console
The application uses swagger to document the rest API. It is accesible on:

-http://localhost:8080/swagger-ui.html

Also on this page, it is possible to execute the api.

## Metris
To access the metrics page go to:
-http://localhost:8080/metrics

## Prerequisites
Gradle, java8

## How to build
-To build the project, on a terminal go to the root folder of the project, and run:

./gradlew build

## How to RUN
-To run the project, on a terminal go the root folder of the project, and run: 

./gradlew bootRun


### The rule engine
  On the package com.eduardomanrique.fxvalidation.ruleengine we have the classes responsible for the
rule engine framework. The framework is a very simple engine to execute Rules in a sequential maner.
  We have the following classes:
  
  -Rule: Interface to be implemented by rules.
  
  -RuleEngine: An spring component that keeps the existing rules and has a way to fire all the rules against a fact.
  
  -Validation: An abstract class to be filled on a rules execution.
  
### Products
  The products are represented by classes in the follwoing hierarchy:
  
  -FXTransaction: abstract class mother of all trade classes
  -Spot: child of FXTransaction
  -Forward: child of FXTransaction
  -VanillaOption: abstract class child of FXTransaction and mother of option classes
  -EuropeanVanillaOption: VanillaOption of style Europe, child of VanillaOption
  -AmericaVanillaOption: VanillaOption of style American, child of VanillaOption
  
### Rules
  To create new rules just implement the interface com.eduardomanrique.fxvalidation.ruleengine.Rule and 
annotate it with @Component from spring. Spring will automatically add the rule to the rule engine. 
  There are 5 implemented rules. They are located in the package "com.eduardomanrique.fxvalidation.rules":
  
  -AllTradesRule: This rule will be fired by trade objects types (spot, forward and option)
  
  -AllOptionsRule: This rule will be fired by all child of VanillaOption class
  
  -AmericanOptionsRule: This rule will be triggered just by AmericanVanillaOption class
  
  -SpotRule: Triggered just by spot trades
  
  -ForwardRule: Triggered just by forward trades.
  
### Project structure
  
#### Main source (src/main/java):
  
  -com.eduardomanrique.fxvalidation.rules.*: Rules to apply the validations
  
  -com.eduardomanrique.fxvalidation.spring: SpringBoot configuration classes
  
  -com.eduardomanrique.fxvalidation.entitiy.*: JPA entities
  
  -com.eduardomanrique.fxvalidation.products.*: Product classes
  
  -com.eduardomanrique.fxvalidation.products.deserializer.FXTransactionDeserializer: Json Deserializer for Product classes
  
  -com.eduardomanrique.fxvalidation.repository.*: Spring repositires
  
  -com.eduardomanrique.fxvalidation.service.*: Spring services
  
  -com.eduardomanrique.fxvalidation.util.*: Utilitary classes
  
  -com.eduardomanrique.fxvalidation.rulesengine.*: Rules engine framework classes 
  
  -com.eduardomanrique.fxvalidation.FXValidationApiApplication: Main class 
  
#### Main resources (src/main/resources)
  
  -sql/*: ddl and dml database scripts
  
  -/application.yml: Spring properties file
  
#### Test sources (src/test/java)
  
  -com.eduardomanrique.fxvalidation.rules.*: Unit tests for rules classes
  
  -com.eduardomanrique.fxvalidation.integration.*: Integrated tests. Simulates a complete test over the application
  
  -com.eduardomanrique.fxvalidation.rulesengine.*: Unit tests for the rule engine framework.
  
  -com.eduardomanrique.fxvalidation.rules.*: Unit tests for the implemented rules.
  
  -com.eduardomanrique.fxvalidation.service.*: Unit tests for the spring services.
  
