(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('ChecklistSearch', ChecklistSearch);

    ChecklistSearch.$inject = ['$resource'];

    function ChecklistSearch($resource) {
        var resourceUrl =  'api/_search/checklists/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
