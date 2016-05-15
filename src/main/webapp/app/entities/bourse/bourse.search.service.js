(function() {
    'use strict';

    angular
        .module('smartinvestplusApp')
        .factory('BourseSearch', BourseSearch);

    BourseSearch.$inject = ['$resource'];

    function BourseSearch($resource) {
        var resourceUrl =  'api/_search/bourses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
