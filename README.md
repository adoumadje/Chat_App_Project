**Realtime Chat Application with:**<br>
+ Spring boot + Spring Data Jpa(Hibernate) + Spring Security(Bcrypt password encoder) 
+ Websocket(SockJS and STOMP) for messaging + MySQL database + Unit Testing using JUnit, AssertJ and Mockito
+ Angular, PrimeNg for components, PrimeFlex for styling and PrimeIcons for icons + Postman and javascript for API testing
+ Selenium and Chrome webdriver for UI testing + AWS EC2, VPC, S3, NGINX, SSH for deployment + Docker, docker-compose and Github Actions
for CI/CD pipeline.

**Open Live**: http://ec2-3-76-223-198.eu-central-1.compute.amazonaws.com

**How the application works?**<br>
The application is a normal chat application where you have the list of all registered users and you can send them private messages.<br>
A private message is only accessible to you and the user to whom you sent that message.<br>
There is also a public chatroom where you can send message and it can be seen by all the users.<br>
You can also see which users are currently online

**If you don't want to create an account**<br>
You can use these two users to test the functionalities of the application:<br>
1. email: fede.valverde@fmail.com && password: Password1234#
2. email: eduardo.camavinga@fmail.com && password: Password1234#


**The Backend**<br>
For the backend I used Spring Boot 3, Spring Data Jpa (Hibernate) and Spring Security for the Bcrypt password encoder. I have implemented
the authentication flow from regitering the user to login, reset the password and logout. I configured two endpoints for messages: one for chatroom/public 
messages and one for user/private messages

**The Frontend**<br>
For the frontend I used angular, PrimeNg for the different components such as Dialog, Toast, form inputs. In addition I used PrimeFlex
for styling and PrimeIcon for icons. I used SockJS-Client and stompjs for messaging on the client side.

**Testing**<br>
I wrote unit tests for my backend using JUnit, AssertJ and Mockito.<br>
I wrote tests for my RestAPI on postman<br>
I automated my UI testing using Selenium and Chrome driver

**DevOps**<br>
I used docker to containerize my application for easier deployment on AWS.<br>
I created three Dockerfiles: one for the frontend, one for the backend and one for my database.<br>
I created a CI/CD pipeline using github actions that will run my docker-compose file to build the 3 images corresponding to <br>
my frontend, my beckend and my database. Then it will push those images to docker hub.<br>
Then I load the docker-compose file that will allow me to pull my images and run my containers to S3 bucket and I make it public <br>
so it can be downloaded.<br>
Then I SSH to my EC2 to download my docker-compose file and pull my images and run my comtainers.


