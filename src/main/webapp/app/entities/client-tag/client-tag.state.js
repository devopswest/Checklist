(function() {
    'use strict';

    angular
        .module('checklistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('client-tag', {
            parent: 'entity',
            url: '/client-tag?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.clientTag.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/client-tag/client-tags.html',
                    controller: 'ClientTagController',
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
                    $translatePartialLoader.addPart('clientTag');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('client-tag-detail', {
            parent: 'entity',
            url: '/client-tag/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.clientTag.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/client-tag/client-tag-detail.html',
                    controller: 'ClientTagDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('clientTag');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ClientTag', function($stateParams, ClientTag) {
                    return ClientTag.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'client-tag',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('client-tag-detail.edit', {
            parent: 'client-tag-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-tag/client-tag-dialog.html',
                    controller: 'ClientTagDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ClientTag', function(ClientTag) {
                            return ClientTag.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('client-tag.new', {
            parent: 'client-tag',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-tag/client-tag-dialog.html',
                    controller: 'ClientTagDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                tagValue: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('client-tag', null, { reload: 'client-tag' });
                }, function() {
                    $state.go('client-tag');
                });
            }]
        })
        .state('client-tag.edit', {
            parent: 'client-tag',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-tag/client-tag-dialog.html',
                    controller: 'ClientTagDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ClientTag', function(ClientTag) {
                            return ClientTag.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('client-tag', null, { reload: 'client-tag' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('client-tag.delete', {
            parent: 'client-tag',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-tag/client-tag-delete-dialog.html',
                    controller: 'ClientTagDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ClientTag', function(ClientTag) {
                            return ClientTag.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('client-tag', null, { reload: 'client-tag' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
