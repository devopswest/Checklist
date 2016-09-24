(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('AuditProfileRealtime', AuditProfileRealtime);

    AuditProfileRealtime.$inject = ['$resource'];

    function AuditProfileRealtime($resource) {

    	var clientId          = '370295086792-2c0r6ve8mm7ot16ie1tq2ne9fe705ugg.apps.googleusercontent.com';
    	var fileMimeType      = 'application/vnd.google-apps.drive-sdk';
      var realtimeUtils     = new utils.RealtimeUtils({ clientId: clientId });
    	var apiKey            = '579021b7897ec0165309794dd5394ea7985d0752';
    	var authScopes        = 'profile';
    	var scopes            = 'profile https://www.googleapis.com/auth/drive https://www.googleapis.com/auth/drive.file profile';
        var auditQuestionResponseModel     = function(){    };
        var auditquestionResponseMapCollab = [];
        var isDirtyQuestionIdMap = {};

        //User Specific information
        var isCustomModelRegistered = false;
        var modelName = 'auditQuestionResponse';
        var auditProfileId;
        var auditquestionResponseMap;
        var driveFileName;
        var driveFileId;
        var templateQuestions;

        var collaborate = function(profileId, responseMap, treedata, isDirtyMap){
        	auditProfileId           = profileId;
        	auditquestionResponseMap = responseMap;
        	templateQuestions        = treedata;
        	isDirtyQuestionIdMap     = isDirtyMap;
        	driveFileName            = 'audit_profile_' + auditProfileId;
        	realtimeUtils.authorize(loginSuccess, false);
        	return true;
        }

        var stopCollaborate = function(){
        	console.log('Method:stopCollaborate ..stop collaboration');
        	window.gapi.auth.signOut();
        	gapi.auth.signOut();
        	return false;
        }

        var defineAuditQuestionResponseModel = function(){
        	if(!isCustomModelRegistered){
        		gapi.drive.realtime.custom.registerType(auditQuestionResponseModel, 'auditQuestionResponse');

        	 	auditQuestionResponseModel.prototype.id = gapi.drive.realtime.custom.collaborativeField('id');
            	auditQuestionResponseModel.prototype.questionResponse = gapi.drive.realtime.custom.collaborativeField('questionResponse');
            	auditQuestionResponseModel.prototype.questionId = gapi.drive.realtime.custom.collaborativeField('questionId');
            	auditQuestionResponseModel.prototype.questionDescription = gapi.drive.realtime.custom.collaborativeField('questionDescription');

        		isCustomModelRegistered = true;
        	}
        }



        var loginSuccess = function(response){
        	defineAuditQuestionResponseModel();
        	if(!response.error){     
        		console.log('Method:loginSuccess....login success');
        		loadFileOrCreateFile();
        	}else{
        		console.log('Method:loginSuccess....Error in login');
        		realtimeUtils.authorize(function(response){ loadFileOrCreateFile(); }, true);
        	}
        }
        
     

        /**
         * If File is found in drive load the file or otherwise create the file in drive
         */
        var loadFileOrCreateFile = function(){        	
        	if(driveFileId){        		
        		realtimeUtils.load(driveFileId, onFileLoaded, onNewFileCreated);
        	}else{
        		console.log('Method:loadFileOrCreateFile ..' + driveFileName + ' need to be CREATED');
        		realtimeUtils.createRealtimeFile(driveFileName, loadDataPostCreation);
        	}
        }

        /**
         * Method to load the drive object into memory root and window history
         */
        var loadDataPostCreation = function(creationResponse){
        	if(creationResponse){
        		driveFileId = creationResponse.id;
        		realtimeUtils.load(driveFileId, onFileLoaded, onNewFileCreated);
        	}else{
        		console.log('Method:loadDataPostCreation ..error in creating file ' + JSON.stringify(creationResponse));
        	}
        }

        /**
         * Ties the current model object to root either after getting from drive or new created object
         */
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

        	attachCollaborateResponseToTemplate(templateQuestions);
        }

        /**
         * Continuously retrieves the model object from Root which is tied to drive object and keep listening for any changes
         *
         */
        var onFileLoaded = function(doc){
        	auditquestionResponseMapCollab = doc.getModel().getRoot().get(modelName);
        	attachCollaborateResponseToTemplate(templateQuestions);
        	//Refresh the tree once the data is retrieved and update in the tree
        	$('#template_questions').scope().$apply();
        	auditquestionResponseMapCollab.addEventListener(gapi.drive.realtime.EventType.OBJECT_CHANGED, refreshQuestionResponses);
        }

        var refreshQuestionResponses = function(evt){
			  var isValueChange = false;
			  for (var i = 0; i < evt.events.length; i++) {
          if(!evt.events[i].isLocal && (evt.events[i].type ==  'value_changed')){
			    	console.log('Values Are changed...refesh'
                   + ', Name : ' + evt.events[i].session.displayName
                   + ', photoUrl :' + evt.events[i].session.photoUrl);
			    	isValueChange = true;
			    	break;
			    }
			  }

			  if(isValueChange){
				  $('#template_questions').scope().$apply();
			  }
		}

        /**
		 * A recursive method to prepare empty responses for template questions which are not yet answered.
		 * These empty responses act as place holder when the template questions are displayed and buttons are toggled
		 */
		var attachCollaborateResponseToTemplate = function(templateQuestions){
			for(var l=0;l<templateQuestions.length;l++){
				var questionId = String(templateQuestions[l].id);
				//Update if drive has value and only when it is not locally dirty
				if(auditquestionResponseMapCollab.has(questionId)){
					if(!isDirtyQuestionIdMap[questionId]){
						templateQuestions[l].response = auditquestionResponseMapCollab.get(questionId);
					}else{
						auditquestionResponseMapCollab.get(questionId).questionResponse = isDirtyQuestionIdMap[questionId];
					}
				}
				attachCollaborateResponseToTemplate(templateQuestions[l].children);
			}
		}

        return {
        	collaborate                      : collaborate,
        	stopCollaborate                  : stopCollaborate
        };
    }
})();
