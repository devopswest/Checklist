(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('GlobalConfigurationSearch', GlobalConfigurationSearch);

    GlobalConfigurationSearch.$inject = ['$resource'];

    function GlobalConfigurationSearch($resource) {
        var resourceUrl =  'api/_search/global-configurations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
