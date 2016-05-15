(function() {
    'use strict';

    angular
        .module('smartinvestplusApp')
        .controller('FractionnementDetailController', FractionnementDetailController);

    FractionnementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Fractionnement', 'Titre'];

    function FractionnementDetailController($scope, $rootScope, $stateParams, entity, Fractionnement, Titre) {
        var vm = this;
        vm.fractionnement = entity;
        
        var unsubscribe = $rootScope.$on('smartinvestplusApp:fractionnementUpdate', function(event, result) {
            vm.fractionnement = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
