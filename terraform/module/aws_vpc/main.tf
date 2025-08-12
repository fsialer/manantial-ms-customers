resource "aws_vpc" "eks_vpc_custom" {
  cidr_block = var.cidr
  enable_dns_support = true
  enable_dns_hostnames = true
  instance_tenancy = "default"
  tags = var.tags
}

resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.eks_vpc_custom.id
  tags = var.tags
}

# Create private subnets
resource "aws_subnet" "eks_private_subnet" {
  for_each = var.private_subnet
  vpc_id = aws_vpc.eks_vpc_custom.id
  cidr_block = each.value.cidr
  availability_zone = each.value.availability_zone
}


# Create public subnets
resource "aws_subnet" "eks_public_subnet" {
  for_each = var.public_subnet
  vpc_id = aws_vpc.eks_vpc_custom.id
  cidr_block = each.value.cidr
  availability_zone = each.value.availability_zone
  map_public_ip_on_launch = true
}



# NAT Gateway per AZ
resource "aws_eip" "nat" {
  for_each = var.public_subnet
  tags = var.tags

}

resource "aws_nat_gateway" "nat" {
  for_each = var.public_subnet
  allocation_id = aws_eip.nat[each.key].id
  subnet_id = aws_subnet.eks_public_subnet[each.key].id
  tags = var.tags
  depends_on = [aws_internet_gateway.igw]
}


# Route table for public subnets
resource "aws_route_table" "public" {
  vpc_id = aws_vpc.eks_vpc_custom.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }

  tags = {
    Name = "eks-public-rt"
  }
}

resource "aws_route_table_association" "public" {
  for_each       = var.public_subnet
  subnet_id      = aws_subnet.eks_public_subnet[each.key].id
  route_table_id = aws_route_table.public.id
}

# Route tables for private subnets
resource "aws_route_table" "private" {
  for_each = var.private_subnet

  vpc_id = aws_vpc.eks_vpc_custom.id

  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.nat[each.key].id
  }

  tags = {
    Name = "eks-private-rt-${each.key}"
  }
}

resource "aws_route_table_association" "private" {
  for_each       = var.private_subnet
  subnet_id      = aws_subnet.eks_private_subnet[each.key].id
  route_table_id = aws_route_table.private[each.key].id
}

