(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('EngagementDialogController', EngagementDialogController);

    EngagementDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Engagement', 'EngagementMember', 'Client', 'Checklist', 'Workflow'];

    function EngagementDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Engagement, EngagementMember, Client, Checklist, Workflow) {
        var vm = this;

        vm.engagement = entity;
        vm.clear = clear;
        vm.save = save;
        vm.engagementmembers = EngagementMember.query();
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
            if (vm.engagement.id !== null) {
                Engagement.update(vm.engagement, onSaveSuccess, onSaveError);
            } else {
                Engagement.save(vm.engagement, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:engagementUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

///NEW
vm.children1 = [
  {
    "id": 1,
    "title1": "Mickey Mouse",
    "title2": "PWC_ROLE_ENGAGEMENT_TEAM"
  },
  {
    "id": 2,
    "title1": "Donal Duck",
    "title2": "PWC_ROLE_ADMIN"
  },
  {
    "id": 3,
    "title1": "Minie Mouse",
    "title2": "PWC_ROLE_MANAGER"
  }
];


vm.children2 = [
  {
    "id": 1,
    "title1": "Smaller Registrants (Form 10-K) and Other Public Entities",
    "title2": "3-STEP-APPROVAL"
  },
  {
    "id": 1,
    "title1": "Suplemental Checklist",
    "title2": "2-STEP-APPROVAL"
  },
];


vm.newItem1 = function () {
        var nodeData = vm.children[vm.children.length - 1];
        vm.children.push({
          id: vm.children.length + 1,
          title1: 'node ' + (vm.children.length + 1),
          title2: 'node ' + (vm.children.length + 1)
        });
      };


vm.removeItem1 = function (scope) {
        scope.remove();
      };


vm.newItem2 = function () {
        var nodeData = vm.children[vm.children.length - 1];
        vm.children.push({
          id: vm.children.length + 1,
          title1: 'node ' + (vm.children.length + 1),
          title2: 'node ' + (vm.children.length + 1)
        });
      };


vm.removeItem2 = function (scope) {
        scope.remove();
      };



///NEW


    }
})();
