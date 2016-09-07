(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('FeatureDetailController', FeatureDetailController);

    FeatureDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Feature', 'FeatureAuthority'];

    function FeatureDetailController($scope, $rootScope, $stateParams, previousState, entity, Feature, FeatureAuthority) {
        var vm = this;

        vm.feature = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:featureUpdate', function(event, result) {
            vm.feature = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
