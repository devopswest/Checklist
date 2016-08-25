(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('ChecklistQuestionSearch', ChecklistQuestionSearch);

    ChecklistQuestionSearch.$inject = ['$resource'];

    function ChecklistQuestionSearch($resource) {
        var resourceUrl =  'api/_search/checklist-questions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
