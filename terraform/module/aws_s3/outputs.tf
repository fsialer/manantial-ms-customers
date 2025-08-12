output "doc_customer_bucket_summary" {
  description = "Resumen del bucket creado."
  value = {
    bucket_name = aws_s3_bucket.customer_bucket.bucket
    tags  = aws_s3_bucket.customer_bucket.tags
    environment = terraform.workspace
  }
}

output "bucket_id" {
  description = "Id del bucket"
  value = aws_s3_bucket.customer_bucket.id
}