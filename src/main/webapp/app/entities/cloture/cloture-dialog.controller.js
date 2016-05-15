(function() {
    'use strict';

    angular
        .module('smartinvestplusApp')
        .controller('ClotureDialogController', ClotureDialogController);

    ClotureDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Cloture', 'Titre'];

    function ClotureDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Cloture, Titre) {
        var vm = this;
        vm.cloture = entity;
        vm.titres = Titre.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('smartinvestplusApp:clotureUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.cloture.id !== null) {
                Cloture.update(vm.cloture, onSaveSuccess, onSaveError);
            } else {
                Cloture.save(vm.cloture, onSaveSuccess, onSaveError);
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
