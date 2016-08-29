'use strict';

describe('Controller Tests', function() {

    describe('AuditProfileLogEntry Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAuditProfileLogEntry, MockAuditProfile, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAuditProfileLogEntry = jasmine.createSpy('MockAuditProfileLogEntry');
            MockAuditProfile = jasmine.createSpy('MockAuditProfile');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'AuditProfileLogEntry': MockAuditProfileLogEntry,
                'AuditProfile': MockAuditProfile,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("AuditProfileLogEntryDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'checklistApp:auditProfileLogEntryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
