(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('NotificationActionDetailController', NotificationActionDetailController);

    NotificationActionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'NotificationAction', 'Notification'];

    function NotificationActionDetailController($scope, $rootScope, $stateParams, previousState, entity, NotificationAction, Notification) {
        var vm = this;

        vm.notificationAction = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:notificationActionUpdate', function(event, result) {
            vm.notificationAction = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
