terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

resource "aws_iam_role" "eks_cluster_role" {
  name = "${var.iam_role_name}-${terraform.workspace}"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Principal = {
          Service = "eks.amazonws.com"
        }
        Action = [
          "sts:AssumeRole",
          "sts:TagSession"
        ]
      }
    ]
  })
  tags = var.tags
}

resource "aws_iam_role_policy_attachment" "eks_cluster_policy" {
  role       = aws_iam_role.eks_cluster_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSClusterPolicy"
}

resource "aws_iam_role" "eks_node_group_role" {
  name = "eksNodeGroupRole"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect = "Allow"
      Principal = {
        Service = "ec2.amazonaws.com"
      }
      Action = ["sts:AssumeRole"]
    }]
  })
  tags = var.tags
}

resource "aws_iam_role_policy_attachment" "eks-_worker_node_policy" {
  role       = aws_iam_role.eks_cluster_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSWorkerNodePolicy"
}

resource "aws_iam_role_policy_attachment" "eks_cn1_policy" {
  role       = aws_iam_role.eks_cluster_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKS_CNI_Policy"
}

resource "aws_iam_role_policy_attachment" "ec2_container_registry_read_only" {
  role       = aws_iam_role.eks_cluster_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly"
}

resource "aws_security_group" "eks_node_sg" {
  name        = "eks-node-sg"
  description = "Security group for EKS worker nodes"
  vpc_id      = var.vpc_id

  ingress {
    description      = "Allow all traffic from within the security group"
    from_port        = 0
    to_port          = 0
    protocol         = "-1"
    self             = true
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = merge(var.tags, {
    Name = "eks-node-sg"
  })
}

resource "aws_eks_cluster" "eks_cluster" {
  name     = var.cluster_name
  role_arn = aws_iam_role.eks_cluster_role.arn
  # access_config {
  #   authentication_mode = "API"
  # }
  version = "1.31"
  vpc_config {
    subnet_ids = var.private_subnets
    endpoint_private_access = true
    endpoint_public_access = true
    #public_access_cidrs = var.public_subnets
  }

  # 🔐 FALTA ESTE BLOQUE
  # encryption_config {
  #   provider {
  #     key_arn = aws_kms_key.eks_secrets.arn
  #   }
  #   resources = ["secrets"]
  # }

  depends_on = [
    aws_iam_role_policy_attachment.eks_cluster_policy
  ]
  tags = merge(var.tags,
    {
      Name = var.cluster_name
    }
  )
}

resource "aws_eks_node_group" "node_group" {
  cluster_name  = aws_eks_cluster.eks_cluster.name
  node_group_name = "manantial-ng"
  node_role_arn = aws_iam_role.eks_node_group_role.arn
  subnet_ids = var.private_subnets

  scaling_config {
    desired_size = var.scaling_desired
    max_size     = var.scaling_max
    min_size     = var.scaling_min
  }

  instance_types = var.types_instance
  ami_type = var.type_ami
  tags = var.tags
  disk_size = 20

  remote_access {
    ec2_ssh_key               = var.ssh_key_name # Cambia esto por tu par de claves EC2
    source_security_group_ids = [aws_security_group.eks_node_sg.id]
  }

  update_config {
    max_unavailable = var.update_config_max
  }

  # Ensure that IAM Role permissions are created before and deleted after EKS Node Group handling.
  # Otherwise, EKS will not be able to properly delete EC2 Instances and Elastic Network Interfaces.
  depends_on = [
    aws_iam_role_policy_attachment.eks-_worker_node_policy,
    aws_iam_role_policy_attachment.eks_cn1_policy,
    aws_iam_role_policy_attachment.ec2_container_registry_read_only,
  ]
}