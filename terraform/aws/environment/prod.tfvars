region ="us-east-1"

tags = {
  "project" : "manantial"
  "environment" : "prod"
}

s3_bucket_name = "manantial-bucket"

vpc_private_subnet = {
  subnet1 = {
    "cidr" : "10.3.1.0/24"
    "availability_zone" : "us-east-1a"
  },
  subnet2 = {
    "cidr" : "10.3.2.0/24"
    "availability_zone" : "us-east-1b"
  },
  subnet3 = {
    "cidr" : "10.3.3.0/24"
    "availability_zone" : "us-east-1c"
  }
}

vpc_public_subnet = {
  subnet1 = {
    "cidr" : "10.3.1.0/24"
    "availability_zone" : "us-east-1a"
  },
  subnet2 = {
    "cidr" : "10.3.2.0/24"
    "availability_zone" : "us-east-1b"
  },
  subnet3 = {
    "cidr" : "10.3.3.0/24"
    "availability_zone" : "us-east-1c"
  }
}

ssh_key_name = "my-keypair"

eks_cluster_name="manantial_cluster"
eks_iam_role_name = "manantial_eks_role"
eks_types_instance = [
  "t3.medium"
]
eks_type_ami = "AL2_x86_64"
eks_update_config_max = 1
eks_scaling_desired = 1
eks_scaling_max = 1
eks_scaling_min = 1