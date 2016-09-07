(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('UserProfileDetailController', UserProfileDetailController);

    UserProfileDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UserProfile', 'User', 'Client'];

    function UserProfileDetailController($scope, $rootScope, $stateParams, previousState, entity, UserProfile, User, Client) {
        var vm = this;

        vm.userProfile = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:userProfileUpdate', function(event, result) {
            vm.userProfile = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
