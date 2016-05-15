'use strict';

describe('Controller Tests', function() {

    describe('Fractionnement Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockFractionnement, MockTitre;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockFractionnement = jasmine.createSpy('MockFractionnement');
            MockTitre = jasmine.createSpy('MockTitre');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Fractionnement': MockFractionnement,
                'Titre': MockTitre
            };
            createController = function() {
                $injector.get('$controller')("FractionnementDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smartinvestplusApp:fractionnementUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
