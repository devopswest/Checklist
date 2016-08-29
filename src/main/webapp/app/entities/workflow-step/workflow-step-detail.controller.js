(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('WorkflowStepDetailController', WorkflowStepDetailController);

    WorkflowStepDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'WorkflowStep', 'Workflow'];

    function WorkflowStepDetailController($scope, $rootScope, $stateParams, previousState, entity, WorkflowStep, Workflow) {
        var vm = this;

        vm.workflowStep = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:workflowStepUpdate', function(event, result) {
            vm.workflowStep = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
