# Using Kotlin in a Serverless Architecture with AWS Lambda
Complete code per chapter for the complete my complete article in medium https://medium.com/@juancho088/using-kotlin-in-a-serverless-architecture-with-aws-lambda-part-1-setting-up-the-project-87033790e2f4

# First Steps

1. Install nodeJS (download [link](https://nodejs.org/en/download/))
2. Run the command `npm install -g serverless`
3. Create your project running the command `serverless create —-template aws-kotlin-jvm-gradle --path your_service`
4. To configure your AWS credentials execute `serverless config credentials --provider aws --key EXAMPLE --secret EXAMPLEKEY`