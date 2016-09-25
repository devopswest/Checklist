(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('AuditProfileDialogController', AuditProfileDialogController);

    AuditProfileDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AuditProfile', 'AuditProfileRollover', 'AuditProfileLogEntry', 'Engagement', 'AuditQuestionResponse','AuditProfileRealtime', 'ChecklistQuestion', 'Checklist'];

    function AuditProfileDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AuditProfile, AuditProfileRollover, AuditProfileLogEntry, Engagement, AuditQuestionResponse, AuditProfileRealtime, ChecklistQuestion, Checklist ) {
        var vm = this;

        vm.auditProfile = entity;
        vm.clear = clear;
        vm.save = save;
        vm.rollover = rollover;
        vm.auditprofilelogentries = AuditProfileLogEntry.query();
        vm.engagements = Engagement.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.auditProfile.id != null) {            	
            	var saveQuestionResponse = [];
            	createResponseArray(saveQuestionResponse, vm.questionTemplate);
            	vm.auditProfile.auditQuestionResponses = saveQuestionResponse;
                AuditProfile.update(vm.auditProfile, onSaveSuccess, onSaveError);
            } else {
                AuditProfile.save(vm.auditProfile, onSaveSuccess, onSaveError);
            }
        }   

        function createResponseArray(saveQuestionResponse, node){
    		for(var l=0;l<node.length;l++){    			
    			if(node[l].response.questionResponse != null){
    				//There was cyclic conversion error when directly passing response object. 
    				//Hence created a clone to avoid this error
    				var qResp = {
    						'id' : node[l].response.id,
    						'questionResponse' : node[l].response.questionResponse,
    						'questionId':node[l].response.questionId,
    						'questionDescription':node[l].response.questionDescription
    				}
    				saveQuestionResponse.push(qResp);
    			}
    			createResponseArray(saveQuestionResponse, node[l].children);
    		}    		
    	}
        
        function rollover(){
        	AuditProfileRollover.rollover({id:vm.auditProfile.id}, onSaveSuccess, onSaveError);
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:auditProfileUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
        
        //Logic to display template questions and their responses
        //Steps:- 
        //Step 1: From auditProfile retrieve engagement Id and answered (responded) questions and responses
        //Step 2: To present complete list of template questions, use enagegementId, then find the template ID (i.e., in DB it is checklistId)
        //Step 3: Retrieve all the template questions based the templateId
        //Step 4: Create empty placeholder for answers not found in auditProfile
        //Step 5: Link the responses stored in database and add them to tree
        //Step 6: Collapse tree when started
        vm.questionTemplate = [];
        vm.engagementId = 0;
        vm.auditQuestionResponses = [];
        vm.auditquestionResponseMap = {};
        vm.dirtyQuestionResponsesMap = {}
        vm.maxResponseId = 0;
        vm.isCollabrate = false;
        vm.isSignedIn = false;
      	
        //Step 1
        if (vm.auditProfile.engagementId) {
        	vm.auditProfile.$promise.then( function (auditProfileResult){
        		vm.engagementId = auditProfileResult.engagementId;
        		vm.auditQuestionResponses = auditProfileResult.auditQuestionResponses; 
        		convertResponsesToMap();
        		
        		//Step 2
        		vm.engagements.$promise.then(function (engagementResult) {
            		for (var i = 0; i < engagementResult.length; i++) {
            			if (vm.engagementId == engagementResult[i].id) {
            				//Step 3
            				Checklist.loadQuestions({"id": engagementResult[i].checklist.id}).$promise.then(function (templateQuestionResult) {
            				vm.questionTemplate = templateQuestionResult.checklistQuestions; 
            				//Step 4
            				createEmptyResponseForMissingQuestion(vm.questionTemplate);
            				//Step 5
            				setResponsesOnLoad(vm.questionTemplate);
            				//Step 6
            				collapseAll();
            				});
            			}
            		}            		
            	});
        		
        	});       
        }
        
        /**
         * On Loading the tree, show the responses which are stored in database. 
         * Later when user clicks collaborate button these changes would be merged with drive changes 
         */
        var setResponsesOnLoad = function setResponsesOnLoad(node){	
			for(var l=0;l<node.length;l++){
				node[l].response = vm.auditquestionResponseMap[node[l].id];
				setResponsesOnLoad(node[l].children);
			}
		}

    	/**
    	 * Prepares and HashMap of the Responses Array for easy retrieval
    	 */
		var convertResponsesToMap = function convertResponsesToMap(){
			vm.dirtyQuestionResponsesMap = {};
			for(var l=0;l<vm.auditQuestionResponses.length;l++){
				vm.auditquestionResponseMap[vm.auditQuestionResponses[l].questionId] = vm.auditQuestionResponses[l];
				if(vm.maxResponseId < vm.auditQuestionResponses[l].id){
    				vm.maxResponseId = vm.auditQuestionResponses[l].id;
    			}
			}	
		}	
		
		/**
		 * A recursive method to prepare empty responses for template questions which are not yet answered.
		 * These empty responses act as place holder when the template questions are displayed and buttons are toggled
		 */		
		var createEmptyResponseForMissingQuestion = function createEmptyResponseForMissingQuestion(node){	
			for(var l=0;l<node.length;l++){
				if(!vm.auditquestionResponseMap[node[l].id]){
					var newQuestionResponse = {
							id:null,
							questionResponse: null,
							questionId: node[l].id
					}
					vm.auditquestionResponseMap[node[l].id] = newQuestionResponse;										
				}				
				createEmptyResponseForMissingQuestion(node[l].children);
			}
		}


		
		/**
		 * Logic to update child and parent nodes based on new changed value
		 */
		vm.updateResponse = updateResponse;
		function updateResponse(node,btnValue){
			updateChildResponses(node,btnValue);
			//Revalidate parent Response
			updateParentNode(vm.questionTemplate,node.parentId);
				
		}

		/**
		 * Recursively traverse child arrays and update current selection
		 */
		function updateChildResponses(node,currentSelection){
			vm.dirtyQuestionResponsesMap[node.response.questionId] = currentSelection;
			node.response.questionResponse = currentSelection;	
			if(node.children.length > 0){
				for( var l=0;l<node.children.length;l++){
					updateChildResponses(node.children[l],currentSelection);
				}
			}
		}
	
		/**
		 * Turn off parent selection criteria if any child deviated their state from parent
		 */
		function updateParentNode(node,parentId,currentSelection){	
			for(var l=0; l<node.length; l++){
				if(node[l].id == parentId){	
					if(node[l].response != currentSelection){				
						node[l].response.questionResponse = null;
					}
				}
				
				//If not found check-in child nodes
				updateParentNode(node[l].children,parentId,currentSelection);
			}
		}
		
		vm.remove=remove;
		function remove (scope) {
		        scope.remove();
		      };
		vm.toggle=toggle;
		      function toggle (scope) {
		        scope.toggle();
		      };
		vm.moveLastToTheBeginning=moveLastToTheBeginning;
		      function moveLastToTheBeginning () {
		        var a = $scope.data.pop();
		        $scope.data.splice(0, 0, a);
		      };
		vm.newSubItem=newSubItem;
		      function newSubItem (scope) {
		        var nodeData = scope.$modelValue;
		        nodeData.nodes.push({
		          id: nodeData.id * 10 + nodeData.nodes.length,
		          title: nodeData.title + '.' + (nodeData.nodes.length + 1),
		          nodes: []
		        });
		        scope.collapsed = false;
		      };
		vm.collapseAll=collapseAll;
		      function collapseAll () {
		        $scope.$broadcast('angular-ui-tree:collapse-all');
		      };
		vm.expandAll=expandAll;
		     function expandAll () {
		        $scope.$broadcast('angular-ui-tree:expand-all');
		      };
		
		vm.handleCollaborateOn=handleCollaborateOn;
		function handleCollaborateOn(){
			vm.isCollabrate = AuditProfileRealtime.collaborate(vm.auditProfile.id, vm.auditProfile.responseFileId, vm.auditquestionResponseMap, vm.questionTemplate, vm.dirtyQuestionResponsesMap);
		}
		
		vm.handleCollaborateOFF=handleCollaborateOFF;
		function handleCollaborateOFF(){
			vm.isCollabrate = AuditProfileRealtime.stopCollaborate();
		}
		
		//Editor
		vm.editorOptions = {
		    // settings more at http://docs.ckeditor.com/#!/guide/dev_configuration
		};
		
		vm.current = null;
		vm.editorTitle = "";
		vm.editorEnabled = false;
		
		vm.openEditor=openEditor;
		function openEditor (scope, node) {
		    vm.current=node;
		    vm.content=node.description;
		    vm.editorEnabled=true;
		    vm.editorTitle="Editing [" + node.code + "]";
		};
		
		vm.editorClear=editorClear;
		function editorClear() {
		  vm.editorEnabled=false;
		}
		
		vm.editorSave=editorSave;
		function editorSave(scope, node) {
		  vm.editorEnabled=false;
		  vm.current.description=vm.content;
		}
		//End of Editor


    }
})();
