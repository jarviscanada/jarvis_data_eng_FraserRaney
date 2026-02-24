# Use the official inbound-agent base so the Kubernetes plugin can connect
FROM jenkins/inbound-agent:latest

# Run as root to install tooling
USER root

# Install Azure CLI
RUN apt-get update && apt-get install -y curl apt-transport-https lsb-release gnupg \
 && curl -sL https://aka.ms/InstallAzureCLIDeb | bash

# Install kubectl (latest stable)
RUN curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl" \
 && install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl

# Switch back to jenkins user
USER jenkins