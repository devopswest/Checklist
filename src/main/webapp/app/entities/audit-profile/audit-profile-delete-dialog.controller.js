(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('AuditProfileDeleteController',AuditProfileDeleteController);

    AuditProfileDeleteController.$inject = ['$uibModalInstance', 'entity', 'AuditProfile'];

    function AuditProfileDeleteController($uibModalInstance, entity, AuditProfile) {
        var vm = this;

        vm.auditProfile = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AuditProfile.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
