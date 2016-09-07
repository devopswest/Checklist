(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ClientDialogController', ClientDialogController);

    ClientDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Client'];

    function ClientDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Client) {
        var vm = this;

        vm.client = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.client.id !== null) {
                Client.update(vm.client, onSaveSuccess, onSaveError);
            } else {
                Client.save(vm.client, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:clientUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

///NEW
vm.children = [
  {
    "id": 1,
    "title1": "LOS",
    "title2": "Assurance"
  },
  {
    "id": 2,
    "title1": "Company Type",
    "title2": "Public Sector"
  },
  {
    "id": 3,
    "title1": "Company Size",
    "title2": "500"
  }
];


vm.newItem = function () {
        var nodeData = vm.children[vm.children.length - 1];
        vm.children.push({
          id: vm.children.length + 1,
          title1: 'node ' + (vm.children.length + 1),
          title2: 'node ' + (vm.children.length + 1)
        });
      };


vm.removeItem = function (scope) {
        scope.remove();
      };


///NEW


    }
})();
