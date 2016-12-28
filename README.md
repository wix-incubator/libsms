# libsms
A simple Java/Scala outgoing SMS [SPI](https://en.wikipedia.org/wiki/Service_provider_interface).

Actively used by [Wix.com](http://www.wix.com/) to deliver mission-critical text messages to your favorite restaurants.

## Supported SMS providers
| SMS Provider                              | Project                                                       |
| ----------------------------------------- | ------------------------------------------------------------- |
| [BulkSMS](http://www.bulksms.com/)        | [libsms-bulksms](https://github.com/wix/libsms-bulksms)       |
| [Cellact](http://www.cellact.co.il/)      | [libsms-cellact](https://github.com/wix/libsms-cellact)       |
| [Clickatell](https://www.clickatell.com/) | [libsms-clickatell](https://github.com/wix/libsms-clickatell) |
| [CM](https://www.cmtelecom.com/)          | [libsms-cm](https://github.com/wix/libsms-cm)                 |
| [Nexmo](https://www.nexmo.com/)           | [libsms-nexmo](https://github.com/wix/libsms-nexmo)           |
| [Plivo](https://www.plivo.com/)           | [libsms-plivo](https://github.com/wix/libsms-plivo)           |
| [Twilio](https://www.twilio.com/)         | [libsms-twilio](https://github.com/wix/libsms-twilio)         |


## Usage
In a nutshell, usage is as simple as instantiating one of the SMS provider implementations, and calling `sendPlain` with some text to send, sender information and destination phone number. You get back a provider-specific message ID.

Of course, you'll need to have an account with the SMS provider. Sending text messages isn't free, you know.

For actual code, see the [IT](https://en.wikipedia.org/wiki/Integration_testing) suite that accompanies each SMS provider implementation.

## Installation
### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
  <groupId>com.wix.sms</groupId>
  <artifactId>libsms-all</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Reporting Issues
Please use [the issue tracker](https://github.com/wix/libsms/issues) to report general issues related to this library.

Issues related to a specific SMS provider implementation should be reported to its separate issue tracker.

## License
This library uses the Apache License, version 2.0.

## Contributions
Want to see your SMS provider supported by this library? Awesome, we do too!
* Implement the `SmsGateway` trait in any JVM supported language (Scala, Java, Clojure, ...)
* Include a test-kit module for integration testing (for reference, see the ```-testkit``` module included in any existing SMS gateway implementation).
* Create a pull request, or add an issue with a link to your implementation
