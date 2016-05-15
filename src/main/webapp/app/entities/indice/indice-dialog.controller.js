(function() {
    'use strict';

    angular
        .module('smartinvestplusApp')
        .controller('IndiceDialogController', IndiceDialogController);

    IndiceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Indice', 'Bourse'];

    function IndiceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Indice, Bourse) {
        var vm = this;
        vm.indice = entity;
        vm.bourses = Bourse.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('smartinvestplusApp:indiceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.indice.id !== null) {
                Indice.update(vm.indice, onSaveSuccess, onSaveError);
            } else {
                Indice.save(vm.indice, onSaveSuccess, onSaveError);
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
