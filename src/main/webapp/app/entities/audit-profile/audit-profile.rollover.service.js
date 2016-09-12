(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('AuditProfileRollover', AuditProfileRollover);

    AuditProfileRollover.$inject = ['$resource'];

    function AuditProfileRollover ($resource) {
        var resourceUrl =  'api/audit-profiles/rollover/:id';

        return $resource(resourceUrl, {id:'@id'}, {
            'rollover': { method:'PUT' }
        });
    }
})();
