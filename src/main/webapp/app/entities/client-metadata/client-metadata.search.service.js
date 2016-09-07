(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('ClientMetadataSearch', ClientMetadataSearch);

    ClientMetadataSearch.$inject = ['$resource'];

    function ClientMetadataSearch($resource) {
        var resourceUrl =  'api/_search/client-metadata/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
