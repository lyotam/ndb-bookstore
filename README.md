# NDB-Bookstore: A Simple Book Store App

NDB-Bookstore is a Kotlin based app with REST API endpoints connecting to MySQL DB. 


## Features

NDB-Bookstore has the following features:
* Add a new book
* Get a book by id, or get one or more books by name or author
* Add a new customer
* Get a list of all customers
* Create a new book order for a specific client
* Retrieve an existing order by id
* Retrieve all books that were ordered by a specific customer (with a unique id)



## How to run this app:
You can run this app in 2 ways:

### Using docker-compose:
```
$ cd ndb-bookstore
$ docker-compose up -d
```

### Running the 2 containers manually:

This image requires mysql:8 and up (running on port 3306). To run an instance:

`$ docker container run --name mysqldb --network bookstore-mysql -p 33066:3306 -e MYSQL_ROOT_PASSWORD=toorpass -e MYSQL_DATABASE=ndb_bookstore -d mysql:8`

Starting an NDB-Bookstore instance is simple:

`$ docker container run --name ndb-bookstore-container --network bookstore-mysql -p 8080:8080 -d lyotam/ndb-bookstore:latest`