(function() {
    'use strict';
    angular
        .module('smartinvestplusApp')
        .factory('Titre', Titre);

    Titre.$inject = ['$resource'];

    function Titre ($resource) {
        var resourceUrl =  'api/titres/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
