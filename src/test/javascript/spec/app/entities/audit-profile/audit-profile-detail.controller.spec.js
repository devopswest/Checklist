'use strict';

describe('Controller Tests', function() {

    describe('AuditProfile Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAuditProfile, MockChecklistQuestion, MockAuditQuestionResponse, MockClient, MockChecklist;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAuditProfile = jasmine.createSpy('MockAuditProfile');
            MockChecklistQuestion = jasmine.createSpy('MockChecklistQuestion');
            MockAuditQuestionResponse = jasmine.createSpy('MockAuditQuestionResponse');
            MockClient = jasmine.createSpy('MockClient');
            MockChecklist = jasmine.createSpy('MockChecklist');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'AuditProfile': MockAuditProfile,
                'ChecklistQuestion': MockChecklistQuestion,
                'AuditQuestionResponse': MockAuditQuestionResponse,
                'Client': MockClient,
                'Checklist': MockChecklist
            };
            createController = function() {
                $injector.get('$controller')("AuditProfileDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'checklistApp:auditProfileUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
