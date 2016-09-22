'use strict';

describe('Controller Tests', function() {

    describe('EngagementChecklistTemplate Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockEngagementChecklistTemplate, MockChecklistTemplate, MockWorkflow, MockEngagement;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockEngagementChecklistTemplate = jasmine.createSpy('MockEngagementChecklistTemplate');
            MockChecklistTemplate = jasmine.createSpy('MockChecklistTemplate');
            MockWorkflow = jasmine.createSpy('MockWorkflow');
            MockEngagement = jasmine.createSpy('MockEngagement');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'EngagementChecklistTemplate': MockEngagementChecklistTemplate,
                'ChecklistTemplate': MockChecklistTemplate,
                'Workflow': MockWorkflow,
                'Engagement': MockEngagement
            };
            createController = function() {
                $injector.get('$controller')("EngagementChecklistTemplateDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'checklistApp:engagementChecklistTemplateUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
