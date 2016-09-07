(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('FeatureSearch', FeatureSearch);

    FeatureSearch.$inject = ['$resource'];

    function FeatureSearch($resource) {
        var resourceUrl =  'api/_search/features/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
