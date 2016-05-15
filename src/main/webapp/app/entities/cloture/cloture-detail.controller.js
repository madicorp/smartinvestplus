(function() {
    'use strict';

    angular
        .module('smartinvestplusApp')
        .controller('ClotureDetailController', ClotureDetailController);

    ClotureDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Cloture', 'Titre'];

    function ClotureDetailController($scope, $rootScope, $stateParams, entity, Cloture, Titre) {
        var vm = this;
        vm.cloture = entity;
        
        var unsubscribe = $rootScope.$on('smartinvestplusApp:clotureUpdate', function(event, result) {
            vm.cloture = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
