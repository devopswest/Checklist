(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ClientProfileDialogController', ClientProfileDialogController);

    ClientProfileDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'ClientProfile', 'Client', 'ClientMetadata'];

    function ClientProfileDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, ClientProfile, Client, ClientMetadata) {
        var vm = this;

        vm.clientProfile = entity;
        vm.clear = clear;
        vm.save = save;
        vm.clients = Client.query({filter: 'clientprofile-is-null'});
        $q.all([vm.clientProfile.$promise, vm.clients.$promise]).then(function() {
            if (!vm.clientProfile.client || !vm.clientProfile.client.id) {
                return $q.reject();
            }
            return Client.get({id : vm.clientProfile.client.id}).$promise;
        }).then(function(client) {
            vm.clients.push(client);
        });
        vm.clientmetadata = ClientMetadata.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.clientProfile.id !== null) {
                ClientProfile.update(vm.clientProfile, onSaveSuccess, onSaveError);
            } else {
                ClientProfile.save(vm.clientProfile, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:clientProfileUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
