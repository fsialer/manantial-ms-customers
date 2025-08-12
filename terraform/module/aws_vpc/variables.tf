variable "cidr" {
  type = string
  default = "10.0.0.0/16"
}

variable "tags" {
  type = map(string)
}

variable "private_subnet" {
  type = map(object({
    cidr = string
    availability_zone = string
  }))
}

variable "public_subnet" {
  type = map(object({
    cidr = string
    availability_zone = string
  }))
}