output "eks_info" {
  description = "Resumen de eks"
  value = {
    eks_id =aws_eks_cluster.eks_cluster.id
    cluster = aws_eks_cluster.eks_cluster.name
  }
}