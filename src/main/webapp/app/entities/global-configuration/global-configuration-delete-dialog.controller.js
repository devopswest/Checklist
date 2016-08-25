(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('GlobalConfigurationDeleteController',GlobalConfigurationDeleteController);

    GlobalConfigurationDeleteController.$inject = ['$uibModalInstance', 'entity', 'GlobalConfiguration'];

    function GlobalConfigurationDeleteController($uibModalInstance, entity, GlobalConfiguration) {
        var vm = this;

        vm.globalConfiguration = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            GlobalConfiguration.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
