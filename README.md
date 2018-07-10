# RabbitMQ Reader for IFS<sup>®</sup>

RabbitMQ is the most popular open source message broker. It is lightweight and easy to deploy on premises and in the cloud and supports multiple messaging protocols. RabbitMQ can be deployed in distributed and federated configurations to meet high-scale, high-availability requirements.

Our product, <b>RabbitMQ reader for IFS<sup>®</sup></b> is a connector agent to poll RabbitMQ queue and receive messages to IFS. It was built using RabbitMQ Java Client Library and uses uses AMQP 0-9-1 protocol.

## Compatibility
RabbitMQ Reader was tested in IFS App8, App9 versions. In IFS version 10, connect architecture is redesigned and this may not work. 
We will update the compatibility with App10 as soon as we finish the development

### Prerequisites
* <a href="https://www.rabbitmq.com/download.html">Download</a> and install RabbitMQ
* Administrative privileges for IFS Solution Manager


### Setup

* Save <a href="https://raw.githubusercontent.com/knakit/RabbitMQ_Reader/master/config/Config_KNAKIT_RABBITMQ_READER1.xml">RabbitMQ Reader Configuration file</a> to a local destination 
* Login to IFS EE client with Administrator account
* Setup IFS Connect

Say what the step will be

```
Give the example
```

And repeat

```
until finished
```

End with an example of getting some data out of the system or using it for a little demo

## Running the tests

Explain how to run the automated tests for this system

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```

## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Dropwizard](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [ROME](https://rometools.github.io/rome/) - Used to generate RSS Feeds

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

* **Billie Thompson** - *Initial work* - [PurpleBooth](https://github.com/PurpleBooth)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration
* etc

