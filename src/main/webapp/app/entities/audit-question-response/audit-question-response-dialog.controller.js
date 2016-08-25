(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('AuditQuestionResponseDialogController', AuditQuestionResponseDialogController);

    AuditQuestionResponseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AuditQuestionResponse', 'ChecklistQuestion', 'AuditProfile'];

    function AuditQuestionResponseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AuditQuestionResponse, ChecklistQuestion, AuditProfile) {
        var vm = this;

        vm.auditQuestionResponse = entity;
        vm.clear = clear;
        vm.save = save;
        vm.checklistquestions = ChecklistQuestion.query();
        vm.auditprofiles = AuditProfile.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.auditQuestionResponse.id !== null) {
                AuditQuestionResponse.update(vm.auditQuestionResponse, onSaveSuccess, onSaveError);
            } else {
                AuditQuestionResponse.save(vm.auditQuestionResponse, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:auditQuestionResponseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
