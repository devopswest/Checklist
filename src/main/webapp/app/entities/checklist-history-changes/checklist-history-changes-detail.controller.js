(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ChecklistHistoryChangesDetailController', ChecklistHistoryChangesDetailController);

    ChecklistHistoryChangesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ChecklistHistoryChanges', 'Checklist', 'User'];

    function ChecklistHistoryChangesDetailController($scope, $rootScope, $stateParams, previousState, entity, ChecklistHistoryChanges, Checklist, User) {
        var vm = this;

        vm.checklistHistoryChanges = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:checklistHistoryChangesUpdate', function(event, result) {
            vm.checklistHistoryChanges = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
