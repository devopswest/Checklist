(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('AuditQuestionResponseDetailController', AuditQuestionResponseDetailController);

    AuditQuestionResponseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AuditQuestionResponse', 'ChecklistQuestion', 'AuditProfile'];

    function AuditQuestionResponseDetailController($scope, $rootScope, $stateParams, previousState, entity, AuditQuestionResponse, ChecklistQuestion, AuditProfile) {
        var vm = this;

        vm.auditQuestionResponse = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:auditQuestionResponseUpdate', function(event, result) {
            vm.auditQuestionResponse = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
