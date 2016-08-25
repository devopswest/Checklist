(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('LicenseTypeDetailController', LicenseTypeDetailController);

    LicenseTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'LicenseType'];

    function LicenseTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, LicenseType) {
        var vm = this;

        vm.licenseType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:licenseTypeUpdate', function(event, result) {
            vm.licenseType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
