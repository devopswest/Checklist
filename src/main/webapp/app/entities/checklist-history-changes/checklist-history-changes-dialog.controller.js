(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ChecklistHistoryChangesDialogController', ChecklistHistoryChangesDialogController);

    ChecklistHistoryChangesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ChecklistHistoryChanges', 'Checklist', 'User'];

    function ChecklistHistoryChangesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ChecklistHistoryChanges, Checklist, User) {
        var vm = this;

        vm.checklistHistoryChanges = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.checklists = Checklist.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.checklistHistoryChanges.id !== null) {
                ChecklistHistoryChanges.update(vm.checklistHistoryChanges, onSaveSuccess, onSaveError);
            } else {
                ChecklistHistoryChanges.save(vm.checklistHistoryChanges, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:checklistHistoryChangesUpdate', result);
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
