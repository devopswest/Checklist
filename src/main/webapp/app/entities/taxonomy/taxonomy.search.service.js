(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('TaxonomySearch', TaxonomySearch);

    TaxonomySearch.$inject = ['$resource'];

    function TaxonomySearch($resource) {
        var resourceUrl =  'api/_search/taxonomies/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
