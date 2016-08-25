(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('LicensesDetailController', LicensesDetailController);

    LicensesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Licenses', 'Company'];

    function LicensesDetailController($scope, $rootScope, $stateParams, previousState, entity, Licenses, Company) {
        var vm = this;

        vm.licenses = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:licensesUpdate', function(event, result) {
            vm.licenses = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
