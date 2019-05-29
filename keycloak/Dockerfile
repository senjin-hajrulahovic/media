FROM jboss/keycloak

EXPOSE 8080:8090
EXPOSE 9990:9990

ENV KEYCLOAK_USER admin
ENV KEYCLOAK_PASSWORD admin

WORKDIR /opt/jboss/keycloak/bin

USER root

#COPY initialize.sh /opt/jboss/keycloak/bin/.
COPY wait-for-it.sh /
RUN chmod +x /wait-for-it.sh

CMD [ "/wait-for-it.sh", "http://localhost:8080", "-t", "100", "--" ]

#CMD ["./kcadm.sh", "config", "credentials", "--server", "http://localhost:8080/auth", "--realm", "master", "--user", "admin", "--password", "admin"]
CMD ./kcadm.sh config credentials --server http://localhost:8080/auth --realm master --user admin --password admin

#CMD ["./kcadm.sh", "create", "realms", "--server", "http://localhost:8080/auth", "--realm", "master", "--user", "admin", "--password", "admin"]
CMD ./kcadm.sh create realms -s realm=media_realm -s enabled=true -o

#CMD ["./kcadm.sh", "config", "credentials", "--server", "http://localhost:8080/auth", "--realm", "master", "--user", "admin", "--password", "admin"]
CMD ./kcadm.sh create clients -r media_realm -s clientId=media_client_id -s enabled=true -s baseUrl=http://localhost:8080 -s bearerOnly=true
