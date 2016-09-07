(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('FeatureAuthoritySearch', FeatureAuthoritySearch);

    FeatureAuthoritySearch.$inject = ['$resource'];

    function FeatureAuthoritySearch($resource) {
        var resourceUrl =  'api/_search/feature-authorities/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
