(function() {
    'use strict';

    angular
        .module('smartinvestplusApp')
        .controller('TitreDeleteController',TitreDeleteController);

    TitreDeleteController.$inject = ['$uibModalInstance', 'entity', 'Titre'];

    function TitreDeleteController($uibModalInstance, entity, Titre) {
        var vm = this;
        vm.titre = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Titre.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
