# Checklist

This application was generated using JHipster, you can find documentation and help at [https://jhipster.github.io](https://jhipster.github.io).

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools (like
[Bower][] and [BrowserSync][]). You will only need to run this command when dependencies change in package.json.

    npm install

We use [Gulp][] as our build system. Install the Gulp command-line tool globally with:

    npm install -g gulp

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    ./gradlew
    gulp

Bower is used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in `bower.json`. You can also run `bower update` and `bower install` to manage dependencies.
Add the `-h` flag on any command to see how you can use it. For example, `bower update -h`.

## Overview and Steps to introduce collaboration
1. Google Realtime API is used for collaborating audit profile responses across multiple users
2. Realtime API is based on concept of Google docs, and can be done using following steps extracted from Realtime Quickstart
    1. Register a user (If we do not have it earlier) Google Cloud Platform  (this would be the billing account for this service)
    2. Create a project using google developer console
    3. Enable the OAuth for this project under credentials
    4. Copy the client ID post OAuth enable which would be used in application
3. Once client ID is copied from above step, use this in the web application which needs Realtime API integration
    1. Include two javascripts in the application
        <script src="https://apis.google.com/js/api.js"></script>
        <script src="https://www.gstatic.com/realtime/realtime-client-utils.js"></script>
    2. Create AngularJS service similar to the one in `src/main/webapp/app/entities/audit-profile/audit-profile.realtime.service.js`
    3. Points to concentrate during creating service
         a) Define your custom Object as used in function `defineAuditQuestionResponseModel()`
         b) Update input parameters which needs to be collaborated as per the application.
         c) Make sure we use unique `driveFileName` as this is a physical file stored in google drive and used for collaboration
         d) Write your custom logic in `onNewFileCreated` which needs creates and populates data correctly in the drive file
         e) Update custom loginc in `onFileLoaded` which will continuosuly invoked if we change or collaborater change data
    4. Collabortation can be started or stopped using methods 
         a) `collaborate` : Starts collaboration and prompts login to google for OAuth (if not logged in)
         b) `stopCollaborate` : Ends collaboration and sign out thes OAuth (on localhost, signOut has no impact...ie signout doesn't not work)


### Realtime API observations
1. For Colloboration work : Google Cloud Platform, Drive Enablement are prerequisite
2. Realtime API accepts only Google Account registered users for OAuth. It can not be integrated with other OAuth
3. Latest Drive API is v3 (version 3), but realtime API only works in v2 (i.e., version 2)

[Overview and Steps to introduce collaboration]:https://developers.google.com/google-apps/realtime/overview
[Realtime Quickstart]:https://developers.google.com/google-apps/realtime/realtime-quickstart


### Building Docker Image

In the Checklist root folder:

1. docker login -u emmanuel16
2. enter password
3. ./custom/build.sh 
 
### Deploying Image to lriczfinnapd003

1. use [pgadmin](https://www.pgadmin.org/download/) to create a new db on lriczfinnapd002:5432.  Name of the new db should be checklistnewdbversion.  Where newdbversion is a new version number.
2. ssh into lriczfinnapd002.  If its the first time login in, run sudo usermod -aG docker YOUR_GUID
3. Run the command below, but first make the following changes to the command: change NEWCONTAINERVERSION to a new container name change NEWPORT to a new application port and NEWDBVERSION to a new database name created above.

`sudo docker pull emmanuel16/checklist;docker rm -f checklistNEWCONTAINERVERSION;docker run -d -p NEWPORT:9090 --restart=always --name checklistNEWCONTAINERVERSION -e SERVICE_ENV=prod,swagger -e DB_USER=postgres -e DB_PASSWORD=password -e SERVICE_DB=jdbc:postgresql://lriczfinnapd002:5432/ChecklistNEWDBVERSION -e SERVICE_ES_CLUSTER=elasticsearch -e SERVICE_ES_NODE=lriczfinnapd003:9300 emmanuel16/checklist`


## Building for production

To optimize the Checklist client for production, run:

    ./gradlew -Pprod clean bootRepackage

This will concatenate and minify CSS and JavaScript files. It will also modify `index.html` so it references
these new files.

To ensure everything worked, run:

    java -jar build/libs/*.war

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

## Testing

Unit tests are run by [Karma][] and written with [Jasmine][]. They're located in `src/test/javascript/` and can be run with:

    gulp test

UI end-to-end tests are powered by [Protractor][], which is built on top of WebDriverJS. They're located in `src/test/javascript/e2e`
and can be run by starting Spring Boot in one terminal (`./gradlew bootRun`) and running the tests (`gulp itest`) in a second one.

## Continuous Integration

To setup this project in Jenkins, use the following configuration:

* Project name: `Checklist`
* Source Code Management
    * Git Repository: `git@github.com:xxxx/Checklist.git`
    * Branches to build: `*/master`
    * Additional Behaviours: `Wipe out repository & force clone`
* Build Triggers
    * Poll SCM / Schedule: `H/5 * * * *`
* Build
    * Invoke Gradle script / Use Gradle Wrapper / Tasks: `-Pprod clean test bootRepackage`
    * Execute Shell / Command:
        ````
        ./gradlew bootRun &
        bootPid=$!
        sleep 30s
        gulp itest
        kill $bootPid
        ````
* Post-build Actions
    * Publish JUnit test result report / Test Report XMLs: `build/test-results/*.xml,build/reports/e2e/*.xml`

[JHipster]: https://jhipster.github.io/
[Node.js]: https://nodejs.org/
[Bower]: http://bower.io/
[Gulp]: http://gulpjs.com/
[BrowserSync]: http://www.browsersync.io/
[Karma]: http://karma-runner.github.io/
[Jasmine]: http://jasmine.github.io/2.0/introduction.html
[Protractor]: https://angular.github.io/protractor/

## Custom Directives

Following custom directives have been developed as part of the application till now:

* adc-metadata: directive created for templating meta data tree currently present on 'Create or edit a Checklist' and 'Create or edit a Client' screens. This directive internally makes use of [angular-ui-tree](https://github.com/angular-ui-tree/angular-ui-tree) tree component for constructing the tree control with custom nodes. Usage of the directive is provided below:
    
    `<adc-metadata type="metadata" tree-data="vm.treeMetaData"></adc-metadata>`

    where:
    
    * type: type of the tree which needs to be rendered. As of now, the directive supports only 'metadata' type
    * tree-data: attribute used for referencing the model (in the parent controller) required for constructing the nodes of the tree. The example shows the usage of `vm'treeMetaData' model for constructing the tree
    
    Source files for the directive can be located at:
    
    https://github.com/andresfuentes/Checklist/tree/master/src/main/webapp/app/components/custom-directives

