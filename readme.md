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
```ACTO_CONF``` in JSON format. An example follows:

    {
        "path": {
            "apiKey": "ajHWm8Bq89j5qpvkYc9jXFG8XUUxB2qM",
            "config": "https://hooks.slack.com/services/T....",
            "dispatcher": "Slack"
        }
    }

Each path is defined by the components.

* apiKey - the apiKey that you must send as 
the```Authorization``` header of your post request

* config - the configuration for the given dispatcher. The
configuration for the slack dispatcher is simply the
webhook URL.  

* dispatcher - the actual dispatcher to use, currently only
"Slack" is a valid value.