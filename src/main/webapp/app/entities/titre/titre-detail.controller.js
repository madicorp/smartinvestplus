(function() {
    'use strict';

    angular
        .module('smartinvestplusApp')
        .controller('TitreDetailController', TitreDetailController);

    TitreDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Titre', 'Bourse', 'Cloture', 'Fractionnement'];

    function TitreDetailController($scope, $rootScope, $stateParams, entity, Titre, Bourse, Cloture, Fractionnement) {
        var vm = this;
        vm.titre = entity;
        
        var unsubscribe = $rootScope.$on('smartinvestplusApp:titreUpdate', function(event, result) {
            vm.titre = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
