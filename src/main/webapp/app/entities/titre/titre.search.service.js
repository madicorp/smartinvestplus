(function() {
    'use strict';

    angular
        .module('smartinvestplusApp')
        .factory('TitreSearch', TitreSearch);

    TitreSearch.$inject = ['$resource'];

    function TitreSearch($resource) {
        var resourceUrl =  'api/_search/titres/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
