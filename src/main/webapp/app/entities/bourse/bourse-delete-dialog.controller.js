(function() {
    'use strict';

    angular
        .module('smartinvestplusApp')
        .controller('BourseDeleteController',BourseDeleteController);

    BourseDeleteController.$inject = ['$uibModalInstance', 'entity', 'Bourse'];

    function BourseDeleteController($uibModalInstance, entity, Bourse) {
        var vm = this;
        vm.bourse = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Bourse.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
