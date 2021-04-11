# WebRegister
A web register system.

A system in JEE created to maintain student's presence, grades, etc. An excercise I made to learn more about JEE and DI with Guice.

This project is split into three modules:
* WebRegister-ear - an EAR file that is intended to be deployed to a JEE server (tested on Glassfish).
* WebRegister-ejb - an EJB module with most of the application logic.
* WebRegister-web - with Guice configuration and application's API.
