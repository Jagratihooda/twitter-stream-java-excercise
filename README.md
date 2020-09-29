- Hit below URL in browser to run the application
  http://localhost:8081/tweetstreaming/process-tweets

- Configure Consumer key, Consumer Scret in application properties

  auth.consumer.key 
  auth.consumer.secret 

- Configure Search Param in application properties

- On first time run a URL will be displyed in console, open the URL in browser and enter the 
  generated in the console input.

- Output Tweets will be displayed in Console and logs/spring.log file



//***** Assignment ******//
Java part:
We would like you to write code that will cover the functionality explained below and provide
us with the source, instructions to build and run the application as well as a sample output of
an execution:
• Connect to the Twitter Streaming API
o Use the following values:
§ Consumer Key: <create a consumer key>
§ Consumer Secret: <your secret>
o The app name will be java-exercise
o You will need to login with Twitter
• Filter messages that track on a term e.g. "formulaone"
o This value should be configurable
• Retrieve the incoming messages for 30 seconds or up to 100 messages, whichever
happens first.
• Your application should return the messages grouped by user (users sorted
chronologically, ascending)
• The messages per user should also be sorted chronologically, ascending
• For each message, we will need the following:
o The message ID
o The creation date of the message as epoch value o The text of the message
o The author of the message
• For each author, we will need the following:
o The user ID
o The creation date of the user as epoch value
o The name of the user
o The screen name of the user
• All the above information is provided in either SDTOUT or a log file
• You are free to choose the output format, provided that it makes it easy to parse and
process by a machine