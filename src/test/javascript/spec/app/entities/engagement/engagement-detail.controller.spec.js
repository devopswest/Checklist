'use strict';

describe('Controller Tests', function() {

    describe('Engagement Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockEngagement, MockEngagementMember, MockEngagementChecklistTemplate, MockClient, MockWorkflow;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockEngagement = jasmine.createSpy('MockEngagement');
            MockEngagementMember = jasmine.createSpy('MockEngagementMember');
            MockEngagementChecklistTemplate = jasmine.createSpy('MockEngagementChecklistTemplate');
            MockClient = jasmine.createSpy('MockClient');
            MockWorkflow = jasmine.createSpy('MockWorkflow');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Engagement': MockEngagement,
                'EngagementMember': MockEngagementMember,
                'EngagementChecklistTemplate': MockEngagementChecklistTemplate,
                'Client': MockClient,
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
