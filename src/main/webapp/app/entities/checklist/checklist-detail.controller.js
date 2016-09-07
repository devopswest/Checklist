(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ChecklistDetailController', ChecklistDetailController);

    ChecklistDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Checklist', 'ChecklistQuestion', 'Taxonomy'];

    function ChecklistDetailController($scope, $rootScope, $stateParams, previousState, entity, Checklist, ChecklistQuestion, Taxonomy) {
        var vm = this;

        vm.checklist = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:checklistUpdate', function(event, result) {
            vm.checklist = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
