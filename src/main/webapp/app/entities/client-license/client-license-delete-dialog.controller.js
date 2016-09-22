(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ClientLicenseDeleteController',ClientLicenseDeleteController);

    ClientLicenseDeleteController.$inject = ['$uibModalInstance', 'entity', 'ClientLicense'];

    function ClientLicenseDeleteController($uibModalInstance, entity, ClientLicense) {
        var vm = this;

        vm.clientLicense = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ClientLicense.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
