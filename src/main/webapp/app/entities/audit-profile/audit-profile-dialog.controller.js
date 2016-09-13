(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('AuditProfileDialogController', AuditProfileDialogController);

    AuditProfileDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AuditProfile', 'AuditProfileRollover', 'AuditProfileLogEntry', 'Engagement', 'AuditQuestionResponse','ChecklistQuestion', 'Checklist'];

    function AuditProfileDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AuditProfile, AuditProfileRollover, AuditProfileLogEntry, Engagement, AuditQuestionResponse,ChecklistQuestion, Checklist ) {
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
            clearBlankResponses();
            if (vm.auditProfile.id !== null) {
                AuditProfile.update(vm.auditProfile, onSaveSuccess, onSaveError);
            } else {
                AuditProfile.save(vm.auditProfile, onSaveSuccess, onSaveError);
            }
        }   

        function clearBlankResponses(){
        	var saveQuestionResponse = [];
    		for(var l=0;l<vm.auditProfile.auditQuestionResponses.length;l++){
    			if(vm.auditProfile.auditQuestionResponses[l].questionResponse != null){
    				saveQuestionResponse.push(vm.auditProfile.auditQuestionResponses[l]);
    			}
    		}
    		vm.auditProfile.auditQuestionResponses = saveQuestionResponse;
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

///Tree
        vm.treedata = [];
        vm.engagementId = 0;
        vm.auditQuestionResponses = [];
        vm.auditquestionResponseMap = {};
        vm.maxResponseId = 0;
        vm.auditProfile.$promise.then( function (result){
        	vm.engagementId = result.engagementId;
        	vm.auditQuestionResponses = result.auditQuestionResponses; 
        	
        	vm.engagements.$promise.then(function (engagementsResult) {
                for(var l=0;l<engagementsResult.length;l++){
                	if(vm.engagementId == engagementsResult[l].id){  
                		vm.loadChecklist(engagementsResult[l].checklist.id);
                	}
                }    		
        	});
        	
        });       
        

    	vm.loadChecklist = function loadChecklist(cid){
    		Checklist.loadQuestions({"id":cid}).$promise.then(function (checkListResult) {
        		vm.treedata = checkListResult.checklistQuestions;
        		convertResponsesToMap();
    			setResponsesOnLoad(vm.treedata);
    			vm.auditProfile.auditQuestionResponses = vm.auditQuestionResponses;
    			collapseAll();
        	});
    	}  	
    	
    	
		var convertResponsesToMap = function convertResponsesToMap(){
			var responses = vm.auditQuestionResponses;
			for(var l=0;l<responses.length;l++){
				vm.auditquestionResponseMap[responses[l].questionId] = responses[l];
				if(vm.maxResponseId < responses[l].id){
    				vm.maxResponseId = responses[l].id;
    			}
			}	
		}		
		
		var setResponsesOnLoad = function setResponsesOnLoad(node){	
			for(var l=0;l<node.length;l++){
				if(vm.auditquestionResponseMap[node[l].id] == undefined){
					var newQuestionResponse = {
							id:null,
							questionResponse: null,
							questionId: node[l].id
					}
					vm.auditQuestionResponses.push(newQuestionResponse);
					vm.auditquestionResponseMap[node[l].id] = newQuestionResponse;										
				}
				
				node[l].response = vm.auditquestionResponseMap[node[l].id];
				setResponsesOnLoad(node[l].children);
			}
		}
		
vm.updateResponse = updateResponse;
function updateResponse(node,btnValue){
	updateChildResponses(node,btnValue);
	//Revalidate parent Response
	updateParentNode(vm.treedata,node.parentId);
		
}

function updateChildResponses(node,btnValue){	
	node.response.questionResponse = btnValue;	
	if(node.children.length > 0){
		for( var l=0;l<node.children.length;l++){
			updateChildResponses(node.children[l],btnValue);
		}
	}
}
	
function updateParentNode(node,parentId,btnValue){	
	for(var l=0; l<node.length; l++){
		if(node[l].id == parentId){	
			if(node[l].response != btnValue){				
				node[l].response.questionResponse = "";
			}
		}
		
		//If not found check-in child nodes
		updateParentNode(node[l].children,parentId,btnValue);
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
