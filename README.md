# RabbitMQ Reader for IFS<sup>®</sup>

RabbitMQ is the most popular open source message broker. It is lightweight and easy to deploy on premises and in the cloud and supports multiple messaging protocols. RabbitMQ can be deployed in distributed and federated configurations to meet high-scale, high-availability requirements.

Our product, <b>RabbitMQ reader for IFS<sup>®</sup></b> is a connector agent to poll RabbitMQ queue and receive messages to IFS. It was built using RabbitMQ Java Client Library and uses uses AMQP 0-9-1 protocol.

## Compatibility
RabbitMQ Reader was tested in IFS App8, App9 versions. In IFS version 10, connect architecture is redesigned and this may not work. 
We will update the compatibility with App10 as soon as we finish the development

### Prerequisites
* <a href="https://www.rabbitmq.com/download.html">Download</a> and install RabbitMQ
* Administrative privileges for IFS Solution Manager


### Setup (for App8 and App9)
* Save <a href="https://raw.githubusercontent.com/knakit/RabbitMQ_Reader/master/config/Config_KNAKIT_RABBITMQ_READER1.xml">RabbitMQ Reader Configuration file</a> to a local destination 
* Login to IFS EE client with Administrator account
* Setup IFS Connect, RMB on root node, Import Instance

<img src="https://raw.githubusercontent.com/knakit/RabbitMQ_Reader/master/screenshots/step1.png" alt="Import Instance">

<br><br>

* Change the RabbitMQ Reader settings
```
Poll Time:  Polling interval (in milliseconds)
Host:       RabbitMQ Server
Port:       RabbitMQ Port used by AMQP 0-9-1 
User Name:  RabbitMQ user (user guest will not have access from remote)
Password:   RabbitMQ user password
Queue:      RabbitMQ queue
```

<img src="https://raw.githubusercontent.com/knakit/RabbitMQ_Reader/master/screenshots/step2.png" alt="RabbitMQ configurations">


<br><br>

* Add the Reader to Connect Server
<br>

<img src="https://raw.githubusercontent.com/knakit/RabbitMQ_Reader/master/screenshots/step3.png" alt="Connect Server configurations">

<br><br>
* Copy <a href="https://github.com/knakit/RabbitMQ_Reader/blob/master/server/lib/amqp-client-4.0.2.jar">amqp-client-4.0.2.jar</a>, 
<a href="https://github.com/knakit/RabbitMQ_Reader/blob/master/server/lib/slf4j-api-1.7.21.jar">slf4j-api-1.7.21.jar</a>, 
<a href="https://github.com/knakit/RabbitMQ_Reader/blob/master/server/lib/slf4j-simple-1.7.22.jar">slf4j-simple-1.7.22.jar</a>, 
<a href="https://github.com/knakit/RabbitMQ_Reader/blob/master/dist/KnakitRabbitMQReader.jar">KnakitRabbitMQReader.jar</a> 
to $IFS_HOME\javaruntime\ location

<br>

* Modify $IFS_HOME\instance\$INSTANCE\bin\start_connectserver1.ifm to include the dependency libraries
```
$RABBITMQPATH=&IFS_HOME\javaruntime\amqp-client-4.0.2.jar;&IFS_HOME\javaruntime\slf4j-simple-1.7.22.jar;&IFS_HOME\javaruntime\slf4j-api-1.7.21.jar;
$CLASSPATH=...;RABBITMQPATH
```

* Restart Connect Server

## Running the Tests

Use the RabbitMQ <a href="https://www.rabbitmq.com/tutorials/tutorial-one-java.html">Hello World</a> example to create a simple java client to send messages to the queue. If everything works fine you should get each message as Application Message in IFS.


## Source

Source files are located in <a href="https://github.com/knakit/RabbitMQ_Reader/tree/master/src/com/knakit/connector/reader">RabbitMQ_Reader/src/com/knakit/connector/reader</a>

## Built With

* [IntelliJ](https://www.jetbrains.com/idea/) - IDE
* [Ant](https://ant.apache.org/) - Dependency Management


## Authors

* **Damith Jinasena** - *Initial work* - [damithsj](https://github.com/damithsj)


## License

This project is licensed under Apache License 2.0 - see the [LICENSE.md](LICENSE.md) file for details

## About knak.it

Knakit is a social initiative to explore the expandable capabilities of IFS applications. Our aim is to develop connectors for IFS to integrate with vast variety of applications. We believe that being social and open will achieve great heights, so our products are free and open source so anybody willing to use or contribute can join our team instantly. 

We understand that upgrading IFS shouldn't be the only option for connecting to innovative technology trends such as IoT, Social connectivity, Microservices, Machine Learning... etc. and the main objective behind our initiative is to develop a product line independent of IFS version so that virtually any customer can take advantage of the modern technology revolution.

Our product catalogue includes set of IFS connector readers and senders which can use out of the box and an integration framework which can use as a hub between IFS and any outside application.

Our products are built as pluggable applications so no need of worrying about customizing IFS core and it will be fully compatible with upgrades.

We also provide consultancy and accept custom tailored solutions for your requirements. If you have any mind-blowing ideas or integration requirements we loved to have a chat and discuss more information.

Enjoy and stay in touch!

Contact: <a href="mailto:knakit.dev@gmail.com">knakit.dev@gmail.com</a> 

