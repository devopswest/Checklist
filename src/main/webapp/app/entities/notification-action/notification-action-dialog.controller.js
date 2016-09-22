(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('NotificationActionDialogController', NotificationActionDialogController);

    NotificationActionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'NotificationAction', 'Notification'];

    function NotificationActionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, NotificationAction, Notification) {
        var vm = this;

        vm.notificationAction = entity;
        vm.clear = clear;
        vm.save = save;
        vm.notifications = Notification.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.notificationAction.id !== null) {
                NotificationAction.update(vm.notificationAction, onSaveSuccess, onSaveError);
            } else {
                NotificationAction.save(vm.notificationAction, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:notificationActionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
