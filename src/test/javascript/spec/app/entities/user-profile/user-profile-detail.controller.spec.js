'use strict';

describe('Controller Tests', function() {

    describe('UserProfile Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockUserProfile, MockUser, MockClient;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockUserProfile = jasmine.createSpy('MockUserProfile');
            MockUser = jasmine.createSpy('MockUser');
            MockClient = jasmine.createSpy('MockClient');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'UserProfile': MockUserProfile,
                'User': MockUser,
                'Client': MockClient
            };
            createController = function() {
                $injector.get('$controller')("UserProfileDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'checklistApp:userProfileUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
