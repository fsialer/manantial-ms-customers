[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=fsialer_manantial-customers-service&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=fsialer_manantial-customers-service)
# MICROSERVICIO DE CLIENTES
Este microservicio de clientes se encarga de registrar, consultar y obtener metricas.

## Ejecutar test
````ssh
gradle test jacocoTestReport
````

## Indicaciones para configurar docker compose
1. Crear archivo .env
````bash
toach .env
````
2. Copiar configuracion base en el archivo .env
````declarative
MONGO_USER=mongo
MONGO_PASSWORD=admin
AUTH_SERVER=http://keycloak-server:8080/realms/manantial-realm
CUSTOMER_URL=http://customer-service:8081/v1/customers
KAFKA_HOST=kafka:9092
MONGO_CUSTOMER_HOST=mongodb://mongo:admin@mongo6:27017/customers_db?authSource=admin
DISCORD_WEBHOOK=WEBHOOK_DISCORD
DISCORD_USERNAME=USUARIO_DISCORD
DEBUG=true
KAFKA_ADVERTISED_HOST_NAME= kafka
KAFKA_ZOOKEEPER_CONNECT= zookeeper:2181
KEYCLOAK_ADMIN=admin
KEYCLOAK_ADMIN_PASSWORD=admin
CLIENT_ID=spring-gateway-client
MONGO_CONSUMER_HOST=mongodb://mongo:admin@mongo6:27017/customers_consumer_db?authSource=admin
````
3. Ejecutar docker composer
````bash
docker compose up -d
````
## Configurar usuario de keycloak
1. Crear realm
![img.png](./images/img.png)
![img_1.png](./images/img_1.png)
2. Crear client
![img_3.png](./images/img_3.png)
![img_4.png](./images/img_4.png)
![img_5.png](./images/img_5.png)
![img_6.png](./images/img_6.png)
![img_7.png](./images/img_7.png)
3. Crear Roles
![img_9.png](./images/img_9.png)
![img_10.png](./images/img_10.png)
4. Crear Usuario
![img_11.png](./images/img_11.png)
![img12.png](./images/img_12.png)
5. Asignar roles Usuario
![img14.png](./images/img_14.png)
![img15.png](./images/img_15.png)
![img16.png](./images/img_16.png)
6. Crear contraseña
![img18.png](./images/img_18.png)
![img19.png](./images/img_19.png)
![img22.png](./images/img_22.png)
![img23.png](./images/img_23.png)
7. Obtener client id  y client secret
![img23.png](./images/img_23.png)
![img24.png](./images/img_24.png)
![img25.png](./images/img_25.png)
![img25.png](./images/img_26.png)
