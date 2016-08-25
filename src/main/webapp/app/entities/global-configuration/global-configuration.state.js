(function() {
    'use strict';

    angular
        .module('checklistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('global-configuration', {
            parent: 'entity',
            url: '/global-configuration?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.globalConfiguration.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/global-configuration/global-configurations.html',
                    controller: 'GlobalConfigurationController',
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
                    $translatePartialLoader.addPart('globalConfiguration');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('global-configuration-detail', {
            parent: 'entity',
            url: '/global-configuration/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.globalConfiguration.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/global-configuration/global-configuration-detail.html',
                    controller: 'GlobalConfigurationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('globalConfiguration');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'GlobalConfiguration', function($stateParams, GlobalConfiguration) {
                    return GlobalConfiguration.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'global-configuration',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('global-configuration-detail.edit', {
            parent: 'global-configuration-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/global-configuration/global-configuration-dialog.html',
                    controller: 'GlobalConfigurationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GlobalConfiguration', function(GlobalConfiguration) {
                            return GlobalConfiguration.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('global-configuration.new', {
            parent: 'global-configuration',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/global-configuration/global-configuration-dialog.html',
                    controller: 'GlobalConfigurationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                propertyKey: null,
                                propertyValue: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('global-configuration', null, { reload: 'global-configuration' });
                }, function() {
                    $state.go('global-configuration');
                });
            }]
        })
        .state('global-configuration.edit', {
            parent: 'global-configuration',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/global-configuration/global-configuration-dialog.html',
                    controller: 'GlobalConfigurationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GlobalConfiguration', function(GlobalConfiguration) {
                            return GlobalConfiguration.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('global-configuration', null, { reload: 'global-configuration' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('global-configuration.delete', {
            parent: 'global-configuration',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/global-configuration/global-configuration-delete-dialog.html',
                    controller: 'GlobalConfigurationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['GlobalConfiguration', function(GlobalConfiguration) {
                            return GlobalConfiguration.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('global-configuration', null, { reload: 'global-configuration' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
