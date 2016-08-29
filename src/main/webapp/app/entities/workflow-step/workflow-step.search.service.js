(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('WorkflowStepSearch', WorkflowStepSearch);

    WorkflowStepSearch.$inject = ['$resource'];

    function WorkflowStepSearch($resource) {
        var resourceUrl =  'api/_search/workflow-steps/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
