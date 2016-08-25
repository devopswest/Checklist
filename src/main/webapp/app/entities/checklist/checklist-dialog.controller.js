(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ChecklistDialogController', ChecklistDialogController);

    ChecklistDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Checklist', 'ChecklistQuestion', 'AuditProfile', 'Country'];

    function ChecklistDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Checklist, ChecklistQuestion, AuditProfile, Country) {
        var vm = this;

        vm.checklist = entity;
        vm.clear = clear;
        vm.save = save;
        vm.checklistquestions = ChecklistQuestion.query();
        vm.auditprofiles = AuditProfile.query();
        vm.countries = Country.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.checklist.id !== null) {
                Checklist.update(vm.checklist, onSaveSuccess, onSaveError);
            } else {
                Checklist.save(vm.checklist, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:checklistUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
