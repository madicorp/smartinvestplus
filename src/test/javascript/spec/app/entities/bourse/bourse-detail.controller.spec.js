'use strict';

describe('Controller Tests', function() {

    describe('Bourse Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockBourse, MockUser, MockTitre, MockIndice;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockBourse = jasmine.createSpy('MockBourse');
            MockUser = jasmine.createSpy('MockUser');
            MockTitre = jasmine.createSpy('MockTitre');
            MockIndice = jasmine.createSpy('MockIndice');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Bourse': MockBourse,
                'User': MockUser,
                'Titre': MockTitre,
                'Indice': MockIndice
            };
            createController = function() {
                $injector.get('$controller')("BourseDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'smartinvestplusApp:bourseUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
