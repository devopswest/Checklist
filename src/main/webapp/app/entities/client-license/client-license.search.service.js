(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('ClientLicenseSearch', ClientLicenseSearch);

    ClientLicenseSearch.$inject = ['$resource'];

    function ClientLicenseSearch($resource) {
        var resourceUrl =  'api/_search/client-licenses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
