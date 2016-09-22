(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ChecklistWorkflowDialogController', ChecklistWorkflowDialogController);

    ChecklistWorkflowDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ChecklistWorkflow', 'Checklist', 'User', 'Workflow', 'ChecklistAnswer'];

    function ChecklistWorkflowDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ChecklistWorkflow, Checklist, User, Workflow, ChecklistAnswer) {
        var vm = this;

        vm.checklistWorkflow = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.checklists = Checklist.query();
        vm.users = User.query();
        vm.workflows = Workflow.query();
        vm.checklistanswers = ChecklistAnswer.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.checklistWorkflow.id !== null) {
                ChecklistWorkflow.update(vm.checklistWorkflow, onSaveSuccess, onSaveError);
            } else {
                ChecklistWorkflow.save(vm.checklistWorkflow, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:checklistWorkflowUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.happened = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
