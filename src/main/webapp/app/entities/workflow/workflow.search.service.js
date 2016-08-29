(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('WorkflowSearch', WorkflowSearch);

    WorkflowSearch.$inject = ['$resource'];

    function WorkflowSearch($resource) {
        var resourceUrl =  'api/_search/workflows/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
