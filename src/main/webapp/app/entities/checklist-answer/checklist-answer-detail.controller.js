(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ChecklistAnswerDetailController', ChecklistAnswerDetailController);

    ChecklistAnswerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ChecklistAnswer', 'DisclosureRequirement', 'Checklist'];

    function ChecklistAnswerDetailController($scope, $rootScope, $stateParams, previousState, entity, ChecklistAnswer, DisclosureRequirement, Checklist) {
        var vm = this;

        vm.checklistAnswer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:checklistAnswerUpdate', function(event, result) {
            vm.checklistAnswer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
