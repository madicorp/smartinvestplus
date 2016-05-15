(function() {
    'use strict';

    angular
        .module('smartinvestplusApp')
        .factory('ClotureSearch', ClotureSearch);

    ClotureSearch.$inject = ['$resource'];

    function ClotureSearch($resource) {
        var resourceUrl =  'api/_search/clotures/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
