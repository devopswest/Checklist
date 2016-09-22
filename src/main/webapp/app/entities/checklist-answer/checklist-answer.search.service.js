(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('ChecklistAnswerSearch', ChecklistAnswerSearch);

    ChecklistAnswerSearch.$inject = ['$resource'];

    function ChecklistAnswerSearch($resource) {
        var resourceUrl =  'api/_search/checklist-answers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
