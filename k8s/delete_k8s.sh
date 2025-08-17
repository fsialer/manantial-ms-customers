#!/bin/bash
if [ -z "$1" ]; then
  read -p "Ingrese un entorno (local, dev, staging, prod): " environment
else
  environment=$1
fi

function eliminar_namespace(){
  echo "⌛ Eliminando namespace para $environment ..."
  kubectl delete namespace $environment
  if kubectl get namespace "$environment" &> /dev/null; then
    echo "❌ El namespace $environment todavía existe."
  else
    echo "✅ Namespace $environment eliminado correctamente."
  fi
}

function eliminar_consumer(){
  echo "⌛ Eliminando consumer para $environment ..."
  kubectl delete -f ./$environment/backend_consumer.yml -n $environment
  if [ $? -ne 0 ]; then
    echo "❌ Error al eliminar consumer en $environment"
  fi
}

function eliminar_costumer(){
  echo "⌛ Eliminando customer para $environment ..."
  kubectl delete -f ./$environment/backend_costumer.yml -n $environment
  if [ $? -ne 0 ]; then
    echo "❌ Error al eliminar costumer en $environment"
  fi
}
function eliminar_gateway(){
  echo "⌛ Eliminando gateway para $environment ..."
  kubectl delete -f ./$environment/backend_gateway.yml -n $environment
  if [ $? -ne 0 ]; then
    echo "❌ Error al eliminar gateway en $environment"
  fi
}

function eliminar_mongo(){
  echo "⌛ Eliminando mongo para $environment ..."
  kubectl delete -f ./$environment/backend_mongo.yml -n $environment
  if [ $? -ne 0 ]; then
      echo "❌ Error al eliminar mongo en $environment"
  fi
}

function eliminar_redis(){
  echo "⌛ Eliminando redis para $environment ..."
  kubectl delete -f ./$environment/backend_redis.yml -n $environment
  if [ $? -ne 0 ]; then
    echo "❌ Error al eliminar redis en $environment"
  fi
}

function eliminar_ingress(){
  echo "⌛ Eliminando ingress para $environment ..."
  kubectl delete -f ./$environment/ingress-rule.yml -n $environment
  if [ $? -ne 0 ]; then
    echo "❌ Error al eliminar ingress en $environment"
  fi
}

function eliminar_configmap(){
  echo "⌛ Eliminando configmap para $environment ..."
  kubectl delete -f ./$environment/configmap.yml -n $environment
  if [ $? -ne 0 ]; then
    echo "❌ Error al eliminar configmap en $environment"
  fi
}

function eliminar_secret(){
  echo "⌛ Eliminando secret para $environment ..."
  kubectl delete -f ./$environment/secret.yml -n $environment
  if [ $? -ne 0 ]; then
    echo "❌ Error al eliminar secret en $environment"
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

# ejecutar
validar_entorno
echo "⌛ Desinstalar k8s de $environment"
eliminar_consumer
eliminar_costumer
eliminar_gateway
eliminar_mongo
eliminar_redis
eliminar_ingress
eliminar_configmap
eliminar_secret
eliminar_namespace
desinstalar_kafka
desinstalar_keycloak
echo "✅ Desinstalacion completa de $environment"
