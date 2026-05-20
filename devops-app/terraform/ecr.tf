resource "aws_ecr_repository" "app_repo" {
  name                 = "devops-app"
  image_tag_mutability = "MUTABLE"

  image_scanning_configuration {
    scan_on_push = true
  }

  tags = {
    Environment = "production"
  }
}

output "ecr_repository_url" {
  value = aws_ecr_repository.app_repo.repository_url
}
