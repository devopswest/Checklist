(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('CountryDialogController', CountryDialogController);

    CountryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Country', 'Checklist'];

    function CountryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Country, Checklist) {
        var vm = this;

        vm.country = entity;
        vm.clear = clear;
        vm.save = save;
        vm.checklists = Checklist.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.country.id !== null) {
                Country.update(vm.country, onSaveSuccess, onSaveError);
            } else {
                Country.save(vm.country, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:countryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
