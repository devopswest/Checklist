(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ChecklistDialogController', ChecklistDialogController);

    ChecklistDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Checklist', 'ChecklistQuestion', 'AuditProfile', 'Country'];

    function ChecklistDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Checklist, ChecklistQuestion, AuditProfile, Country) {
        var vm = this;

        vm.checklist = entity;
        vm.clear = clear;
        vm.save = save;

        vm.auditprofiles = AuditProfile.query();
        vm.countries = Country.query();



        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.checklist.id !== null) {
                Checklist.update(vm.checklist, onSaveSuccess, onSaveError);
            } else {
                Checklist.save(vm.checklist, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:checklistUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }



///Tree
        //ChecklistQuestion.query is incorrect - instead ask Checklist to get child data 
        //It retrieves all rows of ChecklistQuestion table and then creates hierarchy tree
        //So, for example if 2 is child for 1. then using this query it would be pulled twice
        //One time as a child for 1, and second time as next record in the table
        //So instead of this rely on the checklist and ask it get all of its child
        //vm.checklistquestions = ChecklistQuestion.query();
        vm.treedata = [];
        vm.checklist.$promise.then(function (result) {
            vm.treedata = transformToTree(result.checklistQuestions);
            collapseAll();
        });

function transformToTree(result){
         var treedata = [];
         for(var l=0;l<result.length;l++){
             var question = {
                        "id": result[l].id,
                        "title": result[l].code + ":" + result[l].description,
                        "description": result[l].description,
                        "nodes": transformToTree(result[l].children)
                };
                treedata.push(question);
         }
         return treedata;
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

    //collapseAll();


//Editor
vm.editorOptions = {
    // settings more at http://docs.ckeditor.com/#!/guide/dev_configuration
};

vm.current = null;
vm.editorTitle = "";
vm.editorEnabled = false;

vm.openEditor=openEditor;
      function openEditor (scope, node) {

        //scope.$broadcast('ckeditor-visible');
        vm.current=node;
        vm.content=node.title;
        vm.editorEnabled=true;
        vm.editorTitle="Editing [" + node.id + "]";

      };
vm.editorClear=editorClear;
function editorClear() {
  vm.editorEnabled=false;
}


vm.editorSave=editorSave;
function editorSave(scope, node) {
  vm.editorEnabled=false;
  vm.current.title=vm.content;
}



///
    }
})();
