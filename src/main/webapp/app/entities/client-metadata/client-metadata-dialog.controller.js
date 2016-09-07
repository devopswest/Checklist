(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ClientMetadataDialogController', ClientMetadataDialogController);

    ClientMetadataDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ClientMetadata', 'Taxonomy', 'ClientProfile'];

    function ClientMetadataDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ClientMetadata, Taxonomy, ClientProfile) {
        var vm = this;

        vm.clientMetadata = entity;
        vm.clear = clear;
        vm.save = save;
        vm.taxonomies = Taxonomy.query();
        vm.clientprofiles = ClientProfile.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.clientMetadata.id !== null) {
                ClientMetadata.update(vm.clientMetadata, onSaveSuccess, onSaveError);
            } else {
                ClientMetadata.save(vm.clientMetadata, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:clientMetadataUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
