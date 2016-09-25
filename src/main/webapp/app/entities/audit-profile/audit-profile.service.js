(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('AuditProfile', AuditProfile);

    AuditProfile.$inject = ['$resource'];

    function AuditProfile ($resource) {
        var resourceUrl =  'api/audit-profiles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                    	console.log('Error in fromJson....');
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
