(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('DisclosureRequirementDetailController', DisclosureRequirementDetailController);

    DisclosureRequirementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DisclosureRequirement', 'DisclosureRequirementTag', 'ChecklistTemplate', 'Requirement'];

    function DisclosureRequirementDetailController($scope, $rootScope, $stateParams, previousState, entity, DisclosureRequirement, DisclosureRequirementTag, ChecklistTemplate, Requirement) {
        var vm = this;

        vm.disclosureRequirement = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:disclosureRequirementUpdate', function(event, result) {
            vm.disclosureRequirement = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
