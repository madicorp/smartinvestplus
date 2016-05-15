'use strict';

describe('Controller Tests', function() {

    describe('Cloture Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCloture, MockTitre;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCloture = jasmine.createSpy('MockCloture');
            MockTitre = jasmine.createSpy('MockTitre');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Cloture': MockCloture,
                'Titre': MockTitre
            };
            createController = function() {
                $injector.get('$controller')("ClotureDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smartinvestplusApp:clotureUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
