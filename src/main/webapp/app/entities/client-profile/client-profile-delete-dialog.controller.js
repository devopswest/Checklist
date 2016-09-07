(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ClientProfileDeleteController',ClientProfileDeleteController);

    ClientProfileDeleteController.$inject = ['$uibModalInstance', 'entity', 'ClientProfile'];

    function ClientProfileDeleteController($uibModalInstance, entity, ClientProfile) {
        var vm = this;

        vm.clientProfile = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ClientProfile.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
