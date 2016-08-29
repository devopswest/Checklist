(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('WorkflowStepDeleteController',WorkflowStepDeleteController);

    WorkflowStepDeleteController.$inject = ['$uibModalInstance', 'entity', 'WorkflowStep'];

    function WorkflowStepDeleteController($uibModalInstance, entity, WorkflowStep) {
        var vm = this;

        vm.workflowStep = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WorkflowStep.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
