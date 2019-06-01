# media

repo contains:<br>
	- application:<br>
	- docker-compose:<br>
	- keycloak authentication server<br>
    - postgres database<br>
	- elasticsearch<br>
	- logstash for importing data from postgres into elasticsearch<br>		

prerequisites:<br>
	- docker-compose<br>
	- java<br>
	- mvn<br>

setup: <br>
	run <br> 
		```docker-compose up --build db auth elasticsearch```<br>
	when all services are ready start the application:<br>
		```mvn spring-boot:run```<br>
	after the app has successfully started start logstash:<br>
		```docker-compose up logstash```<br>

prepare keycloak users:<br>
	
		```bash
		# enter container
		docker exec -it <keycloak-container-id> /bin/bash

		cd keycloak/bin/

		# authenticate admin user
		./kcadm.sh config credentials --server http://localhost:8080/auth --realm master --user admin --password admin

		# create user
		USER_ID=$(./kcadm.sh create users -r media_realm -s username=media_user -s enabled=true -i)

		# update use password
		./kcadm.sh update users/${USER_ID}/reset-password -r media_realm -s type=password -s value=media_user -s temporary=false -n

		# add role to user
		./kcadm.sh add-roles -r media_realm --uusername=media_user --cclientid rest_media_client_id --rolename media_user
		```

improvement suggestions:

	- furher automize keycloak setup (so additional command execution won't be needed)
		- currently it's necessary to execute some commands manually after the keycloak container has started
	- include application in docker-compose instead of manually running `mvn spring-boot:run`
		- currenly running the application as part of docker-compose causes some netwok issue
		which does not allow the spring app to verify the token
	- verify elastisearch implementation
	- connection leak inside of tests (tests are temporartily disabled)