(function() {
    'use strict';

    angular
        .module('smartinvestplusApp')
        .controller('FractionnementDialogController', FractionnementDialogController);

    FractionnementDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Fractionnement', 'Titre'];

    function FractionnementDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Fractionnement, Titre) {
        var vm = this;
        vm.fractionnement = entity;
        vm.titres = Titre.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('smartinvestplusApp:fractionnementUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.fractionnement.id !== null) {
                Fractionnement.update(vm.fractionnement, onSaveSuccess, onSaveError);
            } else {
                Fractionnement.save(vm.fractionnement, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.date = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
