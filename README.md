Develop a WEB application that supports the specified functionality. Implementation requirements are as follows.
    
    1.Based on the entities of the subject area, create classes that correspond to them.
    2.Classes and methods should have names that reflect their functionality and should be correctly packaged.
    3.Code design must comply with the Java Code Convention.
    4.Information about the subject area is stored in a database.
    5.To access data, use the JDBC API using the connection pool.
    6.The application must support work with the Cyrillic alphabet (be multilingual), including when storing 
      information in a database:
        6.1 Should be able to switch the interface language;
        6.2 There should be support for input, output, and storage of information (in the database) recorded
            in different languages;
        6.3 Choose at least two: one based on the Cyrillic alphabet, the other based on the Latin alphabet.
    7.Application architecture must conform to the MVC pattern
    8.When implementing business logic algorithms, use design patterns.
    9.Using servlets and JSPs, implement the functionality given in the statement of tasks.
    10.Use Hibernate (JPA) to access data.
    11.Use the HTML for the front-end part.
    12.Implement the functionality proposed in the statement
       specific task using Spring.
      
Additionally, to the requirements set forth above, it is more than desirable to ensure that the 
following requirements are met.
       
    1.Implement differentiation of access rights of system users to application components.
    2.All input fields must be data validated.
    
Weight Loss / Tracking System.

    The client selects the food (name, number of proteins, fats, carbohydrates) that he ate (from an already 
    prepared list) and writes the Quantity.
    The client can add his own type of Food (name, calories, count. Proteins, fats, carbohydrates).
    The norm is taken from the Client parameters (age, height, weight, lifestyle, etc.).
    
    Display client statistics for today and last week
    The administrator can add / modify dishes that are available to all users
    The administrator can delete any dishes
    The user can edit / delete only dishes that he created