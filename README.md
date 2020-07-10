# Overview
The project contains automated tests for YandexTranslate. 

# Initial environment setup
1. Install JDK8. 
2. Install IntelliJ IDEA (Community version is enough for out purpose) 

# IDE setup
1. Cucumber-jvm and gherkin plugins (File\Settings\Plugins) should be set up by default. But if not, install it. 
2. Install lombok plugin.
3. Turn on Annotation Processor in project settings (File\Settings\Build,Execution,Deployment\Compiler\Annotation Processors\Enable Annotation Processing set to true)

# Run tests from IDE Run/Debug Configuration
1. Find com.sber.qa.YandexTranslateTest class in the project. 
2. Mouse right click and select Create 'YandexTranslateTest'
3. In Run/Debug Configuration form set VM options to -Dspring.profiles.active=dev and Shorten command line to JAR manifest and press OK. 
4. Run created configuration. All features with @translate tag will be executed.
5. Configuration should be setup in the base.conf file. It located in the resources.

# Run tests from command line
Pre condition: Maven should be installed 
mvn clean -Dtest=YandexTranslateTest test -Dspring.profiles.active=dev

# Open reports from local workstation
Pre condition: Allure server should be installed
allure serve --report-dir <path to report directory> (Example: \<project>\target\allure-results)