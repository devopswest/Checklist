'use strict';

describe('Controller Tests', function() {

    describe('ClientLicense Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockClientLicense, MockClient, MockTaxonomy;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockClientLicense = jasmine.createSpy('MockClientLicense');
            MockClient = jasmine.createSpy('MockClient');
            MockTaxonomy = jasmine.createSpy('MockTaxonomy');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ClientLicense': MockClientLicense,
                'Client': MockClient,
                'Taxonomy': MockTaxonomy
            };
            createController = function() {
                $injector.get('$controller')("ClientLicenseDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'checklistApp:clientLicenseUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
