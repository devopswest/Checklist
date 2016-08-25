(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('AuditProfileDetailController', AuditProfileDetailController);

    AuditProfileDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AuditProfile', 'ChecklistQuestion', 'AuditQuestionResponse', 'Client', 'Checklist'];

    function AuditProfileDetailController($scope, $rootScope, $stateParams, previousState, entity, AuditProfile, ChecklistQuestion, AuditQuestionResponse, Client, Checklist) {
        var vm = this;

        vm.auditProfile = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:auditProfileUpdate', function(event, result) {
            vm.auditProfile = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
