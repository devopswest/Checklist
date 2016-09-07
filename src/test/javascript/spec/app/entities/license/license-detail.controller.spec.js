'use strict';

describe('Controller Tests', function() {

    describe('License Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockLicense, MockClient, MockTaxonomy;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockLicense = jasmine.createSpy('MockLicense');
            MockClient = jasmine.createSpy('MockClient');
            MockTaxonomy = jasmine.createSpy('MockTaxonomy');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'License': MockLicense,
                'Client': MockClient,
                'Taxonomy': MockTaxonomy
            };
            createController = function() {
                $injector.get('$controller')("LicenseDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'checklistApp:licenseUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
