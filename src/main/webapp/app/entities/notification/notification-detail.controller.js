(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('NotificationDetailController', NotificationDetailController);

    NotificationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Notification', 'User', 'NotificationAction'];

    function NotificationDetailController($scope, $rootScope, $stateParams, previousState, entity, Notification, User, NotificationAction) {
        var vm = this;

        vm.notification = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:notificationUpdate', function(event, result) {
            vm.notification = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
