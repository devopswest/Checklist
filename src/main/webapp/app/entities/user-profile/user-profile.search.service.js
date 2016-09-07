(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('UserProfileSearch', UserProfileSearch);

    UserProfileSearch.$inject = ['$resource'];

    function UserProfileSearch($resource) {
        var resourceUrl =  'api/_search/user-profiles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
