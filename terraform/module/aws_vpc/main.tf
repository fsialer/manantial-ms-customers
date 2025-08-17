terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

resource "aws_vpc" "vpc_custom" {
  cidr_block           = var.cidr
  enable_dns_support   = true
  enable_dns_hostnames = true
  instance_tenancy     = "default"

  tags = merge(var.tags, {
    Name = "manantial-vpc"
  })
}

resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.vpc_custom.id

  tags = merge(var.tags, {
    Name = "manantial-igw"
  })
}

resource "aws_subnet" "private" {
  for_each          = var.private_subnet
  vpc_id            = aws_vpc.vpc_custom.id
  cidr_block        = each.value.cidr
  availability_zone = each.value.availability_zone

  tags = merge(var.tags, {
    Name = "manantial-private-subnet-${each.key}"
    "kubernetes.io/role/internal-elb" = "1"
    "kubernetes.io/cluster/manantial-eks-cluster"    = "shared"
  })
}

resource "aws_subnet" "public" {
  for_each          = var.public_subnet
  vpc_id            = aws_vpc.vpc_custom.id
  cidr_block        = each.value.cidr
  availability_zone = each.value.availability_zone

  tags = merge(var.tags, {
    Name = "manantial-public-subnet-${each.key}"
  })
}

# NAT Gateway per AZ
resource "aws_eip" "nat" {
  for_each = var.public_subnet
  tags = merge(var.tags,{
    Name = "manantial-nat-eip-${each.key}"
  })
}

resource "aws_nat_gateway" "nat" {
  for_each = var.private_subnet
  allocation_id = aws_eip.nat[each.key].id
  subnet_id = aws_subnet.private[each.key].id
  tags = merge(var.tags, {
    Name = "manantial-nat-gateway-${each.key}"
  })
  depends_on = [aws_internet_gateway.igw]
}

#
# # Route table for public subnets
resource "aws_route_table" "private" {
  for_each = var.private_subnet
  vpc_id = aws_vpc.vpc_custom.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_nat_gateway.nat[each.key].id
  }

  tags = merge(var.tags,{
    Name = "manantial-private-rt-${each.key}"
  })
}

resource "aws_route_table_association" "private" {
  for_each       = var.public_subnet
  subnet_id      = aws_subnet.private[each.key].id
  route_table_id = aws_route_table.private[each.key].id
}

