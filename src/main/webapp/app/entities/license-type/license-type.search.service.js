(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('LicenseTypeSearch', LicenseTypeSearch);

    LicenseTypeSearch.$inject = ['$resource'];

    function LicenseTypeSearch($resource) {
        var resourceUrl =  'api/_search/license-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
