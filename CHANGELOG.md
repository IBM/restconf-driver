# Change Log

## [0.1.4](https://github.com/IBM/restconf-driver/tree/0.1.4) (2022-12-08)

[Full Changelog](https://github.com/IBM/restconf-driver/compare/0.1.3...0.1.4)

**Implemented enhancements:**

- Disable spring security DEBUG logs by default
- Security Vulnerability Fixes

## [0.1.3](https://github.com/IBM/restconf-driver/tree/0.1.3) (2022-11-22)

[Full Changelog](https://github.com/IBM/restconf-driver/compare/0.1.2...0.1.3)

**Implemented enhancements:**

- Springboot Upgrade from 2.5.x to 2.7.x
- Security vulnerability fixes
- App version update in helm chart

## [0.1.2](https://github.com/IBM/restconf-driver/tree/0.1.2) (2022-09-30)

[Full Changelog](https://github.com/IBM/restconf-driver/compare/0.1.1...0.1.2)

**Implemented enhancements:**

- Security Vulnerabilities fix
- Maintain tenantId as constant and CamelCase

## [0.1.1](https://github.com/IBM/restconf-driver/tree/0.1.1) (2022-09-12)

- Security Vulnerabilities fix

## [0.1.0](https://github.com/IBM/restconf-driver/tree/0.1.0) (2022-09-09)

- Security Vulnerabilities fixes
- Java11 Upgrade
- Enable Request Response Logging
- Enable SSL on restconf driver
- Use Keystore password from the secret cp4na-o-keystore
- Multitenancy feature

## [0.0.4](https://github.com/IBM/restconf-driver/tree/0.0.4) (2022-07-20)

- Security Vulnerabilities fixes

## [0.0.3](https://github.com/IBM/restconf-driver/tree/0.0.3) (2022-05-31)

- Authentication Bypass fix for management API
- Modified icr.io repo path in helm values.yaml file
- Modified Kafka Instance Name from iaf-system-kafka-bootstrap to cp4na-o-events-kafka-bootstrap
- Vulnerabilities fixes

## [0.0.2](https://github.com/IBM/restconf-driver/tree/0.0.2) (2022-04-11)

- Changed the driver to use the Upgrade CP4NA lifecycle method for Update calls to CISCO CNC


## [0.0.1](https://github.com/IBM/restconf-driver/tree/0.0.1) (2022-03-21)

- CP4NA lifecycle methods Create/Update/Delete are implemented to target CISCO CNC Server
- Update lifecycle method is not tested as CP4NA does not support Update. 
- Create lifecycle method is not implemented as CP4NA does not support Get resources.
