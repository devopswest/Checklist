(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ClientLicenseDialogController', ClientLicenseDialogController);

    ClientLicenseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ClientLicense', 'Client', 'Taxonomy'];

    function ClientLicenseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ClientLicense, Client, Taxonomy) {
        var vm = this;

        vm.clientLicense = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.clients = Client.query();
        vm.taxonomies = Taxonomy.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.clientLicense.id !== null) {
                ClientLicense.update(vm.clientLicense, onSaveSuccess, onSaveError);
            } else {
                ClientLicense.save(vm.clientLicense, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:clientLicenseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.expirationDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
