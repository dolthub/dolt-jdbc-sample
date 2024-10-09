# dolt-jdbc-sample
Sample project using the Java Database Connectivity (JDBC) API to connect to a [Dolt database](https://doltdb.com/).

Check out [the associated blog post for an in-depth walkthrough](https://www.dolthub.com/blog/2024-10-11-dolt-with-jdbc/) on how to connect to a Dolt database via JDBC using this sample code. 

# Running the Sample
Before you can run this sample code, you need to start up a Dolt SQL server for this code to connect to. If you don't already have Dolt installed, head over to the [Dolt installation docs](https://docs.dolthub.com/introduction/installation) and follow the instructions to install Dolt.

## Start a Dolt SQL Server
Once Dolt is installed, create a directory for your new Dolt database and initialize it as a Dolt database directory. The directory name you use here will be the name of the Dolt database. 
```bash
mkdir doltdb && cd doltdb
dolt init
```

After you run `dolt init`, you'll have a working Dolt database directory. To launch a Dolt SQL server that serves the data in this directory, run:
```bash
dolt sql-server --port 11229
```

Note that by default, Dolt's SQL server will start up on port 3306, the same port used by MySQL. In case you already have a MySQL server running on your machine, we've added the `--port 11229` flag to start the server on a different port. Keep this terminal window open so you can see any log statements emitted from the server.

## Create Test Data
Next, we'll open up a SQL shell to our Dolt database and create some sample data. In a new terminal window, run:
```bash
mysql --port 11229 --protocol TCP -u root 
```

This will open up a SQL shell connected to your Dolt database. There isn't any data in the database yet, so let's create some sample data to query:
```sql
-- the database name comes from the directory where we ran dolt init
use doltdb;

CREATE TABLE users (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL
);

-- Insert some sample data
INSERT INTO users (name, email) VALUES ("Mickey", "themick@dolthub.com"), ("Minnie", "minnie@dolthub.com"), ("Goofy", "goofy@dolthub.com");

-- Create a Dolt commit
CALL dolt_commit('-Am', 'Adding sample data for users table');
```

## Run the Sample Code
Once you've got some sample data loaded into your Dolt database, you're ready to run the sample code! You can execute the code directly from your IDE's tooling, or you can run it from the command line using Maven: 
```shell
mvn clean compile exec:java -D exec.mainClass=Main
```

# Help!
If you run into any problems using this sample code, or just want some help integrating your application with Dolt, come join the [DoltHub Discord server](https://discord.gg/gqr7K4VNKe) and connect with our development team as well as other Dolt customers. We'll be happy to learn more about what you're building and help you get Dolt working! 
