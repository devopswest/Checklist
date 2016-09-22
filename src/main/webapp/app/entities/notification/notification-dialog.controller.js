(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('NotificationDialogController', NotificationDialogController);

    NotificationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Notification', 'User', 'NotificationAction'];

    function NotificationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Notification, User, NotificationAction) {
        var vm = this;

        vm.notification = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();
        vm.notificationactions = NotificationAction.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.notification.id !== null) {
                Notification.update(vm.notification, onSaveSuccess, onSaveError);
            } else {
                Notification.save(vm.notification, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:notificationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
