Acto web-dispatcher
===
Acto web-dispatcher is a small microservice that accepts JSON
data and dispatches it to one or more channels, like a modern
day formmail.pl, except that it currently doesn't support form
data or sending mail.

The current implementation supports configurable HTTP POSTS
of JSON data. Acto web-dispatcher is written in Java and builds
an executable jar without dependencies (dependencies are copied
to a ```lib``` folder). A ready to go Docker image is available
at [Docker hub](https://hub.docker.com/r/actoaps/web-dispatcher/)

Configuration is supplied through en environment variable called
```ACTO_CONF``` in JSON format. For Slack this could look like this:

    {
        "path": {
            "apiKey": "ajHWm8Bq89j5qpvkYc9jXFG8XUUxB2qM",
            "config": "https://hooks.slack.com/services/T....",
            "dispatcher": "Slack"
        }
    }
    
For Twilio you need to insert your ACCOUNT SID, AUTH TOKEN and FlowSid (from the URL you need to call), seperated by commas.
So the ACTO_CONF should look something like this:

    {
        "path": {
            "apiKey": "3YbHicBWw2dMFPyquu364aNjf8AD7qw",
            "config": "YB61ab31f3cda598f0c3a0d8c25bbdd2qb,i42q06162eeeb929f54161we1b976391,Ybc1f6358d03fdac395a41febec191873o",
            "dispatcher": "Twilio"
        }
    } 
    
For the slf4j Logger it might look like this: 

    {
        "path": {
            "apiKey": "2J5GCMcBWw2dMFPyquu364aNjf8AD6ss",
            "config": "",
            "dispatcher": "Log"
        }
    }
    
For the Smtp dispatcher it might look like this:

    {
       "path":{
          "apiKey":"ibOas8KhECegFlBmIlYMVhsDYQZQNUY5HQSDW",
          "config":"smtp.office365.com,587,user@example.com,mypassword,noreply@example.com",
          "dispatcher":"Smtp"
    }

The config contains the comma separated SMTP server, port, username, passsword and "from" mail-address. The SMTP dispatcher does not support sending e-Mail without authentication and TLS. If no "from" mail-address is set, the mail will be sent from "username".

To send a Message through SMTP the message payload must look like this:

    {
        "to": "you@example.com",
        "subject": "Test e-mail",
        "body": "This is a test e-mail",
        "name": "example.png",
        "type": "image/png",
        "data": "iVBORw..."
    }

The last 3 parameters describe the attachment, data is base64 encoded. The SMTP sender does not currently support 0 or more than 1 attachment.

Which you can then call with curl like this:

    curl -d '{"Hello":"World"}' -H "Authorization: Bearer 2J5GCMcBWw2dMFPyquu364aNjf8AD6ss" -X POST http://localhost:8080/path

Each path is defined by the components.

* apiKey - the apiKey that you must send as 
the```Authorization``` header of your post request You may omit the
"Bearer" type if you want, but bear in mind that certain proxies may
think it's a defective header and remove it.

* config - the configuration for the given dispatcher. The
configuration for the slack dispatcher is simply the
webhook URL.  

* dispatcher - the actual dispatcher to use, currently only
"Slack", "Log", "Twilio" and "Smtp" are valid values.

The service by default runs on port 8080. The simplest way
to change this (other than in the code), is remapping it in
Docker.
