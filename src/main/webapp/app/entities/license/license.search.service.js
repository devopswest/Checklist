(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('LicenseSearch', LicenseSearch);

    LicenseSearch.$inject = ['$resource'];

    function LicenseSearch($resource) {
        var resourceUrl =  'api/_search/licenses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
