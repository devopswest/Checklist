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

### Overview and Steps to introduce collaboration
1. Google Realtime API is used for collaborating audit profile responses across multiple users
2. Realtime API is based on concept of Google docs, and can be done using following steps extracted from Realtime Quickstart
    1. Register a user (If we do not have it earlier) in Google Cloud Platform  (this would be the billing account for this service)
    2. Create a project using google developer console
    3. Enable the OAuth for this project under credentials
    4. Copy the client ID post OAuth enable which would be used in application

3. Once client ID is copied from above step, use this in the web application which needs Realtime API integration
    1. Include following two javascripts in the application `index.html`.

    ```html
    <script src="https://apis.google.com/js/api.js"></script>
    <script src="https://www.gstatic.com/realtime/realtime-client-utils.js"></script>
    ```

    2. Create AngularJS service similar to the one in `src/main/webapp/app/entities/audit-profile/audit-profile.realtime.service.js`
    3. Points to concentrate during creating service
    4. Define your custom Object as used in function `defineAuditQuestionResponseModel()`

    ```javascript
    //Sample JSON to created
    {
      'id' :1,
      'questionResponse':'Yes',
      'questionId':34,
      'questionDescription':'Sample description for the question'
    }
    ```

    ```javascript
    //Google API model definition to create above JSON
    gapi.drive.realtime.custom.registerType(auditQuestionResponseModel, 'auditQuestionResponse');
    auditQuestionResponseModel.prototype.id = gapi.drive.realtime.custom.collaborativeField('id');
    auditQuestionResponseModel.prototype.questionResponse = gapi.drive.realtime.custom.collaborativeField('questionResponse');
    auditQuestionResponseModel.prototype.questionId = gapi.drive.realtime.custom.collaborativeField('questionId');
    auditQuestionResponseModel.prototype.questionDescription = gapi.drive.realtime.custom.collaborativeField('questionDescription');
    ```

    5. Make sure we use unique `driveFileName` as this is a physical file stored in google drive and used for collaboration. As google drive allows duplicate files with same name (google uses fileId for unique).
    6. Write your custom logic in `onNewFileCreated` which creates and populates data correctly in the drive file

    ```javascript
    var onNewFileCreated = function(model){
        auditquestionResponseMapCollab = model.createMap();
        for(var l in auditquestionResponseMap){
          var resp                 = model.create(modelName);
          resp.id                  = auditquestionResponseMap[l].id;
          resp.questionResponse    = auditquestionResponseMap[l].questionResponse;
          resp.questionId          = auditquestionResponseMap[l].questionId;
          resp.questionDescription = auditquestionResponseMap[l].questionDescription;
          auditquestionResponseMapCollab.set(String(auditquestionResponseMap[l].questionId),resp);
        }
        model.getRoot().set(modelName, auditquestionResponseMapCollab);
    }    
    ```
    
    7. Update custom logic in `onFileLoaded` which will Continuously invoked if we change or collaborator change model object

    ```
    var onFileLoaded = function(doc){
      auditquestionResponseMapCollab = doc.getModel().getRoot().get(modelName);
      attachCollaborateResponseToTemplate(templateQuestions);

      //Attach listener to child which only where we want to identify, and not yet root or document level
      auditquestionResponseMapCollab.addEventListener(gapi.drive.realtime.EventType.OBJECT_CHANGED, refreshQuestionResponses);

      //Note attach listeners on root to understand who is joining collaborator list or leaving
      doc.addEventListener(gapi.drive.realtime.EventType.COLLABORATOR_JOINED, collaborateJoinCallback);
	    doc.addEventListener(gapi.drive.realtime.EventType.COLLABORATOR_LEFT, collaborateLeftCallback);
    }
    ```
    ```
    //Collaborator information can be retrieve as follows
    var collaborateJoinCallback = function(evt){
      var user = evt.collaborator;
      console.log(user.userId + ' - ' + user.displayName + ' - ' + user.photoUrl + ' - ' + user.color + ' - ' + user.isMe);
    }
    ```

    8. Start: `collaborate` Starts collaboration and prompts login to google for OAuth (if not logged in)

    ```
    //No login prompt dialog would be displayed
    realtimeUtils.authorize(loginSuccessCallback, false);
    //OR
    //To have login prompt dialog to be displayed
    realtimeUtils.authorize(loginSuccessCallback, true);
    ```

    9. Stop: `stopCollaborate` - End collaboration and sign out OAuth (on localhost, signOut has no impact, so to test it host it on some external IP)

    ```
    window.gapi.auth.signOut();
    ```

### Realtime API observations
1. For Colloboration work : Google Cloud Platform, Google Drive are prerequisite (incurs cost, not open source).
2. Realtime API accepts only Google Account registered users for OAuth. It can not be integrated with other OAuth
3. Latest Drive API is v3 (version 3), but realtime API only works in v2 (i.e., version 2)
4. Multiple listeners can be attached to listen changes on model object. But attach listeners at appropirate levels (Ex:-  collaborator joining or leaving should be done at document level. Changes to the model should listened at the model which is attached to root e.t.c)

[Overview and Steps to introduce collaboration]:https://developers.google.com/google-apps/realtime/overview
[Realtime Quickstart]:https://developers.google.com/google-apps/realtime/realtime-quickstart


### Building Docker Image

In the Checklist root folder:

1. docker login -u adcpoc
2. enter password
3. ./custom/build.sh

### Deploying Image to lriczfinnapd003

1. use [pgadmin](https://www.pgadmin.org/download/) to create a new db on lriczfinnapd002:5432.  Name of the new db should be checklistnewdbversion.  Where newdbversion is a new version number.
2. ssh into lriczfinnapd003.  If its the first time login in, run sudo usermod -aG docker YOUR_GUID
3. Run the command below, but first make the following changes to the command: change NEWCONTAINERVERSION to a new container name change NEWPORT to a new application port and NEWDBVERSION to a new database name created above.

`sudo docker pull adcpoc/checklist;docker rm -f checklistNEWCONTAINERVERSION;docker run -d -p NEWPORT:9090 --restart=always --name checklistNEWCONTAINERVERSION -e SERVICE_ENV=prod,swagger -e DB_USER=postgres -e DB_PASSWORD=password -e SERVICE_DB=jdbc:postgresql://lriczfinnapd002:5432/ChecklistNEWDBVERSION -e SERVICE_ES_CLUSTER=elasticsearch -e SERVICE_ES_NODE=lriczfinnapd003:9300 adcpoc/checklist`


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

* adc-metadata: directive created for templating meta data tree control. This directive has been used on 'Create or edit a Checklist' and 'Create or edit a Client' screens. This directive makes use of [angular-ui-tree](https://github.com/angular-ui-tree/angular-ui-tree) component internally for constructing the tree control with custom nodes. Usage of the directive is provided below:

    `<adc-metadata type="metadata" tree-data="vm.treeMetaData"></adc-metadata>`

    where:

    * type: attribute for specifying the type of tree which needs to be rendered. As of now, the directive supports only `metadata` type
    * tree-data: attribute for referencing the model (in the parent controller) required for constructing the nodes of the tree. The example shows the usage of `vm.treeMetaData` model for constructing the tree

    Source files for the directive can be located at:
    https://github.com/andresfuentes/Checklist/tree/master/src/main/webapp/app/components/custom-directives

* adc-taxonomy: directive created for templating taxonomy dependent dropdown control. This directive has been used on 'Create or edit a Checklist' (for 'Country' dropdown control) and 'Create or edit a License' (for 'License Type' dropdown control) screens. This directive makes use of select control internally for constructing the taxonomy dependent dropdown control. Usage of the directive is provided below:

    `<adc-taxonomy model="vm.checklist.countryId" options-data="vm.taxonomies" load-data="vm.loadData('TERRITORY')"></adc-taxonomy>`

    where:

    * model: attribute for referencing the selected value model (in the parent controller) of the select control. The example shows the usage of `vm.checklist.countryId` model for constructing the dropdown
    * options-data: attribute for referencing the options model (in the parent controller) of the select control. The example shows the usage of `vm.taxonomies` model for constructing options in the dropdown
    * load-data: attribute for referencing the function (in the parent controller) used to populate taxonomy specific values in the dropdown. Taxonomy type ('TERRITORY') is provided as an input to the function call so that taxonomy specific values are loaded in the dropdown

    Source files for the directive can be located at:
    https://github.com/andresfuentes/Checklist/tree/master/src/main/webapp/app/components/custom-directives
