(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('AuditProfileDetailController', AuditProfileDetailController);

    AuditProfileDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AuditProfile', 'AuditProfileLogEntry', 'AuditQuestionResponse', 'Engagement', 'Checklist'];

    function AuditProfileDetailController($scope, $rootScope, $stateParams, previousState, entity, AuditProfile, AuditProfileLogEntry, AuditQuestionResponse, Engagement, Checklist) {
        var vm = this;

        vm.auditProfile = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:auditProfileUpdate', function(event, result) {
            vm.auditProfile = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
