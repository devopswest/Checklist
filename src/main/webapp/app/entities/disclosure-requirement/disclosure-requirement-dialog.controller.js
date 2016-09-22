(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('DisclosureRequirementDialogController', DisclosureRequirementDialogController);

    DisclosureRequirementDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DisclosureRequirement', 'DisclosureRequirementTag', 'ChecklistTemplate', 'Requirement'];

    function DisclosureRequirementDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DisclosureRequirement, DisclosureRequirementTag, ChecklistTemplate, Requirement) {
        var vm = this;

        vm.disclosureRequirement = entity;
        vm.clear = clear;
        vm.save = save;
        vm.disclosurerequirements = DisclosureRequirement.query();
        vm.disclosurerequirementtags = DisclosureRequirementTag.query();
        vm.checklisttemplates = ChecklistTemplate.query();
        vm.requirements = Requirement.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.disclosureRequirement.id !== null) {
                DisclosureRequirement.update(vm.disclosureRequirement, onSaveSuccess, onSaveError);
            } else {
                DisclosureRequirement.save(vm.disclosureRequirement, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:disclosureRequirementUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
