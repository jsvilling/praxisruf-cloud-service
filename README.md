# Praxisruf Cloud Service

## Description

This service is part of the Praxisruf system.
The Cloud Service manages notifications and signaling for voice calls between [Mobile Clients](https://github.com/jsvilling/praxisruf-ios-mobile-client).
It receives notifications from Mobile Clients and forwards them to the interested recipients.
Likewise it receives signals for connection establishmet and forwards them to the relevant recipients.
If a signal can not be delivered a pre-configured notification will be sent instead. 
Clients and their configuration can be registered with the Cloud Service. 
The Configuration can be managed via the [Admin UI](https://github.com/jsvilling/praxisruf-admin-ui).

More detailed information on the system can be found in the project reports [Cloudbasiertes Praxisrufsystem](https://github.com/IP5-Cloudbasiertes-Praxisrufsystem/IP5-documentation/blob/main/out/cloudbasiertes_praxisrufsystem.pdf) and [Peer-to-Peer Kommunikation für Sprachübertragung in einem Praxisrufsystem](https://github.com/jsvilling/IP6_Bachelorarbeit_Bericht_Cloudbasiertes_Praxisrufsystem/blob/master/out/p2p_sprachubertragung_in_praxisrufsystem.pdf).


## Development Setup

* Import the project in IntelliJ
* Create a Run Configuration for a Spring Boot Application
* Use Main Class `ch.fhnw.ip5.praxiscloudservice.PraxisCloudServiceApplication`
* Add the active profile `local`
* Set the following environment variables: 
  * FCM_CREDENTIALS: `Base64 Encoded Firebase Configuration`. 
  * JWT_SECRET_KEY: `Random 64Bit String`  
  * SPRING_PROFILES_ACTIVE: `local`
  * AWS_ACCESS KEY: `AWS Acces Key Id for Amazon Polly`  
  * AWS_SECRET_KEY: `AWS Acces Key Secret for Amazon Polly`
  * NOTIFICATION_TYPE_FOR_UNAVAILABLE: `NotificationTypeId for notification sent on unavailable signaling connection`
* If you need more information on how to determine the values of these properties, please consult the [Installation Manual](https://github.com/jsvilling/IP6_Bachelorarbeit_Bericht_Cloudbasiertes_Praxisrufsystem/blob/master/out/p2p_sprachubertragung_in_praxisrufsystem.pdf) (Appendix D). 
* Run `docker-compose up` in the docker directory under the project root
