(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ClientTagDeleteController',ClientTagDeleteController);

    ClientTagDeleteController.$inject = ['$uibModalInstance', 'entity', 'ClientTag'];

    function ClientTagDeleteController($uibModalInstance, entity, ClientTag) {
        var vm = this;

        vm.clientTag = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ClientTag.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
