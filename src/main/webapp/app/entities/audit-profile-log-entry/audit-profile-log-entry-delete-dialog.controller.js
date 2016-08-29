(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('AuditProfileLogEntryDeleteController',AuditProfileLogEntryDeleteController);

    AuditProfileLogEntryDeleteController.$inject = ['$uibModalInstance', 'entity', 'AuditProfileLogEntry'];

    function AuditProfileLogEntryDeleteController($uibModalInstance, entity, AuditProfileLogEntry) {
        var vm = this;

        vm.auditProfileLogEntry = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AuditProfileLogEntry.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
