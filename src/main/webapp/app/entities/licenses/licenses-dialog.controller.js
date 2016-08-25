(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('LicensesDialogController', LicensesDialogController);

    LicensesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Licenses', 'Company'];

    function LicensesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Licenses, Company) {
        var vm = this;

        vm.licenses = entity;
        vm.clear = clear;
        vm.save = save;
        vm.companies = Company.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.licenses.id !== null) {
                Licenses.update(vm.licenses, onSaveSuccess, onSaveError);
            } else {
                Licenses.save(vm.licenses, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:licensesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
