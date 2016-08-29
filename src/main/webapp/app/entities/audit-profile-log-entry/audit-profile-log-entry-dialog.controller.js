(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('AuditProfileLogEntryDialogController', AuditProfileLogEntryDialogController);

    AuditProfileLogEntryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'AuditProfileLogEntry', 'AuditProfile', 'User'];

    function AuditProfileLogEntryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, AuditProfileLogEntry, AuditProfile, User) {
        var vm = this;

        vm.auditProfileLogEntry = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.auditprofiles = AuditProfile.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.auditProfileLogEntry.id !== null) {
                AuditProfileLogEntry.update(vm.auditProfileLogEntry, onSaveSuccess, onSaveError);
            } else {
                AuditProfileLogEntry.save(vm.auditProfileLogEntry, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:auditProfileLogEntryUpdate', result);
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
