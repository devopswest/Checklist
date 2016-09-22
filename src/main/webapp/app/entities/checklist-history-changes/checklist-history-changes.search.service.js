(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('ChecklistHistoryChangesSearch', ChecklistHistoryChangesSearch);

    ChecklistHistoryChangesSearch.$inject = ['$resource'];

    function ChecklistHistoryChangesSearch($resource) {
        var resourceUrl =  'api/_search/checklist-history-changes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
