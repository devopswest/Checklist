(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$scope','$state', 'Auth', 'Principal', 'ProfileService', 'LoginService'];

    function NavbarController ($scope, $state, Auth, Principal, ProfileService, LoginService) {
        var vm = this;

        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

        ProfileService.getProfileInfo().then(function(response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });

        vm.login = login;
        vm.logout = logout;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.$state = $state;

        vm.menu = [
           {
                "id":"library",
                "label":"Library",
                "uiclass":"glyphicon glyphicon-th-list",
                "options" : [
                    {
                        "id":"checklist",
                        "label":"Checklist",
                        "uiclass" : "glyphicon glyphicon-asterisk",
                        "authorities" : "ROLE_ADMIN"
                    }
                ],
                "authorities" : "ROLE_ADMIN"
           },
           {
                "id":"audit",
                "label":"Audit",
                "uiclass":"glyphicon glyphicon-th-list",
                "options" : [
                    {
                        "id":"client",
                        "label": "Client",
                        "uiclass" : "glyphicon glyphicon-asterisk",
                        "authorities" : "ROLE_ADMIN"
                    },
                                        {
                        "id":"engagement",
                        "label": "Engagements",
                        "uiclass" : "glyphicon glyphicon-asterisk",
                        "authorities" : "ROLE_USER"
                    },
                    {
                        "id":"audit-profile",
                        "label": "Audit Profile",
                        "uiclass" : "glyphicon glyphicon-asterisk",
                        "authorities" : "ROLE_USER"
                    }
                ],
                "authorities" : "ROLE_ADMIN,ROLE_USER"
           },
           {
                "id":"management",
                "label":"Management",
                "uiclass":"glyphicon glyphicon-th-list",
                "options" : [
                    {
                        "id":"feature",
                        "label": "Feature",
                        "uiclass" : "glyphicon glyphicon-asterisk",
                        "authorities" : "ROLE_ADMIN"
                    },
                    {
                        "id":"user-profile",
                        "label": "User Profile",
                        "uiclass" : "glyphicon glyphicon-asterisk",
                        "authorities" : "ROLE_ADMIN"
                    },
                    {
                        "id":"license",
                        "label": "License",
                        "uiclass" : "glyphicon glyphicon-asterisk",
                        "authorities" : "ROLE_ADMIN"
                    },
                    {
                        "id":"workflow",
                        "label": "Workflow",
                        "uiclass" : "glyphicon glyphicon-asterisk",
                        "authorities" : "ROLE_ADMIN"
                    },
                    {
                        "id":"template",
                        "label": "template",
                        "uiclass" : "glyphicon glyphicon-asterisk",
                        "authorities" : "ROLE_ADMIN"
                    },
                    {
                        "id":"taxonomy",
                        "label": "Taxonomy",
                        "uiclass" : "glyphicon glyphicon-asterisk",
                        "authorities" : "ROLE_ADMIN"
                    }
                ],
                "authorities" : "ROLE_ADMIN"
           }
        ];

        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }

        function login() {
            collapseNavbar();
            LoginService.open();
        }

        function logout() {
            collapseNavbar();
            Auth.logout();
            $state.go('home');
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }
    }
})();
