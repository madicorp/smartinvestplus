(function() {
    'use strict';

    angular
        .module('smartinvestplusApp')
        .controller('BourseDialogController', BourseDialogController);

    BourseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Bourse', 'User', 'Titre', 'Indice'];

    function BourseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Bourse, User, Titre, Indice) {
        var vm = this;
        vm.bourse = entity;
        vm.users = User.query();
        vm.titres = Titre.query();
        vm.indices = Indice.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('smartinvestplusApp:bourseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.bourse.id !== null) {
                Bourse.update(vm.bourse, onSaveSuccess, onSaveError);
            } else {
                Bourse.save(vm.bourse, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
