#!/bin/bash
PROJECT_ID=1f3e6919-579f-400b-9dab-a6dafaaaafa7
PROJECT_NAME=soat7myburger

doctl vpcs list
# ID                                      URN                                            Name                 Description    IP Range          Region 
# eb696938-4f6a-46e6-9bae-3e0f63f94fee    do:vpc:eb696938-4f6a-46e6-9bae-3e0f63f94fee    soat7myburger-vpc                   10.108.16.0/20    nyc3

# Create K8S Cluster - soat7myburger-k8s
echo "Creating K8s Cluster on $PROJECT_NAME-vpc"
doctl kubernetes cluster create $PROJECT_NAME-k8s --node-pool "name=$PROJECT_NAME-node-pool;size=s-2vcpu-4gb;count=1;auto-scale=true;min-nodes=1;max-nodes=3" --region nyc3 --vpc-uuid eb696938-4f6a-46e6-9bae-3e0f63f94fee

# List K8S Clusteres
# doctl kubernetes cluster list

doctl kubernetes cluster get $PROJECT_NAME-k8s

DO_CLUSTER_ID=`doctl kubernetes cluster get $PROJECT_NAME-k8s | tail -1 | cut -d ' ' -f1`

# Project Myburger UUID = 1f3e6919-579f-400b-9dab-a6dafaaaafa7
echo "Cluster ID = $DO_CLUSTER_ID"

echo "Assign K8s Cluster (ID=$DO_CLUSTER_ID) with Myburger Project (ID=1f3e6919-579f-400b-9dab-a6dafaaaafa7)"
doctl projects resources assign 1f3e6919-579f-400b-9dab-a6dafaaaafa7 --resource="do:kubernetes:$DO_CLUSTER_ID"

kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml