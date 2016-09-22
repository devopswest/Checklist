(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('EngagementChecklistTemplateSearch', EngagementChecklistTemplateSearch);

    EngagementChecklistTemplateSearch.$inject = ['$resource'];

    function EngagementChecklistTemplateSearch($resource) {
        var resourceUrl =  'api/_search/engagement-checklist-templates/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
