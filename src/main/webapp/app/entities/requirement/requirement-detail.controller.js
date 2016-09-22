(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('RequirementDetailController', RequirementDetailController);

    RequirementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Requirement', 'DisclosureRequirement'];

    function RequirementDetailController($scope, $rootScope, $stateParams, previousState, entity, Requirement, DisclosureRequirement) {
        var vm = this;

        vm.requirement = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:requirementUpdate', function(event, result) {
            vm.requirement = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
