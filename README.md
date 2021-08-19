# Praxisruf Cloud Service

## Description

This service is part of the Praxisruf system.
The Cloud Service manages notifications between [Mobile Clients](https://github.com/IP5-Cloudbasiertes-Praxisrufsystem/IP5-praxis-mobile-client).
It receives notifications from Mobile Clients and forwards them to the interested recipients.
Clients and their configuration can be registered with the Cloud Service. 
The Configuration can be managed via the [Admin UI](https://github.com/IP5-Cloudbasiertes-Praxisrufsystem/IP5-admin-ui).

More detailed information on the system can be found in the project report [Cloudbasiertes Praxisrufsystem](https://github.com/IP5-Cloudbasiertes-Praxisrufsystem/IP5-documentation/blob/main/out/cloudbasiertes_praxisrufsystem.pdf).

## Development Setup

* Import the project in IntelliJ
* Create a Run Configuration for a Spring Boot Application
* Use Main Class `ch.fhnw.ip5.praxiscloudservice.PraxisCloudServiceApplication`
* Add the active profile `local`
* Set the following environment variables: 
  * FCM_CREDENTIALS: `Base64 Encoded Firebase Configuration`. (See [Installation Manual](https://github.com/IP5-Cloudbasiertes-Praxisrufsystem/IP5-documentation/blob/main/out/cloudbasiertes_praxisrufsystem.pdf).)
  * JWT_SECRET_KEY: `Random 64Bit String`  
* Run `docker-compose up` in the docker directory under the project root
