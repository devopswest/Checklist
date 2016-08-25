(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('AuditQuestionResponseSearch', AuditQuestionResponseSearch);

    AuditQuestionResponseSearch.$inject = ['$resource'];

    function AuditQuestionResponseSearch($resource) {
        var resourceUrl =  'api/_search/audit-question-responses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
