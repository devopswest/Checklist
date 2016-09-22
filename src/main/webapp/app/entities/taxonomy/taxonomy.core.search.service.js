(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('TaxonomyCoreSearch', TaxonomyCoreSearch);

    TaxonomyCoreSearch.$inject = ['$resource'];

    function TaxonomyCoreSearch ($resource) {
        var resourceUrl =  'api/taxonomies/parent/:code';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
