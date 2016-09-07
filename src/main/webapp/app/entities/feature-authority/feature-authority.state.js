(function() {
    'use strict';

    angular
        .module('checklistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('feature-authority', {
            parent: 'entity',
            url: '/feature-authority?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.featureAuthority.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/feature-authority/feature-authorities.html',
                    controller: 'FeatureAuthorityController',
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
                    $translatePartialLoader.addPart('featureAuthority');
                    $translatePartialLoader.addPart('applicationAuthorities');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('feature-authority-detail', {
            parent: 'entity',
            url: '/feature-authority/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.featureAuthority.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/feature-authority/feature-authority-detail.html',
                    controller: 'FeatureAuthorityDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('featureAuthority');
                    $translatePartialLoader.addPart('applicationAuthorities');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FeatureAuthority', function($stateParams, FeatureAuthority) {
                    return FeatureAuthority.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'feature-authority',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('feature-authority-detail.edit', {
            parent: 'feature-authority-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/feature-authority/feature-authority-dialog.html',
                    controller: 'FeatureAuthorityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FeatureAuthority', function(FeatureAuthority) {
                            return FeatureAuthority.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('feature-authority.new', {
            parent: 'feature-authority',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/feature-authority/feature-authority-dialog.html',
                    controller: 'FeatureAuthorityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                authority: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('feature-authority', null, { reload: 'feature-authority' });
                }, function() {
                    $state.go('feature-authority');
                });
            }]
        })
        .state('feature-authority.edit', {
            parent: 'feature-authority',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/feature-authority/feature-authority-dialog.html',
                    controller: 'FeatureAuthorityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FeatureAuthority', function(FeatureAuthority) {
                            return FeatureAuthority.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('feature-authority', null, { reload: 'feature-authority' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('feature-authority.delete', {
            parent: 'feature-authority',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/feature-authority/feature-authority-delete-dialog.html',
                    controller: 'FeatureAuthorityDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FeatureAuthority', function(FeatureAuthority) {
                            return FeatureAuthority.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('feature-authority', null, { reload: 'feature-authority' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
