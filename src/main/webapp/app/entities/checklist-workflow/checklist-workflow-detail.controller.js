(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ChecklistWorkflowDetailController', ChecklistWorkflowDetailController);

    ChecklistWorkflowDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ChecklistWorkflow', 'Checklist', 'User', 'Workflow', 'ChecklistAnswer'];

    function ChecklistWorkflowDetailController($scope, $rootScope, $stateParams, previousState, entity, ChecklistWorkflow, Checklist, User, Workflow, ChecklistAnswer) {
        var vm = this;

        vm.checklistWorkflow = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:checklistWorkflowUpdate', function(event, result) {
            vm.checklistWorkflow = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
