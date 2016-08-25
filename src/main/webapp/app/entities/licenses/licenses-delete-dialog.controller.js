(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('LicensesDeleteController',LicensesDeleteController);

    LicensesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Licenses'];

    function LicensesDeleteController($uibModalInstance, entity, Licenses) {
        var vm = this;

        vm.licenses = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Licenses.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
