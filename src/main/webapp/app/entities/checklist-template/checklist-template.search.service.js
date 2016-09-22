(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('ChecklistTemplateSearch', ChecklistTemplateSearch);

    ChecklistTemplateSearch.$inject = ['$resource'];

    function ChecklistTemplateSearch($resource) {
        var resourceUrl =  'api/_search/checklist-templates/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
