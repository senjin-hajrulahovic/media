#!/usr/bin/env bash

docker exec -it <keycloak-container-id> /bin/bash
cd keycloak/bin/

./kcadm.sh config credentials --server http://localhost:8080/auth --realm master --user admin --password admin


./kcadm.sh create users -r media_realm -s username=media_user -s enabled=true
# create user with id <user-id>
./kcadm.sh update users/<user-id>/reset-password -r media_realm -s type=password -s value=media_user -s temporary=false -n
./kcadm.sh add-roles -r media_realm --uusername=media_user --rolename user

# acquire token
curl -d 'client_id=public_media_client_id' -d 'username=media_user' -d 'password=password' -d 'grant_type=password' 'http://localhost:8080/auth/realms/media_realm/protocol/openid-connect/token' | python -m json.tool
