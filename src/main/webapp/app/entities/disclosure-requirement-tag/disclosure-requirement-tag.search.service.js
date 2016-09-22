(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('DisclosureRequirementTagSearch', DisclosureRequirementTagSearch);

    DisclosureRequirementTagSearch.$inject = ['$resource'];

    function DisclosureRequirementTagSearch($resource) {
        var resourceUrl =  'api/_search/disclosure-requirement-tags/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
