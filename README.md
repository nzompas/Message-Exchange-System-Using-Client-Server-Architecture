A client-server messaging system implemented in Java, consisting of four main classes: Message, Account, Client, and Server. 
- The Message class includes five fields: idRead, sender, receiver, body, and messId, with a method add() for creating and storing messages. 
- The Account class manages user data and messages, including fields like username, AuthToken, messageBox, and methods for account creation and message handling. 
- The Client class establishes a socket connection, sends requests, and handles responses from the server using threads to support multiple simultaneous clients. 
- The Server class handles client connections, processes incoming requests, and responds appropriately, with methods for user registration, message sending, inbox display, message reading, deletion, and token authentication.
