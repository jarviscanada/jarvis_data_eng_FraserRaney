# Introduction
This project demonstrates a cloud-native trading application deployed on Azure Kubernetes Service (AKS) with a fully automated CI/CD pipeline using Jenkins. It was designed with separate development and production Kubernetes clusters to support safe iteration and controlled releases. Azure Container Registry (ACR) is used for the container images.

The system is designed with:
- Azure Kubernetes Service (AKS)
- Azure Container Registry (ACR)
- Jenkins (running inside Kubernetes)
- Helm (for Kubernetes packaging)
- PostgreSQL database
- Jenkins credentials

The `aks` folder contains the Kubernetes manifests for AKS.

The Jenkins pipelines (`springboot/Jenkinsfile-dev` and `springboot/Jenkinsfile-prod`):
1. Authenticate to Azure using a Service Principal
2. Build Docker image using Azure ACR build 
3. Push image to Azure Container Registry 
4. Retrieve AKS credentials 
5. Update Kubernetes deployment image 
6. Wait for rollout to complete

# Application Architecture
The application consists of:
- Azure Load Balancer (exposes service externally)
- Kubernetes Deployment (Spring Boot application)
- PostgreSQL database pod
- Azure Container Registry (ACR) for container images
- Jenkins for CI/CD automation
## AKS Deployment Diagram
![AKS Deployment](./assets/aks-deployment.png)


# Jenkins CI/CD pipeline

## Pipeline Stages
### Init Stage
- Logs into Azure using Service Principal
- Validates Azure CLI
```bash
az login --service-principal --username $AZ_USER --password $AZ_PWD --tenant $AZ_TENANT
```
### Build & Push Stage
- Uses Azure Container Registry build
- Builds Spring Boot image
- Tags image using `${BUILD_NUMBER}`
- Pushes image directly to ACR
```bash
az acr build --image ${IMAGE_NAME} --registry ${ACR_NAME} --file springboot/Dockerfile springboot
```
### Deploy Stage
- Fetches AKS credentials
- Updates Kubernetes deployment image
- Waits for rollout

#### From Azure CLI:
```bash
az aks get-credentials \
  --resource-group ${RESOURCE_GROUP} \
  --name ${CLUSTER_NAME} \
  --file /home/jenkins/.kube/config
```
#### and then using kubectl
```bash
export KUBECONFIG=/home/jenkins/.kube/config
kubectl set image deployment/trading trading=${ACR_LOGIN}/${IMAGE_NAME}
kubectl rollout status deployment/trading
```

## CI/CD Pipeline Diagram
![CI/CD](./assets/ci-cd.png)

# Improvements
- Add Horizontal Pod Autoscaler
- Add Monitoring & Logging, e.g., Azure Monitor
- Implement Helm Charts for deployment/trading