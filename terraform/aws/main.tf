#######################################
# Módulo de s3 custom
#######################################
module "aws_s3_bucket" {
  source      = "../module/aws_s3"
  bucket_name = "${var.s3_bucket_name}-${terraform.workspace}"
  tags        = var.tags
}
#######################################
# Módulo de VPC oficial
#######################################

module "aws_vpc" {
  source         = "../module/aws_vpc"
  cidr           = var.vpc_cidr
  private_subnet = var.vpc_private_subnet
  public_subnet  = var.vpc_public_subnet
  tags           = var.tags
}

#######################################
# Módulo de EKS oficial
#######################################

module "aws_eks" {
  source            = "../module/aws_eks"
  tags              = var.tags
  cluster_name      = "${var.eks_cluster_name}-${terraform.workspace}"
  private_subnets   = module.aws_vpc.private_subnets
  iam_role_name     = "${var.eks_iam_role_name}-${terraform.workspace}"
  types_instance     = var.eks_types_instance
  type_ami          = var.eks_type_ami
  update_config_max = var.eks_update_config_max
  scaling_desired   = var.eks_scaling_desired
  scaling_max       = var.eks_scaling_max
  scaling_min       = var.eks_scaling_min
}