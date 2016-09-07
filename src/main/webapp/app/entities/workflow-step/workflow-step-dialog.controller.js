(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('WorkflowStepDialogController', WorkflowStepDialogController);

    WorkflowStepDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WorkflowStep', 'Template', 'Workflow'];

    function WorkflowStepDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WorkflowStep, Template, Workflow) {
        var vm = this;

        vm.workflowStep = entity;
        vm.clear = clear;
        vm.save = save;
        vm.templates = Template.query();
        vm.workflows = Workflow.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.workflowStep.id !== null) {
                WorkflowStep.update(vm.workflowStep, onSaveSuccess, onSaveError);
            } else {
                WorkflowStep.save(vm.workflowStep, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:workflowStepUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
