(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('AuditProfileLogEntryArray', AuditProfileLogEntryArray);

    AuditProfileLogEntryArray.$inject = ['$resource', 'DateUtils'];

    function AuditProfileLogEntryArray ($resource, DateUtils) {
        var resourceUrl =  'api/audit-profile-log-entries/array';

        return $resource(resourceUrl, {}, {
            'save': { method:'POST' }
        });
    }
})();
