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

resource "aws_s3_bucket_versioning" "customer_bucket_versioning" {
  bucket = aws_s3_bucket.customer_bucket.id
  versioning_configuration {
    status = "Enabled"
  }
}