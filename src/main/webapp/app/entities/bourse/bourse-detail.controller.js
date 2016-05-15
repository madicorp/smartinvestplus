(function() {
    'use strict';

    angular
        .module('smartinvestplusApp')
        .controller('BourseDetailController', BourseDetailController);

    BourseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Bourse', 'User', 'Titre', 'Indice'];

    function BourseDetailController($scope, $rootScope, $stateParams, entity, Bourse, User, Titre, Indice) {
        var vm = this;
        vm.bourse = entity;
        
        var unsubscribe = $rootScope.$on('smartinvestplusApp:bourseUpdate', function(event, result) {
            vm.bourse = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
