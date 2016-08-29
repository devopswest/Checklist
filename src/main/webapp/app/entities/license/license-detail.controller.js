(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('LicenseDetailController', LicenseDetailController);

    LicenseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'License', 'Company', 'Taxonomy'];

    function LicenseDetailController($scope, $rootScope, $stateParams, previousState, entity, License, Company, Taxonomy) {
        var vm = this;

        vm.license = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:licenseUpdate', function(event, result) {
            vm.license = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
