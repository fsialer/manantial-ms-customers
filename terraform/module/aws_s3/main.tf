terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

resource "aws_s3_bucket" "customer_bucket" {
  bucket = "${var.bucket_name}-${terraform.workspace}"
  force_destroy = true
  tags = var.tags
}

resource "aws_s3_bucket_public_access_block" "customer_bucket_block" {
  bucket = aws_s3_bucket.customer_bucket.id
  block_public_acls = true
  block_public_policy = true
  ignore_public_acls = true
  restrict_public_buckets = true
}

# resource "aws_s3_bucket_logging" "customer_bucket_logging" {
#   bucket = aws_s3_bucket.customer_bucket.id
#
#   #target_bucket = var.logging_bucket_name   # Un bucket separado para logs
#   target_bucket = "backet_log"   # Un bucket separado para logs
#   target_prefix = "logs/${terraform.workspace}/"
# }

resource "aws_s3_bucket_versioning" "customer_bucket_versioning" {
  bucket = aws_s3_bucket.customer_bucket.id
  versioning_configuration {
    status = "Enabled"
#    mfa_delete = "Enabled" # Solo reflejado en el estado, NO aplicado por Terraform
  }
}