(function() {
    'use strict';

    angular
        .module('checklistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('client-metadata', {
            parent: 'entity',
            url: '/client-metadata?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.clientMetadata.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/client-metadata/client-metadata.html',
                    controller: 'ClientMetadataController',
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
                    $translatePartialLoader.addPart('clientMetadata');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('client-metadata-detail', {
            parent: 'entity',
            url: '/client-metadata/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.clientMetadata.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/client-metadata/client-metadata-detail.html',
                    controller: 'ClientMetadataDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('clientMetadata');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ClientMetadata', function($stateParams, ClientMetadata) {
                    return ClientMetadata.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'client-metadata',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('client-metadata-detail.edit', {
            parent: 'client-metadata-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-metadata/client-metadata-dialog.html',
                    controller: 'ClientMetadataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ClientMetadata', function(ClientMetadata) {
                            return ClientMetadata.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('client-metadata.new', {
            parent: 'client-metadata',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-metadata/client-metadata-dialog.html',
                    controller: 'ClientMetadataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                metadataValue: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('client-metadata', null, { reload: 'client-metadata' });
                }, function() {
                    $state.go('client-metadata');
                });
            }]
        })
        .state('client-metadata.edit', {
            parent: 'client-metadata',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-metadata/client-metadata-dialog.html',
                    controller: 'ClientMetadataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ClientMetadata', function(ClientMetadata) {
                            return ClientMetadata.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('client-metadata', null, { reload: 'client-metadata' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('client-metadata.delete', {
            parent: 'client-metadata',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-metadata/client-metadata-delete-dialog.html',
                    controller: 'ClientMetadataDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ClientMetadata', function(ClientMetadata) {
                            return ClientMetadata.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('client-metadata', null, { reload: 'client-metadata' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
