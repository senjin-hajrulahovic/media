# media

repo contains:
	- application:
	- docker-compose:
		keycloak authentication server
		postgres database
		elasticsearch
		logstash for importing data from postgres into elasticsearch		

prerequisites:
	docker-compose
	java
	mvn

setup:
	run 
		```docker-compose up --build db auth elasticsearch```
	when all services are ready start the application:
		```mvn spring-boot:run```
	after the app has successfully started start logstash:
		```docker-compose up logstash```

	prepare keycloak users:
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