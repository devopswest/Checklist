(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('EngagementDialogController', EngagementDialogController);

    EngagementDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Engagement', 'AuditProfile', 'EngagementMember', 'Client'];

    function EngagementDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Engagement, AuditProfile, EngagementMember, Client) {
        var vm = this;

        vm.engagement = entity;
        vm.clear = clear;
        vm.save = save;
        vm.auditprofiles = AuditProfile.query();
        vm.engagementmembers = EngagementMember.query();
        vm.clients = Client.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.engagement.id !== null) {
                Engagement.update(vm.engagement, onSaveSuccess, onSaveError);
            } else {
                Engagement.save(vm.engagement, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:engagementUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();