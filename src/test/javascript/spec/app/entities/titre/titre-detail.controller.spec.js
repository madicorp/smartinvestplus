'use strict';

describe('Controller Tests', function() {

    describe('Titre Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTitre, MockBourse, MockCloture, MockFractionnement;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTitre = jasmine.createSpy('MockTitre');
            MockBourse = jasmine.createSpy('MockBourse');
            MockCloture = jasmine.createSpy('MockCloture');
            MockFractionnement = jasmine.createSpy('MockFractionnement');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Titre': MockTitre,
                'Bourse': MockBourse,
                'Cloture': MockCloture,
                'Fractionnement': MockFractionnement
            };
            createController = function() {
                $injector.get('$controller')("TitreDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smartinvestplusApp:titreUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
