(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('WorkflowDetailController', WorkflowDetailController);

    WorkflowDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Workflow', 'AuditProfile', 'WorkflowStep'];

    function WorkflowDetailController($scope, $rootScope, $stateParams, previousState, entity, Workflow, AuditProfile, WorkflowStep) {
        var vm = this;

        vm.workflow = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:workflowUpdate', function(event, result) {
            vm.workflow = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
