# HttpService

The goal of this application is to provide HTTP service that would listen on port 80. Application sends HTTP GET request to
google.com and prints out the response. To test the application, compile this project, then open any Internet browser and go to
localhost:80, where you should see a response of HTTP GET request to google.com in a plain text format. An updated result will
be provided every time you reload the page, as application use threads to allow multiple requests at the same time.
