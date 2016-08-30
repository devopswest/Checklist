(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('AuditProfileDialogController', AuditProfileDialogController);

    AuditProfileDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AuditProfile', 'AuditProfileLogEntry', 'ChecklistQuestion', 'AuditQuestionResponse', 'Client', 'Checklist', 'Workflow'];

    function AuditProfileDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AuditProfile, AuditProfileLogEntry, ChecklistQuestion, AuditQuestionResponse, Client, Checklist, Workflow) {
        var vm = this;

        vm.auditProfile = entity;
        vm.clear = clear;
        vm.save = save;
        vm.auditprofilelogentries = AuditProfileLogEntry.query();
        vm.checklistquestions = ChecklistQuestion.query();
        vm.auditquestionresponses = AuditQuestionResponse.query();
        vm.clients = Client.query();
        vm.checklists = Checklist.query();
        vm.workflows = Workflow.query();

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
vm.treedata =
[
  {
    "id": 1,
    "title": "node1",
    "nodes": [
      {
        "id": 11,
        "title": "node1.1",
        "nodes": [
          {
            "id": 111,
            "title": "node1.1.1",
            "nodes": []
          }
        ]
      },
      {
        "id": 12,
        "title": "node1.2",
        "nodes": []
      }
    ]
  },
  {
    "id": 2,
    "title": "node2",
    "nodrop": true,
    "nodes": [
      {
        "id": 21,
        "title": "node2.1",
        "nodes": []
      },
      {
        "id": 22,
        "title": "node2.2",
        "nodes": []
      }
    ]
  },
  {
    "id": 3,
    "title": "node3",
    "nodes": [
      {
        "id": 31,
        "title": "node3.1",
        "nodes": []
      }
    ]
  }
];

vm.remove=remove;
function remove (scope) {
        scope.remove();
      };
vm.toggle=toggle;
      function toggle (scope) {
        scope.toggle();
      };

vm.collapseAll=collapseAll;
      function collapseAll () {
        $scope.$broadcast('angular-ui-tree:collapse-all');
      };
vm.expandAll=expandAll;
     function expandAll () {
        $scope.$broadcast('angular-ui-tree:expand-all');
      };


    }
})();
