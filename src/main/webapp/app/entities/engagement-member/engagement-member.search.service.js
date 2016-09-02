(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('EngagementMemberSearch', EngagementMemberSearch);

    EngagementMemberSearch.$inject = ['$resource'];

    function EngagementMemberSearch($resource) {
        var resourceUrl =  'api/_search/engagement-members/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
