variable "region" {
  type    = string
  default = "us-east-1"
  validation {
    condition     = contains(["us-east-1", "us-east-2", "us-east-3"], var.region)
    error_message = "La region es invalida"
  }
}

variable "tags" {
  type = map(string)
}

variable "s3_bucket_name" {
  type    = string
  default = "bucket-default"
}

variable "eks_cluster_name" {
  type    = string
  default = "cluster_default"
}

variable "vpc_cidr" {
  type    = string
  default = "10.0.0.0/16"
}



variable "vpc_private_subnet" {
  type = map(object({
    cidr              = string
    availability_zone = string
  }))
}

variable "vpc_public_subnet" {
  type = map(object({
    cidr              = string
    availability_zone = string
  }))
}

variable "eks_iam_role_name" {
  type = string
}

variable "eks_types_instance" {
  type    = list(string)
  default = ["t3.micro"]
}

variable "eks_scaling_desired" {
  description = "Escalado deseado"
  type        = number
  default     = 1
}

variable "eks_scaling_max" {
  description = "Escalado maximo"
  type        = number
  default     = 1
}

variable "eks_scaling_min" {
  description = "Escalado minimo"
  type        = number
  default     = 1
}

variable "eks_type_ami" {
  description = "Tipo ami"
  type        = string
  default     = "AL2_x86_64"
}

variable "eks_update_config_max" {
  description = "Cantidad de pods que puede actualizar a la vez."
  type        = number
  default     = 1
}

