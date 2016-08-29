(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('WorkflowDeleteController',WorkflowDeleteController);

    WorkflowDeleteController.$inject = ['$uibModalInstance', 'entity', 'Workflow'];

    function WorkflowDeleteController($uibModalInstance, entity, Workflow) {
        var vm = this;

        vm.workflow = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Workflow.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
