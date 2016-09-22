(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ChecklistWorkflowDeleteController',ChecklistWorkflowDeleteController);

    ChecklistWorkflowDeleteController.$inject = ['$uibModalInstance', 'entity', 'ChecklistWorkflow'];

    function ChecklistWorkflowDeleteController($uibModalInstance, entity, ChecklistWorkflow) {
        var vm = this;

        vm.checklistWorkflow = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ChecklistWorkflow.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
