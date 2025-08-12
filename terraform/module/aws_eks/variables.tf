variable "tags" {
  type = map(string)
}

variable "cluster_name" {
  type = string
  default = "cluster_default"
}

variable "iam_role_name" {
  type = string
  default = "eks-cluster-example"
}

variable "private_subnets" {
  type = list(string)
}

variable "types_instance" {
  type    = list(string)
  default = []
}

variable "scaling_desired" {
  description = "Escalado deseado"
  type = number
  default = 1
}

variable "scaling_max" {
  description = "Escalado maximo"
  type = number
  default = 1
}

variable "scaling_min" {
  description = "Escalado minimo"
  type = number
  default = 1
}

variable "type_ami" {
  description = "Tipo ami"
  type = string
  default = "AL2_x86_64"
}

variable "update_config_max" {
  description = "Cantidad de pods que puede actualizar a la vez."
  type = number
  default = 1
}
