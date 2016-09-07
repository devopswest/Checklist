(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ClientProfileDetailController', ClientProfileDetailController);

    ClientProfileDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ClientProfile', 'Client', 'ClientMetadata'];

    function ClientProfileDetailController($scope, $rootScope, $stateParams, previousState, entity, ClientProfile, Client, ClientMetadata) {
        var vm = this;

        vm.clientProfile = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:clientProfileUpdate', function(event, result) {
            vm.clientProfile = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
