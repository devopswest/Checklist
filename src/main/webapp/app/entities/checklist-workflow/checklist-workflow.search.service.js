(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('ChecklistWorkflowSearch', ChecklistWorkflowSearch);

    ChecklistWorkflowSearch.$inject = ['$resource'];

    function ChecklistWorkflowSearch($resource) {
        var resourceUrl =  'api/_search/checklist-workflows/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
