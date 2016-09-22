(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('DisclosureRequirementSearch', DisclosureRequirementSearch);

    DisclosureRequirementSearch.$inject = ['$resource'];

    function DisclosureRequirementSearch($resource) {
        var resourceUrl =  'api/_search/disclosure-requirements/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
