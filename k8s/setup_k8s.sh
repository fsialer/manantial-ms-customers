#!/bin/bash
function ejecutar_manifiestos() {
    echo "⌛ Creando configmap en $environment ..."
    kubectl apply -f ./$environment/configmap.yml -n $environment
    echo "⌛ Creando secrets en $environment ..."
    kubectl apply -f ./$environment/secret.yml -n $environment
    echo "⌛ Creando pv/pvc/deployment/service de mongo en $environment ..."
    kubectl apply -f ./$environment/backend_mongo.yml -n $environment
    echo "⌛ Creando pv/pvc/deployment/service de redis en $environment ..."
    kubectl apply -f ./$environment/backend_redis.yml -n $environment
    echo "⌛ Creando deployment/service de customers en $environment ..."
    kubectl apply -f ./$environment/backend_customer.yml -n $environment
    echo "⌛ Creando deployment/service de consumer en $environment ..."
    kubectl apply -f ./$environment/backend_consumer.yml -n $environment
    echo "⌛ Creando deployment/service de gateway en $environment ..."
    kubectl apply -f ./$environment/backend_gateway.yml -n $environment
    echo "⌛ Creando ingress rules $environment ..."
    kubectl apply -f ./$environment/ingress-rule.yml -n $environment
}

function crear_namespace(){
  echo "⌛ Verificando namespace 'ingress-nginx'..."
  if ! kubectl get namespace ingress-nginx &> /dev/null; then
    echo "� Instalando ingress-nginx y creando namespace..."
    helm upgrade --install ingress-nginx ingress-nginx \
      --repo https://kubernetes.github.io/ingress-nginx \
      --namespace ingress-nginx \
      --create-namespace
  else
    echo "✅ Namespace 'ingress-nginx' ya existe."
  fi

  echo "⌛ Verificando namespace '$environment'..."
  if ! kubectl get namespace "$environment" &> /dev/null; then
    echo "� Creando namespace '$environment'..."
    kubectl create namespace "$environment"
  else
    echo "✅ Namespace '$environment' ya existe."
  fi
}

function validar_entorno() {
    case "$environment" in 
        dev|staging|prod|local)
                ;;
        *)
           echo "❌ El entorno ingresado no es válido."
           exit 1
           ;;
    esac
}

function instalar_kafka(){
  echo "⌛ Instalando chart de kafka en $environment ..."
  helm install kafka-$environment bitnami/kafka -n $environment -f ./$environment/kafka-deployment/values/values-development.yml
}

function instalar_keycloak(){
  echo "⌛ Instalando chart de keycloak en $environment ..."
  helm install keycloak-$environment bitnami/keycloak -n $environment -f ./$environment/keycloak-deployment/values/values-development.yml
}

# ejecutar
if [ -z "$1" ]; then
  read -p "Ingrese un entorno (local, dev, staging, prod): " environment
else
  environment=$1
fi
validar_entorno
echo "📦 Agregando repositorio de bitnami ..."
helm repo add bitnami https://charts.bitnami.com/bitnami
echo "📦 Actualizando paquetes ..."
helm repo update
crear_namespace
instalar_kafka
instalar_keycloak
ejecutar_manifiestos
echo "✅ Instalacion completa de $environment"
kubectl get svc ingress-nginx-controller -n ingress-nginx
