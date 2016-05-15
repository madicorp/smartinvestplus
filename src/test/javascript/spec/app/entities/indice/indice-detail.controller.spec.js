'use strict';

describe('Controller Tests', function() {

    describe('Indice Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockIndice, MockBourse;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockIndice = jasmine.createSpy('MockIndice');
            MockBourse = jasmine.createSpy('MockBourse');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Indice': MockIndice,
                'Bourse': MockBourse
            };
            createController = function() {
                $injector.get('$controller')("IndiceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smartinvestplusApp:indiceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
