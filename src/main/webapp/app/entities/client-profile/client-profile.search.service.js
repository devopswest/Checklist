(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('ClientProfileSearch', ClientProfileSearch);

    ClientProfileSearch.$inject = ['$resource'];

    function ClientProfileSearch($resource) {
        var resourceUrl =  'api/_search/client-profiles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
