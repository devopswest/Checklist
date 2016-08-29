(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('AuditProfileLogEntryDetailController', AuditProfileLogEntryDetailController);

    AuditProfileLogEntryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AuditProfileLogEntry', 'AuditProfile', 'User'];

    function AuditProfileLogEntryDetailController($scope, $rootScope, $stateParams, previousState, entity, AuditProfileLogEntry, AuditProfile, User) {
        var vm = this;

        vm.auditProfileLogEntry = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:auditProfileLogEntryUpdate', function(event, result) {
            vm.auditProfileLogEntry = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
