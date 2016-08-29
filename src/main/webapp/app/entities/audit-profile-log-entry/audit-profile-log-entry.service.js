(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('AuditProfileLogEntry', AuditProfileLogEntry);

    AuditProfileLogEntry.$inject = ['$resource', 'DateUtils'];

    function AuditProfileLogEntry ($resource, DateUtils) {
        var resourceUrl =  'api/audit-profile-log-entries/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.happened = DateUtils.convertDateTimeFromServer(data.happened);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
