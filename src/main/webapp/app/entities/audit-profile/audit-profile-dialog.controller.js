(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('AuditProfileDialogController', AuditProfileDialogController);

    AuditProfileDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AuditProfile', 'AuditProfileLogEntry', 'Engagement', 'AuditQuestionResponse','ChecklistQuestion', 'Checklist'];

    function AuditProfileDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AuditProfile, AuditProfileLogEntry, Engagement, AuditQuestionResponse,ChecklistQuestion, Checklist ) {
        var vm = this;

        vm.auditProfile = entity;
        vm.clear = clear;
        vm.save = save;
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
            if (vm.auditProfile.id !== null) {
                AuditProfile.update(vm.auditProfile, onSaveSuccess, onSaveError);
            } else {
                AuditProfile.save(vm.auditProfile, onSaveSuccess, onSaveError);
            }
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
        vm.maxid = 0;
        vm.checklistId = 0;
        vm.checklistName = "";
        vm.engagementId = 0;
        vm.auditquestionresponses = [];
        vm.auditProfile.$promise.then( function (result){
        	vm.engagementId = result.engagementId;
        	vm.auditquestionresponses = result.auditQuestionResponses;
        	
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
    			console.log(checkListResult);
        		vm.treedata = checkListResult.checklistQuestions;
    			setChecklistIdAndName(vm.treedata);
    			collapseAll();
        	});
    	}
		function setChecklistIdAndName(node){
			for(var l=0;l<node.length;l++){
				vm.checklistId = node[l].checklistId;
		        vm.checklistName = node[l].checklistName;
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
vm.addQuestion=addQuestion;
    function addQuestion () {
        var newQuestion = {
                "id": 'id ' + vm.treedata.length + 1,
                "title": 'title ' + vm.treedata.length + 1,
                "nodes": []
                    };
        vm.treedata.push(newQuestion);
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
