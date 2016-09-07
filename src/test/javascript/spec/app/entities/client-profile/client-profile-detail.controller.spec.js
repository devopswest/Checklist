'use strict';

describe('Controller Tests', function() {

    describe('ClientProfile Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockClientProfile, MockClient, MockClientMetadata;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockClientProfile = jasmine.createSpy('MockClientProfile');
            MockClient = jasmine.createSpy('MockClient');
            MockClientMetadata = jasmine.createSpy('MockClientMetadata');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ClientProfile': MockClientProfile,
                'Client': MockClient,
                'ClientMetadata': MockClientMetadata
            };
            createController = function() {
                $injector.get('$controller')("ClientProfileDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'checklistApp:clientProfileUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
