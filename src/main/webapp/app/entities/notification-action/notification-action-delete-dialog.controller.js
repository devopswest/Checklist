(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('NotificationActionDeleteController',NotificationActionDeleteController);

    NotificationActionDeleteController.$inject = ['$uibModalInstance', 'entity', 'NotificationAction'];

    function NotificationActionDeleteController($uibModalInstance, entity, NotificationAction) {
        var vm = this;

        vm.notificationAction = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            NotificationAction.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
