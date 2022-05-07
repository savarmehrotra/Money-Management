# Money Management Application (Spring-boot) 
This Application provides basic functions such as :
1) Account Balance / Info Retreival
2) List of Transactions of Account
2) Money Deposit to Account
3) Money withdrawal From Account  
4) Wrire Transfer Between 2 Accounts

## Running The Application 
Docker is Required as a Prerequisite

Step 1 : (Build and Package)  ```./mvnw clean package```

Step 2 : (Crate a Docker Image) ``` docker build -t "spring-boot:money" .```

Step 3 : (Deploy the Image)  ```docker run -p 8085:8085 spring-boot:money```


## Testing
A Sample Postman Collection File Has been provided. Import the Postman man collection and send requests to the API using Postman.

The Database Already Contains Two Sample Accounts For The Tests and The Account Numbers are as follows :

Account 1 : John Doe , Account No. : ``` "10000" ```, Initial Balance : 7000

Account 2 : Jane Doe, Account No. : ``` "10001" ```, Initial Balance : 5000
