[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=fsialer_manantial-customers-service&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=fsialer_manantial-customers-service)
# MICROSERVICIO DE CLIENTES
Este microservicio de clientes se encarga de registrar, consultar y obtener metricas.

## TABLA DE RECURSOS GATEWAY
| NOMBRE                           | RUTA                      | PETICION | PARAMETROS                                             | CUERPO                                                                                   | 
|----------------------------------|---------------------------|----------|--------------------------------------------------------|------------------------------------------------------------------------------------------| 
| Listar clientes                  | /api/v1/customers         | GET      | NINGUNO                                                | NINGUNO                                                                                  |
| Obtener metricas de los clientes | /api/v1/customers/metrics | GET      | NINGUNO                                                | NINGUNO                                                                                  |
| Guardar clientes                 | /api/v1/customers    | POST     | NINGUNO                                                | {<br/>"name":"Jhon"<br/>"lastName":"Doe"<br/>"age":34<br/>"birthDate":"1991-05-01"<br/>} |
[Reposito: Gateway](https://github.com/fsialer/manantial-gateway)

## TABLA DE RECURSOS
| NOMBRE                         | RUTA                   | PETICION |  
|--------------------------------|------------------------|----------|
| Actuator                       | /actuator              | GET      | 
| Documentacion                  | /swagger-ui/index.html | GET      | 
| Listar clientes                | /v1/customers          | GET      | 
| Obtener metricas               | /v1/customers/metrics  | GET      | 
| Guardar clientes               | /v1/cusomers           | POST     | 

## EJECUTAR TEST
````ssh
gradle test jacocoTestReport
````

## INDICACIONES DE DESPLIEGUE LOCAL
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
Crea los roles **admin** y **user** (Roles obligatorios)
![img_10.png](./images/img_10.png)
4. Crear Usuario
![img_11.png](./images/img_11.png)
![img12.png](./images/img_12.png)
5. Asignar roles Usuario
![img14.png](./images/img_14.png)
![img15.png](./images/img_15.png)
Procura filtrar por client y busca los roles.
![img16.png](./images/img_16.png)
6. Crear contraseña
![img18.png](./images/img_18.png)
![img19.png](./images/img_19.png)
![img22.png](./images/img_22.png)
![img23.png](./images/img_23.png)
7. Obtener client id  y client secret
![img24.png](./images/img_24.png)
![img25.png](./images/img_25.png)
![img25.png](./images/img_26.png)

## Obtener token
````declarative
CLIENT_ID = Se obtiene de la configuracion de keycloak
CLIENT_SECRET = Se obtiene de la configuracion de keycloak
USER = Es el usuario creado en la configuracion de keycloack
PASSWORD = Es la contraseña del usuario creado en la configuracion de keycloack
````
````bash
curl --location --request POST 'http://keycloak-server:8080/realms/manantial-realm/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=CLIENT_ID' \
--data-urlencode 'client_secret=CLIENT_SECRET' \
--data-urlencode 'grant_type=password' \
--data-urlencode 'username=USER' \
--data-urlencode 'password=PASSWORD'
````

## Usar token en el microservicio de clientes
````declarative
TOKEN= Valor obtenido usando el comando de Obtener token
````
````bash
curl --location 'http://manantial-gateway:8090/api/v1/customers' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer TOKEN' \
--data '{
  "name": "John",
  "lastName": "Doe",
  "age": 33,
  "birthDate": "1991-09-05"
}'
````

## SOLUCION AL ENVIO DE MENSAJERIA ASINCRONICA
> Para abordar el problema de [desempeño / escalabilidad / desacoplamiento], se adoptó una arquitectura orientada a eventos (event-driven), utilizando Apache Kafka para implementar el patrón de publicación/suscripción (pub/sub).
Este enfoque permite que diferentes servicios estén informados en tiempo real sobre los eventos generados al momento de guardar información, sin afectar el rendimiento del sistema principal.
Los eventos se procesan de forma asíncrona y los datos se almacenan en una base de datos NoSQL, lo que facilita su posterior análisis y permite una recuperación de datos más rápida.
Así se evitan cuellos de botella y se mejora el rendimiento general de la aplicación, especialmente en operaciones como generación de reportes o tareas analíticas.
![img30.png](./images/img_30.png)
[Repositorio: customer consumer](https://github.com/fsialer/manantial-ms-consumer)
## DESPLIEGUE EN LA NUBE
> Para poder realizar el despliegue en la nube nos ayudaremos en un recurso de azure (AKS), que nos permite crear 
> pequeñas maquinas al igual que docker para asi tener un control de cuando escalar dichas maquinas o pod, esto se lograra 
> con la ayuda de los pods en kubernetes. A continuacion se mostrara la arquitectura a implmentar.
![img31.png](./images/img_31.png)
 La confguracion se encuentra en las rutas:
> 1. Despliegue en terraform: ./terraform
> 2. Configuracion en k8s: ./k8s


## Tech
1. WebFlux
2. Mockito
3. mongodb
4. kafka
5. sonarqube
6. jacoco
7. github actions
8. terraform
9. prometheus
10. grafana
11. Kubernetes
12. Docker
13. Docker compose



