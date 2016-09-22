(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('DisclosureRequirementTagDetailController', DisclosureRequirementTagDetailController);

    DisclosureRequirementTagDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DisclosureRequirementTag', 'Taxonomy', 'DisclosureRequirement'];

    function DisclosureRequirementTagDetailController($scope, $rootScope, $stateParams, previousState, entity, DisclosureRequirementTag, Taxonomy, DisclosureRequirement) {
        var vm = this;

        vm.disclosureRequirementTag = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:disclosureRequirementTagUpdate', function(event, result) {
            vm.disclosureRequirementTag = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
