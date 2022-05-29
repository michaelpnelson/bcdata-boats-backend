# bcdata-boats-backend

This is a REST API providing a backend server to track boats and their statuses. It maintains an in-memory store of Boat instances and can create, retrieve, update, delete, list, and clear all Boat elements. 

Boat has the properties:

* *id* Long
* *name* String
* *status* String
## REST Endpoints

### /boat

POST

Create a Boat.

Sample request body:
```
{"id":3, "name": "Sailin' Away", "status": "Outbound"}
```

PUT

Update a Boat. The request body is also a Boat, as in the sample above. The id must be supplied. All properties are updated.
### /boat/{id}

GET

Get the Boat having the same id. 

DELETE

Remove the Boat having the same id.

### /boats

GET

Get all boats.

DELETE

Clear all boats.