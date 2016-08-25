(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('AuditQuestionResponseDeleteController',AuditQuestionResponseDeleteController);

    AuditQuestionResponseDeleteController.$inject = ['$uibModalInstance', 'entity', 'AuditQuestionResponse'];

    function AuditQuestionResponseDeleteController($uibModalInstance, entity, AuditQuestionResponse) {
        var vm = this;

        vm.auditQuestionResponse = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AuditQuestionResponse.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
