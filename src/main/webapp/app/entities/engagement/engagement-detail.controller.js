(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('EngagementDetailController', EngagementDetailController);

    EngagementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Engagement', 'EngagementMember', 'Client', 'Checklist'];

    function EngagementDetailController($scope, $rootScope, $stateParams, previousState, entity, Engagement, EngagementMember, Client, Checklist) {
        var vm = this;

        vm.engagement = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:engagementUpdate', function(event, result) {
            vm.engagement = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
