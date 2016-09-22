(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ChecklistAnswerDialogController', ChecklistAnswerDialogController);

    ChecklistAnswerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ChecklistAnswer', 'DisclosureRequirement', 'Checklist'];

    function ChecklistAnswerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ChecklistAnswer, DisclosureRequirement, Checklist) {
        var vm = this;

        vm.checklistAnswer = entity;
        vm.clear = clear;
        vm.save = save;
        vm.disclosurerequirements = DisclosureRequirement.query();
        vm.checklists = Checklist.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.checklistAnswer.id !== null) {
                ChecklistAnswer.update(vm.checklistAnswer, onSaveSuccess, onSaveError);
            } else {
                ChecklistAnswer.save(vm.checklistAnswer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:checklistAnswerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
