#!/bin/bash
# instalar keycloak por helm
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update
helm install keycloak bitnami/keycloak \
  --set auth.adminUser=admin \
  --set auth.adminPassword=adminpassword


# kubectl port-forward svc/keycloak 8080:80 Para acceder por port-forward a keycloak

# Producción
helm install keycloak-prod bitnami/keycloak -f values/values-production.yml

# Staging
helm install keycloak-staging bitnami/keycloak -f values/values-staging.yml

# Desarrollo
helm install keycloak-dev bitnami/keycloak -n dev -f values/values-development.yml

# Local
helm install keycloak-local bitnami/keycloak -f values/values-local.yml

# Instalar kafka por helm
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update
# Producción
helm install kafka-prod bitnami/kafka -f values/values-production.yml

# Staging
helm install kafka-staging bitnami/kafka -f values/values-staging.yml

# Desarrollo
helm install kafka-dev bitnami/kafka -n dev -f values/values-development.yml

# Local
helm install kafka-local bitnami/kafka -f values/values-local.yml


helm upgrade --install keycloak-dev bitnami/keycloak --n dev -f values/values-development.yml


helm upgrade --install ingress-nginx ingress-nginx --repo https://kubernetes.github.io/ingress-nginx --namespace ingress-nginx --create-namespace
helm upgrade keycloak-dev bitnami/kafka -n dev -f dev/kafka-deployment/values/values-development.yml # para actualizar

helm upgrade keycloak-dev bitnami/keycloak -n dev -f dev/keycloak-deployment/values/values-development.yml 
helm upgrade kafka-dev bitnami/kafka -n dev -f dev/kafka-deployment/values/values-development.yml 
