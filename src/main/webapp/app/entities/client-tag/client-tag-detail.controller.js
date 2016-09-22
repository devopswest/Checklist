(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ClientTagDetailController', ClientTagDetailController);

    ClientTagDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ClientTag', 'Taxonomy', 'Client'];

    function ClientTagDetailController($scope, $rootScope, $stateParams, previousState, entity, ClientTag, Taxonomy, Client) {
        var vm = this;

        vm.clientTag = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:clientTagUpdate', function(event, result) {
            vm.clientTag = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
