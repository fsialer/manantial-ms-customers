#!/bin/bash
if [[ -z "$1" && -z "$2" && -z "$3" ]]; then
  read -p "Ingresa region: " region
  read -p "Ingresa profile: " profile
  read -p "Ingresa name_eks: " name_eks
else
  region=$1
  profile=$2
  name_eks=$3
fi
echo "Conectando al cluster $name_eks en la región $region usando el perfil $profile..."
aws eks --region $region --profile $profile update-kubeconfig --name $name_eks
if [ $? -eq 0 ]; then
  echo "✅ Kubeconfig actualizado correctamente para el cluster $name_eks"
else
  echo "❌ Hubo un error actualizando el kubeconfig"
fi