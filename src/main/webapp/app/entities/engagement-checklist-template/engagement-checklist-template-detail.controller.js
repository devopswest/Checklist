(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('EngagementChecklistTemplateDetailController', EngagementChecklistTemplateDetailController);

    EngagementChecklistTemplateDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'EngagementChecklistTemplate', 'ChecklistTemplate', 'Workflow', 'Engagement'];

    function EngagementChecklistTemplateDetailController($scope, $rootScope, $stateParams, previousState, entity, EngagementChecklistTemplate, ChecklistTemplate, Workflow, Engagement) {
        var vm = this;

        vm.engagementChecklistTemplate = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:engagementChecklistTemplateUpdate', function(event, result) {
            vm.engagementChecklistTemplate = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
