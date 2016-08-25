(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('AuditQuestionResponse', AuditQuestionResponse);

    AuditQuestionResponse.$inject = ['$resource'];

    function AuditQuestionResponse ($resource) {
        var resourceUrl =  'api/audit-question-responses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
