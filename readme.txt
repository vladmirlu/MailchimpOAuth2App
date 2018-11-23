Instruction to get access_token
1). replace [client_id], [client_secret], [redirect_uri] in 'application.properties' file with actual values of your MailChimp registered application

2). Run RunApp.main method of this application;

3). Take a look on the console output and follow [Authorize_URL]

4). Authorize yourself using your username and password (NOTE! You have no more 30 sec for doing next two steps)

5). Copy [code] value from redirect url and immediately replace [auth_code] value with that [code] value in 'application.properties' file

6). Run RunApp.main method of this application (NOTE! After 30 sec. [code] value expired so you wont be able to obtain access_token)