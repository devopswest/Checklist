(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ChecklistTemplateDetailController', ChecklistTemplateDetailController);

    ChecklistTemplateDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ChecklistTemplate', 'DisclosureRequirement', 'Taxonomy'];

    function ChecklistTemplateDetailController($scope, $rootScope, $stateParams, previousState, entity, ChecklistTemplate, DisclosureRequirement, Taxonomy) {
        var vm = this;

        vm.checklistTemplate = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:checklistTemplateUpdate', function(event, result) {
            vm.checklistTemplate = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
