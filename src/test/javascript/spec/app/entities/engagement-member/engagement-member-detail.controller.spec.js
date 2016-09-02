'use strict';

describe('Controller Tests', function() {

    describe('EngagementMember Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockEngagementMember, MockUser, MockEngagement;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockEngagementMember = jasmine.createSpy('MockEngagementMember');
            MockUser = jasmine.createSpy('MockUser');
            MockEngagement = jasmine.createSpy('MockEngagement');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'EngagementMember': MockEngagementMember,
                'User': MockUser,
                'Engagement': MockEngagement
            };
            createController = function() {
                $injector.get('$controller')("EngagementMemberDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'checklistApp:engagementMemberUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
