output "vpc_general" {
  description = "Resumen de vpc"
  value = {
    vpc_id = aws_vpc.eks_vpc_custom.id
    cidr = aws_vpc.eks_vpc_custom.cidr_block
    instance_tenancy = aws_vpc.eks_vpc_custom.instance_tenancy
    tags = aws_vpc.eks_vpc_custom.tags
  }
}

output "private_subnets" {
  description = "Listado de las subredes privadas"
  value = [for s in aws_subnet.eks_private_subnet : s.id]
}

output "public_subnets" {
  description = "Listado las subredes publicas"
  value = [ for s in aws_subnet.eks_public_subnet : s.id]
}