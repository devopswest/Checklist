'use strict';

describe('Controller Tests', function() {

    describe('AuditQuestionResponse Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAuditQuestionResponse, MockChecklistQuestion, MockAuditProfile;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAuditQuestionResponse = jasmine.createSpy('MockAuditQuestionResponse');
            MockChecklistQuestion = jasmine.createSpy('MockChecklistQuestion');
            MockAuditProfile = jasmine.createSpy('MockAuditProfile');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'AuditQuestionResponse': MockAuditQuestionResponse,
                'ChecklistQuestion': MockChecklistQuestion,
                'AuditProfile': MockAuditProfile
            };
            createController = function() {
                $injector.get('$controller')("AuditQuestionResponseDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'checklistApp:auditQuestionResponseUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
