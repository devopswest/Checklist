(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('AuditProfileDialogController', AuditProfileDialogController);

    AuditProfileDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AuditProfile', 'AuditProfileLogEntry', 'Engagement', 'AuditQuestionResponse','ChecklistQuestion'];

    function AuditProfileDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AuditProfile, AuditProfileLogEntry, Engagement, AuditQuestionResponse,ChecklistQuestion) {
        var vm = this;

        vm.auditProfile = entity;
        vm.clear = clear;
        vm.save = save;
        vm.auditprofilelogentries = AuditProfileLogEntry.query();
        vm.engagements = Engagement.query();
        vm.auditquestionresponses = AuditQuestionResponse.query();

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
        vm.checklistquestions = ChecklistQuestion.query();
        vm.treedata = [];
      vm.checklistquestions.$promise.then(function (result) {
        vm.treedata = transformToTree(result);
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


//

    }
})();
