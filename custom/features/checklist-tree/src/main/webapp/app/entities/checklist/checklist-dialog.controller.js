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
        vm.checklistquestions = ChecklistQuestion.query();
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
vm.treedata =
[
  {
    "id": "SECTION A",
    "title": "INTRODUCTION",
    "nodes": [
      {
        "id": "1.1",
        "title": "node1.1",
        "nodes": [
          {
            "id": "1.1.1",
            "title": "node1.1.1",
            "nodes": []
          }
        ]
      },
      {
        "id": "1.2",
        "title": "node1.2",
        "nodes": []
      }
    ]
  },
  {
    "id": "SECTION B",
    "title": "BALANCE SHEET",
    "nodrop": true,
    "nodes": [
      {
        "id": "2.1",
        "title": "node2.1",
        "nodes": []
      },
      {
        "id": "2.2",
        "title": "node2.2",
        "nodes": []
      }
    ]
  },
  {
    "id": "SECTION C",
    "title": "INCOME STATEMENT",
    "nodes": [
      {
        "id": "3.1",
        "title": "node3.1",
        "nodes": []
      }
    ]
  },
  {
    "id": "SECTION D",
    "title": "STATEMENT OF CASH FLOWS",
    "nodes": [
      {
        "id": "3.1",
        "title": "node3.1",
        "nodes": []
      }
    ]
  },
  {
    "id": "SECTION E",
    "title": "STATEMENT OF STAKEHOLDERS EQUITY",
    "nodes": [
      {
        "id": "3.1",
        "title": "node3.1",
        "nodes": []
      }
    ]
  },
  {
    "id": "SECTION F",
    "title": "COMPRENHENSIVE INCOME",
    "nodes": [
      {
        "id": "3.1",
        "title": "node3.1",
        "nodes": []
      }
    ]
  },
  {
    "id": "SECTION G",
    "title": "OTHER STATEMENT FINANCIAL DISCLOSURES",
    "nodes": [
      {
        "id": "3.1",
        "title": "node3.1",
        "nodes": []
      }
    ]
  },
  {
    "id": "SECTION H",
    "title": "OTHER INFORMATION REQUIRED IN ANNUAL REPORT TO STAKEHOLDERS",
    "nodes": [
      {
        "id": "3.1",
        "title": "node3.1",
        "nodes": []
      }
    ]
  },
  {
    "id": "SECTION I",
    "title": "INFORMATION TO BE FURNISHED",
    "nodes": [
      {
        "id": "3.1",
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
