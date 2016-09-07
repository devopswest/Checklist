(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ClientMetadataDetailController', ClientMetadataDetailController);

    ClientMetadataDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ClientMetadata', 'Taxonomy', 'ClientProfile'];

    function ClientMetadataDetailController($scope, $rootScope, $stateParams, previousState, entity, ClientMetadata, Taxonomy, ClientProfile) {
        var vm = this;

        vm.clientMetadata = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:clientMetadataUpdate', function(event, result) {
            vm.clientMetadata = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
