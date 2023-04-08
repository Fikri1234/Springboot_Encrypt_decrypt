# encryption-utility

Create encrypt & decrypt with Springboot

**Prerequisites**
1. Dev Tools
2. Lombok
3. Bouncy Castle

## Getting Started
The application will have the following encryption following:
1. DES/Triple DES [Encrypt - Decrypt]
2. AES (128, 192, 256) [Encrypt - Decrypt]
3. RSA (512, 1024, 2048, 4096) [Encrypt - Decrypt]
4. Blowfish [Encrypt - Decrypt]
5. Twofish [Encrypt - Decrypt]
6. MD5 [Encrypt]
7. SHA-2 (224, 256, 384, 512, 512/224, 512/256) [Encrypt]
8. SHA-3 (224, 256, 384, 512) [Encrypt]
9. GOST [Encrypt - Decrypt]

## Sonarqube
Install sonarqube on docker
```
docker pull sonarqube
```
Do this following step
1. Open http://localhost:9000/ and create project manually
   ![Alt text](asset/1.JPG?raw=true "Create project")
2. Create your desire name
   ![Alt text](asset/2.JPG?raw=true "Project name")
3. In this case, will run on local side
   ![Alt text](asset/3.JPG?raw=true "Run server")
4. Setup the expiry token
   ![Alt text](asset/4.JPG?raw=true "Token expired")
5. Since running on maven,copy and open cmd on project directory,then paste the script with a bit modify to add
   ```
   mvn sonar:sonar  -Dsonar.projectKey=encrypt-decrypt  -Dsonar.projectName='encrypy-decrypt'   -Dsonar.host.url=http://localhost:9000  -Dsonar.token=sqp_536bd65f5049002ef75534e84bee369f59f73842 -Dsonar.java.binaries=target/classes
   ```
   ![Alt text](asset/5.JPG?raw=true "Command")
6. Let's analyze the project
   ![Alt text](asset/6.JPG?raw=true "Analyze")
7. Make the code as clean as possible
   ![Alt text](asset/7.JPG?raw=true "Clearance")


## Deployment Strategy with Docker
Open cmd on your project directory

### Compose up

```
docker-compose up --build
```