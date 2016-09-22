(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('ClientTagSearch', ClientTagSearch);

    ClientTagSearch.$inject = ['$resource'];

    function ClientTagSearch($resource) {
        var resourceUrl =  'api/_search/client-tags/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
