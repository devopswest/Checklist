(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('AuditProfileLogEntrySearch', AuditProfileLogEntrySearch);

    AuditProfileLogEntrySearch.$inject = ['$resource'];

    function AuditProfileLogEntrySearch($resource) {
        var resourceUrl =  'api/_search/audit-profile-log-entries/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
