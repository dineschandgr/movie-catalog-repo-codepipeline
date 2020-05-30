# movie-catalog-repo-codepipeline
AWS Codepipeline for movie-catalog microservice

1. When the code is commited to github, codebuild retrieves the source and builds it using mvn clean package and generates the latest jar file
2. The CodeBuild then uses the DockerFile to download java image and packages Java images with the springboot jar file to create a Movie-Catalog-Image 
3. Then CodeBuild pushes the newly built docker images to AWS ECR
4. Code Deploy deploys the latest docker images to AWS ECS by updating the existing task definition to latest version
5. Then the existing service is stopped and a new task is started with the latest task definition
6. Now the newly running service task has the latest code change reflected

Create a AWS CodePipeline With the following stages

1. Source (repo from github)
2. Build using AWS CodeBuild with the following environment variables specified
    
    1. CONTAINER_NAME -	movie-catalog-image	
    2. AWS_DEFAULT_REGION -	ap-southeast-1	
    3. REPOSITORY_URI	- ECR Repository URI

3. Deploy to AWS ECS with the following properties specified in appspec.yml file

     1. TaskDefinition: Task Definition ARN from AWS Fargate
     2. Container Name
     3. Container Port

Select elevated privileges for AWS CodeBuild project to push docker images
Provide AWS CodeBuild Role with access to ECR so that CodeBuild builds the docker images and pushes it to AWS ECR
