(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('LicenseDialogController', LicenseDialogController);

    LicenseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'License', 'Client', 'Taxonomy'];

    function LicenseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, License, Client, Taxonomy) {
        var vm = this;

        vm.license = entity;
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
            if (vm.license.id !== null) {
                License.update(vm.license, onSaveSuccess, onSaveError);
            } else {
                License.save(vm.license, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:licenseUpdate', result);
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
