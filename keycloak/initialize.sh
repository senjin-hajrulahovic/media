#!/usr/bin/env bash

docker exec -it <keycloak-container-id> /bin/bash
cd keycloak/bin/

./kcadm.sh config credentials --server http://localhost:8080/auth --realm master --user admin --password admin

# create user
./kcadm.sh create users -r media_realm -s username=media_user -s enabled=true
# created user with id <user-id>

# need to create client roles
./kcadm.sh create clients/f914278a-1208-4b7a-a212-19058db6d7ed/roles -r media_realm -s name=media_user

#update use password
./kcadm.sh update users/bd7c0aa4-60fb-4fdc-bc74-140ebd8f54e7/reset-password -r media_realm -s type=password -s value=media_user -s temporary=false -n

#add role to user
./kcadm.sh add-roles -r media_realm --uusername=media_user --cclientid rest_media_client_id --rolename media_user

# acquire token
curl -d 'client_id=public_media_client_id' -d 'username=media_user' -d 'password=media_user' -d 'grant_type=password' 'http://localhost:8080/auth/realms/media_realm/protocol/openid-connect/token' | python -m json.tool
