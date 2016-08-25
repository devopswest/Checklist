(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('LicenseTypeDialogController', LicenseTypeDialogController);

    LicenseTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'LicenseType'];

    function LicenseTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, LicenseType) {
        var vm = this;

        vm.licenseType = entity;
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
            if (vm.licenseType.id !== null) {
                LicenseType.update(vm.licenseType, onSaveSuccess, onSaveError);
            } else {
                LicenseType.save(vm.licenseType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:licenseTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
