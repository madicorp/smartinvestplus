(function() {
    'use strict';

    angular
        .module('smartinvestplusApp')
        .controller('IndiceDetailController', IndiceDetailController);

    IndiceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Indice', 'Bourse'];

    function IndiceDetailController($scope, $rootScope, $stateParams, entity, Indice, Bourse) {
        var vm = this;
        vm.indice = entity;
        
        var unsubscribe = $rootScope.$on('smartinvestplusApp:indiceUpdate', function(event, result) {
            vm.indice = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
