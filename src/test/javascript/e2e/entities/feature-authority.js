'use strict';

describe('FeatureAuthority e2e test', function () {

    var username = element(by.id('username'));
    var password = element(by.id('password'));
    var entityMenu = element(by.id('entity-menu'));
    var accountMenu = element(by.id('account-menu'));
    var login = element(by.id('login'));
    var logout = element(by.id('logout'));

    beforeAll(function () {
        browser.get('/');

        accountMenu.click();
        login.click();

        username.sendKeys('admin');
        password.sendKeys('admin');
        element(by.css('button[type=submit]')).click();
    });

    it('should load FeatureAuthorities', function () {
        entityMenu.click();
        element(by.css('[ui-sref="feature-authority"]')).click().then(function() {
            expect(element.all(by.css('h2')).first().getText()).toMatch(/Feature Authorities/);
        });
    });

    it('should load create FeatureAuthority dialog', function () {
        element(by.css('[ui-sref="feature-authority.new"]')).click().then(function() {
            expect(element(by.css('h4.modal-title')).getAttribute("translate")).toMatch(/checklistApp.featureAuthority.home.createOrEditLabel/);
            element(by.css('button.close')).click();
        });
    });

    afterAll(function () {
        accountMenu.click();
        logout.click();
    });
});
