module "resource_group" {
  source   = "../../module/resource_group"
  name     = var.AZ_GROUP_NAME
  location = var.AZ_LOCATION
  env      = var.ENV
  project  = var.PROJECT
}


module "aks" {
  source              = "../../module/aks"
  name                = var.AZ_AKS_CLUSTER_NAME
  location            = var.AZ_LOCATION
  resource_group_name = module.resource_group.name
  env                 = var.ENV
  project             = var.PROJECT
}
