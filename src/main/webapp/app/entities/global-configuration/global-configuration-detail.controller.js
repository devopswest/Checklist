(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('GlobalConfigurationDetailController', GlobalConfigurationDetailController);

    GlobalConfigurationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'GlobalConfiguration'];

    function GlobalConfigurationDetailController($scope, $rootScope, $stateParams, previousState, entity, GlobalConfiguration) {
        var vm = this;

        vm.globalConfiguration = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:globalConfigurationUpdate', function(event, result) {
            vm.globalConfiguration = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
