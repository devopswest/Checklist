(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('EngagementMemberDialogController', EngagementMemberDialogController);

    EngagementMemberDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'EngagementMember', 'User', 'Engagement'];

    function EngagementMemberDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, EngagementMember, User, Engagement) {
        var vm = this;

        vm.engagementMember = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();
        vm.engagements = Engagement.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.engagementMember.id !== null) {
                EngagementMember.update(vm.engagementMember, onSaveSuccess, onSaveError);
            } else {
                EngagementMember.save(vm.engagementMember, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:engagementMemberUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
