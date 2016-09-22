(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ClientTagDialogController', ClientTagDialogController);

    ClientTagDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ClientTag', 'Taxonomy', 'Client'];

    function ClientTagDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ClientTag, Taxonomy, Client) {
        var vm = this;

        vm.clientTag = entity;
        vm.clear = clear;
        vm.save = save;
        vm.taxonomies = Taxonomy.query();
        vm.clients = Client.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.clientTag.id !== null) {
                ClientTag.update(vm.clientTag, onSaveSuccess, onSaveError);
            } else {
                ClientTag.save(vm.clientTag, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:clientTagUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
