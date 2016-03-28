# libsms
A simple Java/Scala outgoing SMS [SPI](https://en.wikipedia.org/wiki/Service_provider_interface), including out-of-the-box implementations for
* [Twilio](http://www.twilio.com/)
* [Plivo](https://www.plivo.com/)
* [Clickatell](http://www.clickatell.com/)
* [Cellact](http://www.cellact.co.il/)

This library is actively used by [Wix.com](http://www.wix.com/) to deliver mission-critical text messages to your favorite restaurants.

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

Please use [the issue tracker](https://github.com/wix/libsms/issues) to report issues related to this library.

## License
This library uses the Apache License, version 2.0.

## Contributions
Want to see your SMS provider supported by this library? Awesome, we do too!
* Implement the `SmsGateway` trait in any JVM supported language (Scala, Java, Clojure, ...)
* Include a test-kit module for integration testing (see the included *-testkit modules for reference).
* Create a pull request, or add an issue with a link to your implementation
