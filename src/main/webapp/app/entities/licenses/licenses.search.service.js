(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('LicensesSearch', LicensesSearch);

    LicensesSearch.$inject = ['$resource'];

    function LicensesSearch($resource) {
        var resourceUrl =  'api/_search/licenses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
