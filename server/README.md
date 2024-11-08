# Purpose
This module uses Vertx to handle the server side of the application.
The server ultimately handles three main central functions:
1. Serving the swagger UI for use in the embedded browser.
2. Receiving requests from swagger UI for requested CURL commands.
3. Provides an Event Bus for communication between the server and the UI. (Vertx Event Bus is awesome)