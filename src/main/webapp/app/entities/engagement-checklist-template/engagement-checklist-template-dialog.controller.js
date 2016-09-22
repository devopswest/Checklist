(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('EngagementChecklistTemplateDialogController', EngagementChecklistTemplateDialogController);

    EngagementChecklistTemplateDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'EngagementChecklistTemplate', 'ChecklistTemplate', 'Workflow', 'Engagement'];

    function EngagementChecklistTemplateDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, EngagementChecklistTemplate, ChecklistTemplate, Workflow, Engagement) {
        var vm = this;

        vm.engagementChecklistTemplate = entity;
        vm.clear = clear;
        vm.save = save;
        vm.checklisttemplates = ChecklistTemplate.query();
        vm.workflows = Workflow.query();
        vm.engagements = Engagement.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.engagementChecklistTemplate.id !== null) {
                EngagementChecklistTemplate.update(vm.engagementChecklistTemplate, onSaveSuccess, onSaveError);
            } else {
                EngagementChecklistTemplate.save(vm.engagementChecklistTemplate, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:engagementChecklistTemplateUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
