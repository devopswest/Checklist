(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ChecklistQuestionDetailController', ChecklistQuestionDetailController);

    ChecklistQuestionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ChecklistQuestion', 'Checklist'];

    function ChecklistQuestionDetailController($scope, $rootScope, $stateParams, previousState, entity, ChecklistQuestion, Checklist) {
        var vm = this;

        vm.checklistQuestion = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:checklistQuestionUpdate', function(event, result) {
            vm.checklistQuestion = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
