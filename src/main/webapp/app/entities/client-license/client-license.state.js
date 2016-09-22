(function() {
    'use strict';

    angular
        .module('checklistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('client-license', {
            parent: 'entity',
            url: '/client-license?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.clientLicense.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/client-license/client-licenses.html',
                    controller: 'ClientLicenseController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('clientLicense');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('client-license-detail', {
            parent: 'entity',
            url: '/client-license/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.clientLicense.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/client-license/client-license-detail.html',
                    controller: 'ClientLicenseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('clientLicense');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ClientLicense', function($stateParams, ClientLicense) {
                    return ClientLicense.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'client-license',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('client-license-detail.edit', {
            parent: 'client-license-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-license/client-license-dialog.html',
                    controller: 'ClientLicenseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ClientLicense', function(ClientLicense) {
                            return ClientLicense.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('client-license.new', {
            parent: 'client-license',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-license/client-license-dialog.html',
                    controller: 'ClientLicenseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                contactName: null,
                                contactEmail: null,
                                expirationDate: null,
                                activationToken: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('client-license', null, { reload: 'client-license' });
                }, function() {
                    $state.go('client-license');
                });
            }]
        })
        .state('client-license.edit', {
            parent: 'client-license',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-license/client-license-dialog.html',
                    controller: 'ClientLicenseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ClientLicense', function(ClientLicense) {
                            return ClientLicense.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('client-license', null, { reload: 'client-license' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('client-license.delete', {
            parent: 'client-license',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-license/client-license-delete-dialog.html',
                    controller: 'ClientLicenseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ClientLicense', function(ClientLicense) {
                            return ClientLicense.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('client-license', null, { reload: 'client-license' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
