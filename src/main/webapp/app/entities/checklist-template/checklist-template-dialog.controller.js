(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ChecklistTemplateDialogController', ChecklistTemplateDialogController);

    ChecklistTemplateDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ChecklistTemplate', 'DisclosureRequirement', 'Taxonomy'];

    function ChecklistTemplateDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ChecklistTemplate, DisclosureRequirement, Taxonomy) {
        var vm = this;

        vm.checklistTemplate = entity;
        vm.clear = clear;
        vm.save = save;
        vm.disclosurerequirements = DisclosureRequirement.query();
        vm.taxonomies = Taxonomy.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.checklistTemplate.id !== null) {
                ChecklistTemplate.update(vm.checklistTemplate, onSaveSuccess, onSaveError);
            } else {
                ChecklistTemplate.save(vm.checklistTemplate, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:checklistTemplateUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
