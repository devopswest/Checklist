(function() {
    'use strict';

    angular
        .module('checklistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('taxonomy', {
            parent: 'entity',
            url: '/taxonomy?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.taxonomy.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/taxonomy/taxonomies.html',
                    controller: 'TaxonomyController',
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
                    $translatePartialLoader.addPart('taxonomy');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('taxonomy-detail', {
            parent: 'entity',
            url: '/taxonomy/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.taxonomy.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/taxonomy/taxonomy-detail.html',
                    controller: 'TaxonomyDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('taxonomy');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Taxonomy', function($stateParams, Taxonomy) {
                    return Taxonomy.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'taxonomy',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('taxonomy-detail.edit', {
            parent: 'taxonomy-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/taxonomy/taxonomy-dialog.html',
                    controller: 'TaxonomyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Taxonomy', function(Taxonomy) {
                            return Taxonomy.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('taxonomy.new', {
            parent: 'taxonomy',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/taxonomy/taxonomy-dialog.html',
                    controller: 'TaxonomyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                label: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('taxonomy', null, { reload: 'taxonomy' });
                }, function() {
                    $state.go('taxonomy');
                });
            }]
        })
        .state('taxonomy.edit', {
            parent: 'taxonomy',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/taxonomy/taxonomy-dialog.html',
                    controller: 'TaxonomyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Taxonomy', function(Taxonomy) {
                            return Taxonomy.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('taxonomy', null, { reload: 'taxonomy' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('taxonomy.delete', {
            parent: 'taxonomy',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/taxonomy/taxonomy-delete-dialog.html',
                    controller: 'TaxonomyDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Taxonomy', function(Taxonomy) {
                            return Taxonomy.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('taxonomy', null, { reload: 'taxonomy' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
