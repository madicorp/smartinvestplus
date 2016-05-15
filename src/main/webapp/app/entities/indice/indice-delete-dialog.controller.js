(function() {
    'use strict';

    angular
        .module('smartinvestplusApp')
        .controller('IndiceDeleteController',IndiceDeleteController);

    IndiceDeleteController.$inject = ['$uibModalInstance', 'entity', 'Indice'];

    function IndiceDeleteController($uibModalInstance, entity, Indice) {
        var vm = this;
        vm.indice = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Indice.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
