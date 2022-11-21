# Getting Started


## Local environment

To run the project locally its important to configure Docker and Maven. 

By compiling the application then running docker-compose the service will build all the images needed, and a demo version will start to run.
``` 
mvn clean install 
docker-compose up
```

Inside Docker will be running a Postgres instance to store the Tasks, a Ngix to serve the resized images and the Spring webserver.


### POST /task

To call the Post method you may use this curl execution
```
curl --location --request POST 'localhost:8080/task' \
--header 'Content-Type: multipart/form-data' \
--form 'image=@"{PATH_TO_FOLDER}/src/test/resources/large.png"' \
--form 'width="500"' \
--form 'height="500"'

```
 Respose
```
{
  "created": 0,
  "fileName": "string",
  "height": 0,
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "md5": "string",
  "url": "string",
  "width": 0
}
```

### GET /Task/:taskid
This is an example of the Get request
```
curl --location --request GET 'localhost:8080/task/28bdd3f6-513f-453a-88ad-13f142670b62'

```
Response

```
{
"created": 0,
"fileName": "string",
"height": 0,
"id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
"md5": "string",
"url": "string",
"width": 0
}
```

### Swagger
Swagger documentation can also be found at 
```
  http://localhost:8080/swagger-ui/
```

## Data Base migration
Data base migration is done using Liquibase, for now it is done automatically on the docker-compose and integration tests.
Further it can be transfered to a CD environment.

## Data Access
Data base access is done using Jooq to not rely on generated queries by a ORM mechanism. 
To regenerate jooq files in case any change on the db run the command bellow.

```
docker-compose up 
mvn clean install -Pgenerate
```


### Throubleshoot

Sometimes the local database might not start due to files of previous versions that were around
you can delete the `.postgres-data` folder and execute the docker system prune command to clean up.


### Running over IDE
To run using IDE you need to set the environment var

`DB_URL=jdbc:postgresql://localhost:5431/postgres`

### Future
Unfortunately I didn't have time to evolve the PoC to be fully non-blocking. 
The current implementation will show some issues under load.

Some scenarios are not fully tested as well.