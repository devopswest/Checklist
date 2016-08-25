(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('LicenseTypeDeleteController',LicenseTypeDeleteController);

    LicenseTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'LicenseType'];

    function LicenseTypeDeleteController($uibModalInstance, entity, LicenseType) {
        var vm = this;

        vm.licenseType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            LicenseType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
