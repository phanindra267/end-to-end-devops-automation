resource "aws_iam_user" "jenkins" {
  name = "jenkins-ci-user"
  path = "/system/"
}

resource "aws_iam_access_key" "jenkins" {
  user = aws_iam_user.jenkins.name
}

resource "aws_iam_user_policy" "jenkins_policy" {
  name = "jenkins-multi-access"
  user = aws_iam_user.jenkins.name

  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "ecr:*",
                "eks:*",
                "ec2:*",
                "iam:*",
                "logs:*",
                "cloudwatch:*"
            ],
            "Resource": "*"
        }
    ]
}
EOF
}

output "jenkins_access_key_id" {
  value = aws_iam_access_key.jenkins.id
}

output "jenkins_secret_access_key" {
  value     = aws_iam_access_key.jenkins.secret
  sensitive = true
}
