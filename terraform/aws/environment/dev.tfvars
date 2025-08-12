region ="us-east-1"

tags = {
  "project" : "manantial"
  "environment" : "dev"
}

s3_bucket_name = "manantial-bucket"

vpc_private_subnet = {
  subnet1 = {
      "cidr" : "10.1.1.0/24"
      "availability_zone" : "us-east-1a"
  }
}

vpc_public_subnet = {
  subnet1 = {
    "cidr" : "10.2.1.0/24"
    "availability_zone" : "us-east-1a"
  }
}

eks_cluster_name="manantial_cluster"
eks_iam_role_name = "manantial_eks_role"
eks_types_instance = [
  "t3.micro"
]
eks_type_ami = "AL2_x86_64"
eks_update_config_max = 1
eks_scaling_desired = 1
eks_scaling_max = 1
eks_scaling_min = 1