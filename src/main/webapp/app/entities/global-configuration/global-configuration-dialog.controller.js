(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('GlobalConfigurationDialogController', GlobalConfigurationDialogController);

    GlobalConfigurationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'GlobalConfiguration'];

    function GlobalConfigurationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, GlobalConfiguration) {
        var vm = this;

        vm.globalConfiguration = entity;
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
            if (vm.globalConfiguration.id !== null) {
                GlobalConfiguration.update(vm.globalConfiguration, onSaveSuccess, onSaveError);
            } else {
                GlobalConfiguration.save(vm.globalConfiguration, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:globalConfigurationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
