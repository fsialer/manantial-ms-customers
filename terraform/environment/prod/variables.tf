variable "ENV" { type = string }
variable "PROJECT" { type = string }
variable "AZ_LOCATION" { type = string }
variable "AZ_GROUP_NAME" { type = string }
variable "AZ_AKS_CLUSTER_NAME" { type = string }
variable "ARM_SUBSCRIPTION" {
  type      = string
}
variable "ARM_TENANT_ID" {
  type      = string
}
variable "LISTS_SECRETS" {
  type = list(object({
    KEY   = string
    VALUE = string
  }))
}


