(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('WorkflowDialogController', WorkflowDialogController);

    WorkflowDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Workflow', 'WorkflowStep'];

    function WorkflowDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Workflow, WorkflowStep) {
        var vm = this;

        vm.workflow = entity;
        vm.clear = clear;
        vm.save = save;
        vm.workflowsteps = WorkflowStep.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.workflow.id !== null) {
                Workflow.update(vm.workflow, onSaveSuccess, onSaveError);
            } else {
                Workflow.save(vm.workflow, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:workflowUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
