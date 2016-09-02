(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('EngagementMemberDetailController', EngagementMemberDetailController);

    EngagementMemberDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'EngagementMember', 'User', 'Engagement'];

    function EngagementMemberDetailController($scope, $rootScope, $stateParams, previousState, entity, EngagementMember, User, Engagement) {
        var vm = this;

        vm.engagementMember = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:engagementMemberUpdate', function(event, result) {
            vm.engagementMember = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
