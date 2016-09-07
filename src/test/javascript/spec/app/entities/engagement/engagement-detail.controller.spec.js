'use strict';

describe('Controller Tests', function() {

    describe('Engagement Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockEngagement, MockEngagementMember, MockClient, MockChecklist, MockWorkflow;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockEngagement = jasmine.createSpy('MockEngagement');
            MockEngagementMember = jasmine.createSpy('MockEngagementMember');
            MockClient = jasmine.createSpy('MockClient');
            MockChecklist = jasmine.createSpy('MockChecklist');
            MockWorkflow = jasmine.createSpy('MockWorkflow');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Engagement': MockEngagement,
                'EngagementMember': MockEngagementMember,
                'Client': MockClient,
                'Checklist': MockChecklist,
                'Workflow': MockWorkflow
            };
            createController = function() {
                $injector.get('$controller')("EngagementDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'checklistApp:engagementUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
