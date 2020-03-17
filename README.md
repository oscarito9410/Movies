# MOVIES

[![Build Status](https://build.appcenter.ms/v0.1/apps/1769524d-d19f-4947-b0e3-a62b3e051f80/branches/master/badge)](https://sonarcloud.io/dashboard?id=oscarito9410_Movies) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=oscarito9410_Movies&metric=alert_status)](https://sonarcloud.io/dashboard?id=oscarito9410_Movies)  [![SonarClod Bugs](https://sonarcloud.io/api/project_badges/measure?project=oscarito9410_Movies&metric=bugs)](https://sonarcloud.io/dashboard?id=oscarito9410_Movies) [![SonarClod Bugs](https://sonarcloud.io/api/project_badges/measure?project=oscarito9410_Movies&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=oscarito9410_Movies)

An android application to get popular movies from ```themoviedb``` API
using clean architecture for separation of code in different layers with assigned responsibilities in order to make it easier for further modification.


## Installation

Just one step to clone the repo as below

```bash
git clone https://github.com/oscarito9410/Movies.git
```
It's recommend to have the latest version of android studio **3.5.1**


## Project structure

As we mentioned before we're taking into account SOLID principles and clean architecture and of course clean code. The project structure contains the following layers:

1. **UI (Presentation)**
   - Includes a reference of the domain and data layer. It's an android specific layer which executes the UI logic. This layer also contains the implementation of the architectural pattern which is **MVVM** as required in the code challenge.

2. **Domain**
     - It is just a pure kotlin package with no android specific dependency. It contains the execution of the business rules (use case) .
     - Contains the definition of the business entities. Specifically in this app contains the entity  **Movie.**


3. **Data**
   - Provides the required data for the application to the domain layer by implementing interfaces (**repositories**).

   - This layer is basically divided in 2 packages. The first one contains the implementation for the local data source (**room**) and the second contains the implementation for the remote data source (**retrofit**)


## Unit tests

Unit tests are located at ```test``` and ```androidtest``` directories.

  **Test directory**
    : Contains unit tests for the **viewModels**, **repositories** and **mappers**, these tests doesn't contain any android dependencies and could be run in a local development machine.

  **Android test directory**
    : Contains unit tests for room **database**. Just to make sure database transactions are working as expected. These test must be run using a device or an emulator.


## Build with

* [Kotlin](#) - Main programming language
* [MVVM](#) - Architectural pattern used
* [Koin](#) - Framework for Dependency Injection
* [Room](#) - To store local data
* [Retrofit](#) - To make network requests
* [Rxjava2](#) - To handle async calls
* [Jetpack](#) - Used in conjunction with MVVM
* [SonarQuabe](#) - To catch bugs and vulnerabilities in the app
* [AppCenter](#) - As continuos integration tool.

## Author

Oscar Emilio Pérez Martínez
* [StackOverFlow](https://stackoverflow.com/users/1710571/oscar-emilio-perez-martinez)
* [Linkedin](https://www.linkedin.com/in/oscar-perez-martinez-3aa07b63)



## License
[MIT](https://github.com/oscarito9410/Movies/blob/master/LICENSE)


## Screenshot
<img src="https://github.com/oscarito9410/Movies/blob/master/screenshot.png" width="350" height="350" title="hover text">
