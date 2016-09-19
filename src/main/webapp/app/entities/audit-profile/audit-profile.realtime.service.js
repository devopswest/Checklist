(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('AuditProfileRealtime', AuditProfileRealtime);

    AuditProfileRealtime.$inject = ['$resource'];

    function AuditProfileRealtime($resource) {
    	
    	var clientId = '370295086792-2c0r6ve8mm7ot16ie1tq2ne9fe705ugg.apps.googleusercontent.com';
    	var fileMimeType= 'application/vnd.google-apps.drive-sdk';
        var realtimeUtils = new utils.RealtimeUtils({ clientId: clientId });
        var auditQuestionResponseModel = function(){    };
        var auditquestionResponseMapCollab =[];
        
        //User Specific information
        var isCustomModelRegistered = false;
        var modelName = 'auditQuestionResponse';
        var auditProfileId;
        var auditquestionResponseMap;
        var driveFileName;
        var driveFileId;
        var templateQuestions;
        
        var collaborate = function(profileId,responseMap,treedata){
        	auditProfileId           = profileId;
        	auditquestionResponseMap = responseMap;
        	templateQuestions        = treedata;
        	driveFileName            = 'audit_profile_' + auditProfileId;
        	authorizeUser();
        }        

        var defineAuditQuestionResponseModel = function(){
        	if(!isCustomModelRegistered){
        		gapi.drive.realtime.custom.registerType(auditQuestionResponseModel, 'auditQuestionResponse');
        		isCustomModelRegistered = true;
        	}        	
        	        	
        	auditQuestionResponseModel.prototype.id = gapi.drive.realtime.custom.collaborativeField('id');
        	auditQuestionResponseModel.prototype.questionResponse = gapi.drive.realtime.custom.collaborativeField('questionResponse');
        	auditQuestionResponseModel.prototype.questionId = gapi.drive.realtime.custom.collaborativeField('questionId');
        	auditQuestionResponseModel.prototype.questionDescription = gapi.drive.realtime.custom.collaborativeField('questionDescription');
        }

        var authorizeUser = function(){
    		realtimeUtils.authorize(loginSuccess, false);
    	} 

        var loginSuccess = function(response){
        	if(!response.error){
        		defineAuditQuestionResponseModel();
        		gapi.client.load('drive', 'v3', searchDriveIfFileExist);
        	}else{
        		console.log('Method:loginSuccess Unable to complete OAuth for current logged in user:' + JSON.stringify(response));
        	}
        }

        /**
         * Search file in the google drive
         */
        var searchDriveIfFileExist = function(){
        	gapi.client.drive.files.list({ 'pageSize': 10, 'fields': 'nextPageToken, files(id, name)' }).execute(loadFileOrCreateFile);
        }
        
        /**
         * If File is found in drive load the file or otherwise create the file in drive
         */
        var loadFileOrCreateFile = function(response){
    		var files = response.files;
    		if (files && files.length > 0) {
    			for (var i = 0; i < files.length; i++) {
    				if(files[i].name == driveFileName){
    					console.log('Method:loadFileOrCreateFile ..searching file ' + driveFileName + ' already exists in drive');
    					driveFileId = files[i].id.replace('/', '');
    					realtimeUtils.load(driveFileId, onFileLoaded, onNewFileCreated);
    					break;	
    				}        				        				
    			}
    		}
    		
    		if(!driveFileId){
    			console.log('Method:loadFileOrCreateFile ..searching file ' + driveFileName + ' need to be CREATED');
    			   			
    			var fileMetadata = { 'name' : driveFileName, 'mimeType' : fileMimeType };
    			gapi.client.drive.files.create({ 'resource': fileMetadata, 'fields': 'id'}).execute(loadDataPostCreation);
    		}
        }
        
        /**
         * Method to load the drive object into memory root and window history
         */
        var loadDataPostCreation = function(creationResponse){
        	if(creationResponse){
        		driveFileId = creationResponse.id;
        		window.history.pushState(null, null, '?id=' + driveFileId);
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
        	auditquestionResponseMapCollab.addEventListener(gapi.drive.realtime.EventType.OBJECT_CHANGED, refreshQuestionResponses);
        }
        
        var refreshQuestionResponses = function(evt){
			  var isValueChange = false;
			  for (var i = 0; i < evt.events.length; i++) {			  
			    if(!evt.events[i].isLocal && (evt.events[i].type ==  'value_changed')){
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
				if(auditquestionResponseMapCollab.has(questionId)){
					templateQuestions[l].response = auditquestionResponseMapCollab.get(questionId);
					attachCollaborateResponseToTemplate(templateQuestions[l].children);					
				}
			}
		}  
		
        return {
        	collaborate                      : collaborate,
        	authorizeUser                    : authorizeUser,
        	searchDriveIfFileExist           : searchDriveIfFileExist
        };
    }
})();
