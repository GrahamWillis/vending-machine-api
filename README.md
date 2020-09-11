# Vending Machine API
This is an exercise.

This is a simple API written in Java using Spring Boot. It has no persistence. The operation endpoints are defined in the 
ActionsController class and this makes calls on a service class Machine.

The model is minimal and mixes message representation classes with base entities - in a real application these would
probably be separate.   

The sterling denominations are represented by the class Coinage.

There is not much in the way of automated tests due to time constraints. There is a single integration test and no unit tests.

## Installation
(1) Install java version 11
(2) Clone this repository
(2) cd ./vending-machine-api
(3) ./mvnw spring-boot:run

## Operation
Test with curl or similar

(1) First establish a 'float' using the following post command. This is cumulative.
curl -d '{	
    "FIVE_POUND": 5,
    "TWO_POUND": 20,
    "ONE_POUND": 20,
    "FIFTY_PENCE": 20,
    "TWENTY_PENCE": 20,
    "TEN_PENCE": 20,
    "FIVE_PENCE": 20,
    "TWO_PENCE": 20,
    "ONE_PENCE": 20
}' -H "Content-Type: application/json" -X POST http://localhost:8080/float

This should return an integer value of the total amount stored in the machine, in pence.

(2) At any time the machine ammount can be retrieved
curl http://localhost:8080/float

(3) The use adds some coins to cover the purchase of an item at a specific price.
For example if the user inserts two pund coins and 4 fifty pence coins to for an item costing £3.73 

curl -d '{
	"coinsInserted": {
		"ONE_POUND": 2,
	    "FIFTY_PENCE": 4
	},
	"forAmount": 373
}'  -H "Content-Type: application/json" -X POST http://localhost:8080/insert

This should respond with {"change":{"TWENTY_PENCE":1,"FIVE_PENCE":1,"TWO_PENCE":1},"message":"Returning change"}

(4) If the coins inserted do not cover the cost
curl -d '{
	"coinsInserted": {
		"ONE_POUND": 1,
	    "FIFTY_PENCE": 4
	},
	"forAmount": 373
}'  -H "Content-Type: application/json" -X POST http://localhost:8080/insert

{"change":null,"message":"Payment insufficient"}

(4) If the value exceeds that in the machine
curl -d '{
	"coinsInserted": {
		"ONE_POUND": 10000
	},
	"forAmount": 1000000
}'  -H "Content-Type: application/json" -X POST http://localhost:8080/insert

{"change":null,"message":"Insufficient funds in machine"}

(4) If the coinage available in the machine cannot give the exact change:
Restart the server

curl -d '{	
    "FIVE_POUND": 1
}' -H "Content-Type: application/json" -X POST http://localhost:8080/float

{"change":{"FIFTY_PENCE":6},"message":"Insufficient coinage to give correct change"}

Notes:

If the machine has an insufficient stock of any given denomination that is required in for chain it moves on and issues the change 
in the next largest denomination. The machine could be improved by partially issuing from the available denomination.  

In a real vending machine the change for a transaction would first attempted to be dispensed from the coins the user inserted 
and then the machine stock. This would prevent users from using the machine to 'change up' coinage.


