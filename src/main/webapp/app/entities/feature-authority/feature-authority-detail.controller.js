(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('FeatureAuthorityDetailController', FeatureAuthorityDetailController);

    FeatureAuthorityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FeatureAuthority', 'Feature'];

    function FeatureAuthorityDetailController($scope, $rootScope, $stateParams, previousState, entity, FeatureAuthority, Feature) {
        var vm = this;

        vm.featureAuthority = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:featureAuthorityUpdate', function(event, result) {
            vm.featureAuthority = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
