# Courier Tracking Service
This project is a courier tracking service that processes streaming geolocations of couriers, logs when couriers enter a 100-meter radius of Migros stores, avoids counting reentries within one minute, and provides methods to query the total travel distance of couriers.

## Technologies Used
- Spring Boot
- Java 19
- PostgreSQL with PostGIS
- Docker
- Gradle
- Swagger
- Lombok
- Exception Handling
- JUnit & Mockito

## How It Works
The Courier Tracking Service processes and logs the geolocation of couriers to provide insights into their travel patterns and interactions with Migros stores. Here’s a detailed breakdown of how it works:

### Logging Courier Locations:

Couriers' locations are sent to the /courier-locations endpoint via a POST request.
Each location log includes the courier's ID, latitude, longitude, and timestamp.
The data is saved in the courier_location_log table in the PostgreSQL database.

### Distance Calculation:

To calculate the travel distance of a courier, the Haversine formula is used. This formula calculates the distance between two points on the Earth's surface given their latitude and longitude.
The DistanceService contains the logic to compute the distance between consecutive location logs for a given courier.

There are two methods for calculating the total distance:

1. #### Real-time Update:

Every time a new location log is added, the system calculates the distance from the previous log and updates the total distance for the courier. Thus, the total distance information for the courier can be quickly retrieved through a query.

2. #### Batch Calculation:

In case of any system failures or missed updates, the total distance can be recalculated by fetching all location logs of couriers, sorting them by timestamp in reverse order, and summing up the distances between consecutive logs.
As a precaution against any system issues that may prevent real-time updates, the system recalculates the total distance for each courier from the location logs every hour using a scheduled job. This ensures that the distance data remains accurate and up-to-date.

### Store Proximity Detection:

The system checks if a courier enters a 100-meter radius of any Migros store.
The store locations are loaded from the stores.json file.
When a courier logs their location, the system calculates the distance between the courier's current location and each store's location using the Haversine formula.
If the calculated distance is within 100 meters, an entry is made in the store_entrance_log table, recording the event.

### Preventing Duplicate Entries:

To avoid counting reentries within one minute, the system checks the timestamp of the last recorded entry for the same store and courier.
If a courier logs a location within 100 meters of the same store within a minute of the previous entry, the system does not log a new entry.

## How to Run

- Start the services using Docker Compose:

```
docker-compose up --build
```
- Access the application:

The application will be available at 
```
http://localhost:8080 
```
Swagger UI for API documentation and testing: http://localhost:8080/swagger-ui.html.

## Example Request

### Create Courier

```
curl --location 'http://localhost:8080/courier' \
--header 'Content-Type: application/json' \
--data '{
     "username": "mert.agcakoyun"
}'
```
### Get Courier
```
curl --location 'http://localhost:8080/courier/2'
```

### List Couriers
```
curl --location 'http://localhost:8080/courier?page=0&size=10'
```

### Save Location Log
```
curl --location 'http://localhost:8080/courier-location' \
--header 'Content-Type: application/json' \
--data '{
   "courierId":2,
    "lat": 40.9953232,
    "lng": 29.1244521
}'
```

### Get Total Distance of Courier
```
curl --location 'http://localhost:8080/courier/2/distance'
```

### Get Store Entrance History of Courier
```
curl --location 'http://localhost:8080/api/store-entrance-logs/courier/2?page=0&size=1' \
--data ''
```

