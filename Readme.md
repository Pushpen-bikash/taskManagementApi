## Task Management Service Api


## Steps to Setup

1. **Clone the application**

	```bash
	https://github.com/Pushpen-bikash/taskManagementApi.git
	cd taskManagementApi
	```

2. **Create MySQL database**

	```bash
	create database taskManagement
	```

3. **Change MySQL username and password as per your MySQL installation**

	+ open `src/main/resources/application.properties` file.

	+ change `spring.datasource.username` and `spring.datasource.password` properties as per your mysql installation

4. **Run the app**

	You can run the spring boot app by typing the following command -

	```bash
	mvn spring-boot:run
	```

	The server will start on port 8080.

	You can also package the application in the form of a `jar` file and then run it like so -

	```bash
	mvn package
	java -jar target/taskManagement-0.0.1-SNAPSHOT.jar
	```
5. **Default Roles and Task Status**
	
	The spring boot app uses role based authorization powered by spring security. Additionally, I have also inserted the status of task during project start. To add the default roles and status in the database, I have added the following sql queries in `src/main/resources/data.sql` file. Spring boot will automatically execute this script on startup -

	```sql
    INSERT IGNORE INTO roles(name) VALUES('ROLE_USER');
    INSERT IGNORE INTO roles(name) VALUES('ROLE_ADMIN');

    INSERT IGNORE INTO task_status(status) VALUES('Inprogress');
    INSERT IGNORE INTO task_status(status) VALUES('Submitted');
    INSERT IGNORE INTO task_status(status) VALUES('Closed');
	```

	Any new user who signs up to the app is assigned the `ROLE_USER` by default.
	An admin user is also created on application startup in `AuthService.java` file
	
