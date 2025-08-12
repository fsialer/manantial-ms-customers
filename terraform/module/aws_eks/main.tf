resource "aws_eks_cluster" "eks_cluster" {
  name     = var.cluster_name
  access_config {
    authentication_mode = "API"
  }
  role_arn = aws_iam_role.cluster_role.arn
  version = "1.31"
  vpc_config {
    subnet_ids = var.private_subnets
  }

  depends_on = [
    aws_iam_role_policy_attachment.cluster_AmazonEKSClusterPolicy
  ]
  tags = var.tags
}

resource "aws_iam_role" "cluster_role" {
  name = "${var.iam_role_name}-${terraform.workspace}"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = [
          "sts:AssumeRole",
          "sts:TagSession"
        ]
        Effect = "Allow"
        Principal = {
          Service = "eks.amazonws.com"
        }
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "cluster_AmazonEKSClusterPolicy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSClusterPolicy"
  role       = aws_iam_role.cluster_role.name
}

resource "aws_iam_role_policy_attachment" "example-AmazonEKSWorkerNodePolicy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSWorkerNodePolicy"
  role       = aws_iam_role.cluster_role.name
}

resource "aws_iam_role_policy_attachment" "example-AmazonEKS_CNI_Policy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKS_CNI_Policy"
  role       = aws_iam_role.cluster_role.name
}

resource "aws_iam_role_policy_attachment" "example-AmazonEC2ContainerRegistryReadOnly" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly"
  role       = aws_iam_role.cluster_role.name
}

resource "aws_eks_node_group" "nodes_worker" {
  cluster_name  = aws_eks_cluster.eks_cluster.name
  node_group_name = "manantial-node-group"
  node_role_arn = aws_iam_role.cluster_role.arn
  subnet_ids = var.private_subnets

  scaling_config {
    desired_size = var.scaling_desired
    max_size     = var.scaling_max
    min_size     = var.scaling_min
  }

  instance_types = var.types_instance
  ami_type = var.type_ami
  tags = var.tags

  update_config {
    max_unavailable = var.update_config_max
  }

  # Ensure that IAM Role permissions are created before and deleted after EKS Node Group handling.
  # Otherwise, EKS will not be able to properly delete EC2 Instances and Elastic Network Interfaces.
  depends_on = [
    aws_iam_role_policy_attachment.example-AmazonEKSWorkerNodePolicy,
    aws_iam_role_policy_attachment.example-AmazonEKS_CNI_Policy,
    aws_iam_role_policy_attachment.example-AmazonEC2ContainerRegistryReadOnly,
  ]
}