(function() {
    'use strict';

    angular
        .module('smartinvestplusApp')
        .factory('IndiceSearch', IndiceSearch);

    IndiceSearch.$inject = ['$resource'];

    function IndiceSearch($resource) {
        var resourceUrl =  'api/_search/indices/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
