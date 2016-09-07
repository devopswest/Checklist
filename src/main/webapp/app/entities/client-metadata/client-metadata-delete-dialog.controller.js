(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ClientMetadataDeleteController',ClientMetadataDeleteController);

    ClientMetadataDeleteController.$inject = ['$uibModalInstance', 'entity', 'ClientMetadata'];

    function ClientMetadataDeleteController($uibModalInstance, entity, ClientMetadata) {
        var vm = this;

        vm.clientMetadata = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ClientMetadata.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
