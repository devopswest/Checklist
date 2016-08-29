(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('LicenseDeleteController',LicenseDeleteController);

    LicenseDeleteController.$inject = ['$uibModalInstance', 'entity', 'License'];

    function LicenseDeleteController($uibModalInstance, entity, License) {
        var vm = this;

        vm.license = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            License.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
