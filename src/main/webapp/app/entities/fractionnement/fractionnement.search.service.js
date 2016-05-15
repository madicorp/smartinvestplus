(function() {
    'use strict';

    angular
        .module('smartinvestplusApp')
        .factory('FractionnementSearch', FractionnementSearch);

    FractionnementSearch.$inject = ['$resource'];

    function FractionnementSearch($resource) {
        var resourceUrl =  'api/_search/fractionnements/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
