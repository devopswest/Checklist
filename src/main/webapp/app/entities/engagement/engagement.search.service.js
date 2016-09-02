(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('EngagementSearch', EngagementSearch);

    EngagementSearch.$inject = ['$resource'];

    function EngagementSearch($resource) {
        var resourceUrl =  'api/_search/engagements/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
