# media

# keycloak users are not included in an export so we have to add them through the cli by following these steps:

#enter container
docker exec -it <keycloak-container-id> /bin/bash

cd keycloak/bin/

# authenticate admin user
./kcadm.sh config credentials --server http://localhost:8080/auth --realm master --user admin --password admin

# create user
USER_ID=$(./kcadm.sh create users -r media_realm -s username=media_user -s enabled=true -i)
# created user with id <user-id>

#update use password
./kcadm.sh update users/${USER_ID}/reset-password -r media_realm -s type=password -s value=media_user -s temporary=false -n

#add role to user
./kcadm.sh add-roles -r media_realm --uusername=media_user --cclientid rest_media_client_id --rolename media_user