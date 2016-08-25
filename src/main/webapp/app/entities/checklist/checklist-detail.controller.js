(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ChecklistDetailController', ChecklistDetailController);

    ChecklistDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Checklist', 'ChecklistQuestion', 'AuditProfile', 'Country'];

    function ChecklistDetailController($scope, $rootScope, $stateParams, previousState, entity, Checklist, ChecklistQuestion, AuditProfile, Country) {
        var vm = this;

        vm.checklist = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:checklistUpdate', function(event, result) {
            vm.checklist = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
