(function() {
    'use strict';

    angular
        .module('smartinvestplusApp')
        .controller('ClotureDeleteController',ClotureDeleteController);

    ClotureDeleteController.$inject = ['$uibModalInstance', 'entity', 'Cloture'];

    function ClotureDeleteController($uibModalInstance, entity, Cloture) {
        var vm = this;
        vm.cloture = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Cloture.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
