output "vpc_general" {
  description = "Resumen de vpc"
  value = {
    vpc_id = aws_vpc.vpc_custom.id
    cidr = aws_vpc.vpc_custom.cidr_block
    instance_tenancy = aws_vpc.vpc_custom.instance_tenancy
    tags = aws_vpc.vpc_custom.tags
  }
}

output "private_subnets" {
  description = "Listado de las subredes privadas"
  value = [for s in aws_subnet.private : s.id]
}

output "public_subnets" {
  description = "Listado las subredes publicas"
  value = [ for s in aws_subnet.public : s.id]
}