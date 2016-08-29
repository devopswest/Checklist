'use strict';

describe('Controller Tests', function() {

    describe('Workflow Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockWorkflow, MockAuditProfile, MockWorkflowStep;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockWorkflow = jasmine.createSpy('MockWorkflow');
            MockAuditProfile = jasmine.createSpy('MockAuditProfile');
            MockWorkflowStep = jasmine.createSpy('MockWorkflowStep');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Workflow': MockWorkflow,
                'AuditProfile': MockAuditProfile,
                'WorkflowStep': MockWorkflowStep
            };
            createController = function() {
                $injector.get('$controller')("WorkflowDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'checklistApp:workflowUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
