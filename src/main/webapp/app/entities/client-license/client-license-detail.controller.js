(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ClientLicenseDetailController', ClientLicenseDetailController);

    ClientLicenseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ClientLicense', 'Client', 'Taxonomy'];

    function ClientLicenseDetailController($scope, $rootScope, $stateParams, previousState, entity, ClientLicense, Client, Taxonomy) {
        var vm = this;

        vm.clientLicense = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:clientLicenseUpdate', function(event, result) {
            vm.clientLicense = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
