(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ChecklistQuestionDialogController', ChecklistQuestionDialogController);

    ChecklistQuestionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ChecklistQuestion', 'Checklist'];

    function ChecklistQuestionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ChecklistQuestion, Checklist) {
        var vm = this;

        vm.checklistQuestion = entity;
        vm.clear = clear;
        vm.save = save;
        vm.checklistquestions = ChecklistQuestion.query();
        vm.checklists = Checklist.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.checklistQuestion.id !== null) {
                ChecklistQuestion.update(vm.checklistQuestion, onSaveSuccess, onSaveError);
            } else {
                ChecklistQuestion.save(vm.checklistQuestion, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:checklistQuestionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
