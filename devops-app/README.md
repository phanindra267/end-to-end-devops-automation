# 🚀 Ultimate Cloud-Native DevOps Pipeline

A production-ready, industry-standard DevOps ecosystem. This project demonstrates a complete lifecycle: Infrastructure as Code (Terraform), CI/CD Automation (Jenkins), Containerization (Docker), Orchestration (Kubernetes), and Full-Stack Observability (Prometheus/Grafana).

---

## 🏗️ System Architecture

```mermaid
graph TD
    subgraph Local Development
        Dev[Developer] -->|Git Push| GitHub[GitHub Repository]
    end

    subgraph CI/CD Layer (Jenkins)
        Jenkins[Jenkins Server :9090] -->|Maven Build| JAR[Spring Boot JAR]
        JAR -->|Dockerize| Image[Docker Image]
        Image -->|Push| ECR[AWS ECR]
        Jenkins -->|Provision| TF[Terraform]
        Jenkins -->|Deploy| EKS[AWS EKS / Minikube]
    end

    subgraph Cloud Infrastructure (AWS)
        subgraph Networking
            VPC[AWS VPC] --> Subnets[Public/Private Subnets]
        end
        subgraph Orchestration
            EKS --> Pods[Spring Boot Pods x2]
            Pods --> LB[Load Balancer]
        end
    end

    subgraph Observability
        Pods --> Prom[Prometheus]
        Prom --> Graf[Grafana Dashboards]
        Pods --> CW[CloudWatch Logs]
    end
```

---

## 🧱 Technology Stack
| Category | Technology |
| :--- | :--- |
| **Backend** | Java 21, Spring Boot 3.x, Maven |
| **Infrastructure** | Terraform, AWS (VPC, EKS, ECR, IAM, CloudWatch) |
| **Containers** | Docker (Multi-stage, Optimized) |
| **Orchestration** | Kubernetes (Minikube & EKS-ready) |
| **CI/CD** | Jenkins (Declarative Pipeline) |
| **Monitoring** | Prometheus, Grafana |
| **Security** | DevSecOps (Least Privilege, Security Contexts) |

---

## 📂 Project Structure
```text
devops-app/
├── src/                # Spring Boot REST API (Health, Hello, Info)
├── terraform/          # IaC: VPC, EKS, ECR, IAM setup
├── kubernetes/         # K8s: Deployment, Service, LB, Secrets
├── monitoring/         # Metrics: Prometheus & Grafana Manifests
├── Dockerfile          # Optimized multi-stage build
├── Jenkinsfile         # Full CI/CD Pipeline
└── README.md           # This comprehensive guide
```

---

## 📘 Unit I: Cloud Computing & Git Lifecycle
### Cloud Service Models
1.  **IaaS (Infrastructure as a Service)**: Raw computing power, storage, and networking (e.g., AWS EC2).
2.  **PaaS (Platform as a Service)**: Platforms for developers to build apps without managing infrastructure (e.g., Elastic Beanstalk).
3.  **SaaS (Software as a Service)**: Fully managed software delivered over the web (e.g., Google Drive).

### Git Workflow
- `git init` - Initialize local repo.
- `git add .` - Stage changes.
- `git commit -m "feat: setup eks"` - Snapshot changes.
- `git push origin main` - Send to GitHub.

---

## 📘 Unit II: Docker & Kubernetes (K8s)
### Docker Containerization
We use a **Multi-Stage Build** to keep images small (<150MB):
- **Stage 1 (Build)**: Compiles code using Maven 3.9.
- **Stage 2 (Runtime)**: Runs the JAR using a lightweight JRE 21.

### Kubernetes Orchestration
- **Deployment**: Manages 2 replicas of the app. If one fails, K8s restarts it automatically (Self-healing).
- **Service (NodePort)**: Exposes the app locally on port 30001.
- **Service (LoadBalancer)**: Provisions an AWS ELB for cloud access.

---

## 📘 Unit III: Infrastructure as Code (Terraform)
The `terraform/` directory contains:
- `vpc.tf`: Provisions a custom network with 2 public subnets.
- `ecr.tf`: Creates a private repository for Docker images.
- `eks.tf`: Sets up a managed Kubernetes cluster with auto-scaling node groups.
- `iam.tf`: Implements **Least Privilege** policies for Jenkins and the EKS nodes.

---

## 📘 Unit IV: Jenkins CI/CD Pipeline
Access Jenkins on port `9090`. The `Jenkinsfile` automates:
1.  **Terraform Plan**: Validates AWS infrastructure.
2.  **Maven Build**: Packages the Spring Boot app.
3.  **Docker Push**: Authenticates with AWS and pushes to ECR.
4.  **K8s Deploy**: Updates the EKS cluster with the new image.

---

## 📘 Unit V: Monitoring & Security
### Monitoring (Prometheus + Grafana)
- **Prometheus**: Scrapes metrics from `/actuator/prometheus`.
- **Grafana**: Visualizes CPU, Memory, and Request counts (Port 32000).

### Security (DevSecOps)
- **Pod Security**: Pods run as a non-root user (UID 1000).
- **IAM**: Jenkins user has specific permissions (no root access).
- **Secrets**: K8s Secrets used for sensitive data.

---

## 🏗️ Step-by-Step Implementation Guide

This project follows a logical 6-step progression to build a complete DevOps ecosystem.

### Step 1: Develop the Spring Boot API
- **Action**: Create a Java 21 REST API with Spring Web and Actuator.
- **Why**: To provide a functional "Cloud-Native" service that exposes endpoints for both users (`/hello`) and monitoring tools (`/actuator/prometheus`).
- **Details**: We use a `Controller-Service-Model` architecture for clean code separation.

### Step 2: Containerize with Docker
- **Action**: Create a `Dockerfile` using a **multi-stage build**.
- **Why**: To ensure the application runs identically in any environment. Multi-stage builds keep the final image small by excluding build tools (Maven) and only including the runtime (JRE).
- **Details**: We use `eclipse-temurin:21` as the base image for modern Java support.

### Step 3: Provision Infrastructure with Terraform (IaC)
- **Action**: Write Terraform scripts to create AWS resources (VPC, EKS, ECR).
- **Why**: To avoid "manual clicking" in the AWS console. This ensures infrastructure is versioned, repeatable, and scalable.
- **Details**: 
    - `ecr.tf` creates the registry for our images.
    - `eks.tf` creates the cluster where our app will live.

### Step 4: Automate with Jenkins (CI/CD)
- **Action**: Create a `Jenkinsfile` to define the pipeline.
- **Why**: To automate the "Life of a Commit." Every code change is automatically built, tested, and deployed without human intervention.
- **Details**: The pipeline handles Terraform initialization, Docker image tagging, and `kubectl` deployment.

### Step 5: Deploy to Kubernetes
- **Action**: Create YAML manifests for `Deployment` and `Service`.
- **Why**: To manage the application's lifecycle. Kubernetes ensures we always have 2 replicas running and provides a `LoadBalancer` to handle external traffic.
- **Details**: We use `RollingUpdate` strategy to ensure zero-downtime deployments.

### Step 6: Setup Monitoring & Observability
- **Action**: Deploy Prometheus and Grafana.
- **Why**: To gain visibility. We need to know if the app is slow or crashing. Prometheus "scrapes" metrics, and Grafana "visualizes" them on a dashboard.

---

## 🚀 Setup & Execution Guide

### 1. Local Development (Minikube)
```bash
# Start Minikube
minikube start --driver=docker

# Build & Load Image
docker build -t devops-app:v2 .
minikube image load devops-app:v2

# Deploy
kubectl apply -f kubernetes/
kubectl apply -n monitoring -f monitoring/

# Access App
minikube service devops-app-service --url
```

### 2. Cloud Deployment (AWS EKS)
```bash
# Configure AWS CLI
aws configure

# Provision Infrastructure
cd terraform
terraform init
terraform apply -auto-approve

# Connect to Cluster
aws eks update-kubeconfig --name devops-eks-cluster --region us-east-1

# Deploy
kubectl apply -f kubernetes/
```

### 3. API Endpoints
- `GET /health` : System status
- `GET /hello` : Greeting message
- `GET /info` : App version and environment info
- `GET /actuator/prometheus` : Raw metrics for Prometheus

---

## 🎓 Curriculum Alignment (Units I-VI)
This project is designed to fulfill the requirements of a modern Cloud & DevOps curriculum:

### Unit I: Fundamentals
- **Cloud Overview**: Detailed explanation of IaaS, PaaS, SaaS in README.
- **Git Basics**: demonstrated through repository lifecycle and Jenkins integration.

### Unit II: Virtualization & Containerization
- **Docker**: Implementation of multi-stage Docker builds and architecture.
- **Kubernetes**: Full orchestration setup with Pods, Deployments, and Services.

### Unit III: IaC & Cloud Services
- **Terraform**: Provisioning of AWS VPC, ECR, and EKS clusters.
- **AWS Services**: Direct integration with EC2, EKS, and CloudWatch.

### Unit IV: CI/CD Pipeline
- **Jenkins**: Automated delivery pipeline from code checkout to deployment.
- **Automation**: End-to-end automation of builds, tests, and infra provisioning.

### Unit V: Monitoring & Security
- **Observability**: Real-time metrics visualization with Prometheus and Grafana.
- **Cloud Security**: Implementation of IAM, RBAC, and DevSecOps security contexts.

### Unit VI: Advanced Trends
- **DevSecOps**: Security-by-design principles applied to the entire pipeline.
- **Future Trends**: Discussion on mobile cloud and performance management.

---

## 🛠️ Practical DevOps Tasks (Hands-on)
To fully experience this project, try performing these common DevOps actions:

### 1. Scale the Application
Observe how Kubernetes handles traffic by scaling the pods:
```bash
kubectl scale deployment/devops-app-deployment --replicas=5
kubectl get pods -w
```

### 2. Perform a Rolling Update
Modify the code (e.g., change the greeting in `AppService.java`), rebuild the image as `v3`, and update the deployment:
```bash
# After building v3 image
kubectl set image deployment/devops-app-deployment devops-app=devops-app:v3
kubectl rollout status deployment/devops-app-deployment
```

### 3. Simulate a Failure (Self-Healing)
Delete a running pod and watch Kubernetes automatically recreate it:
```bash
kubectl delete pod <pod-name>
kubectl get pods
```

### 4. Port-Forward for Monitoring
If NodePorts are not accessible, use port-forwarding to view dashboards:
```bash
# View Grafana
kubectl port-forward svc/grafana-service 3000:3000 -n monitoring
```

---

## 🛠️ Troubleshooting
- **Pod ImagePullBackOff**: Ensure the image is loaded into Minikube or pushed to ECR.
- **Terraform 403**: Check if your IAM user has `AdministratorAccess` or correct permissions.
- **Jenkins 9090**: Ensure port 9090 is allowed in your firewall.
