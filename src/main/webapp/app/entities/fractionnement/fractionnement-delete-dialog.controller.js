(function() {
    'use strict';

    angular
        .module('smartinvestplusApp')
        .controller('FractionnementDeleteController',FractionnementDeleteController);

    FractionnementDeleteController.$inject = ['$uibModalInstance', 'entity', 'Fractionnement'];

    function FractionnementDeleteController($uibModalInstance, entity, Fractionnement) {
        var vm = this;
        vm.fractionnement = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Fractionnement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
