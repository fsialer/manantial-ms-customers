#!/bin/bash
if [ -z "$1" ]; then
  read -p "Ingrese un entorno (local, dev, staging, prod): " environment
else
  environment=$1
fi

function eliminar_namespace(){
  echo "⌛ Eliminando namespace para $environment ..."
  kubectl delete namespace $environment
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
eliminar_namespace
desinstalar_kafka
desinstalar_keycloak
echo "✅ Desinstalacion completa de $environment"
