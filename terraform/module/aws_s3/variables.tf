variable "bucket_name" {
  type = string
  default = "customer-bucket"
  validation {
    condition = can(regex("^[a-z0-9]([a-z0-9.-]{1,61}[a-z0-9])?$", var.bucket_name))
    error_message = "No cumple con el nombre estandar."
  }
}

variable "tags" {
  type = map(string)
}