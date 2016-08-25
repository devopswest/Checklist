(function() {
    'use strict';

    angular
        .module('checklistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('licenses', {
            parent: 'entity',
            url: '/licenses?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.licenses.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/licenses/licenses.html',
                    controller: 'LicensesController',
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
                    $translatePartialLoader.addPart('licenses');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('licenses-detail', {
            parent: 'entity',
            url: '/licenses/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.licenses.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/licenses/licenses-detail.html',
                    controller: 'LicensesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('licenses');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Licenses', function($stateParams, Licenses) {
                    return Licenses.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'licenses',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('licenses-detail.edit', {
            parent: 'licenses-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/licenses/licenses-dialog.html',
                    controller: 'LicensesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Licenses', function(Licenses) {
                            return Licenses.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('licenses.new', {
            parent: 'licenses',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/licenses/licenses-dialog.html',
                    controller: 'LicensesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                contactName: null,
                                contactEmail: null,
                                activationToken: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('licenses', null, { reload: 'licenses' });
                }, function() {
                    $state.go('licenses');
                });
            }]
        })
        .state('licenses.edit', {
            parent: 'licenses',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/licenses/licenses-dialog.html',
                    controller: 'LicensesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Licenses', function(Licenses) {
                            return Licenses.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('licenses', null, { reload: 'licenses' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('licenses.delete', {
            parent: 'licenses',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/licenses/licenses-delete-dialog.html',
                    controller: 'LicensesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Licenses', function(Licenses) {
                            return Licenses.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('licenses', null, { reload: 'licenses' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
