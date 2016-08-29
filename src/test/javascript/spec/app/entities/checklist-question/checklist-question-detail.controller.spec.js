'use strict';

describe('Controller Tests', function() {

    describe('ChecklistQuestion Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockChecklistQuestion, MockAuditQuestionResponse, MockChecklist, MockQuestion, MockAuditProfile;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockChecklistQuestion = jasmine.createSpy('MockChecklistQuestion');
            MockAuditQuestionResponse = jasmine.createSpy('MockAuditQuestionResponse');
            MockChecklist = jasmine.createSpy('MockChecklist');
            MockQuestion = jasmine.createSpy('MockQuestion');
            MockAuditProfile = jasmine.createSpy('MockAuditProfile');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ChecklistQuestion': MockChecklistQuestion,
                'AuditQuestionResponse': MockAuditQuestionResponse,
                'Checklist': MockChecklist,
                'Question': MockQuestion,
                'AuditProfile': MockAuditProfile
            };
            createController = function() {
                $injector.get('$controller')("ChecklistQuestionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'checklistApp:checklistQuestionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
