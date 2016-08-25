(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('AuditProfileSearch', AuditProfileSearch);

    AuditProfileSearch.$inject = ['$resource'];

    function AuditProfileSearch($resource) {
        var resourceUrl =  'api/_search/audit-profiles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
