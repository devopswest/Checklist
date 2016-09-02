(function() {
    'use strict';

    angular
        .module('checklistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('engagement', {
            parent: 'entity',
            url: '/engagement?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.engagement.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/engagement/engagements.html',
                    controller: 'EngagementController',
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
                    $translatePartialLoader.addPart('engagement');
                    $translatePartialLoader.addPart('responseStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('engagement-detail', {
            parent: 'entity',
            url: '/engagement/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.engagement.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/engagement/engagement-detail.html',
                    controller: 'EngagementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('engagement');
                    $translatePartialLoader.addPart('responseStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Engagement', function($stateParams, Engagement) {
                    return Engagement.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'engagement',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('engagement-detail.edit', {
            parent: 'engagement-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/engagement/engagement-dialog.html',
                    controller: 'EngagementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Engagement', function(Engagement) {
                            return Engagement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('engagement.new', {
            parent: 'engagement',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/engagement/engagement-dialog.html',
                    controller: 'EngagementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fiscalYear: null,
                                description: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('engagement', null, { reload: 'engagement' });
                }, function() {
                    $state.go('engagement');
                });
            }]
        })
        .state('engagement.edit', {
            parent: 'engagement',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/engagement/engagement-dialog.html',
                    controller: 'EngagementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Engagement', function(Engagement) {
                            return Engagement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('engagement', null, { reload: 'engagement' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('engagement.delete', {
            parent: 'engagement',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/engagement/engagement-delete-dialog.html',
                    controller: 'EngagementDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Engagement', function(Engagement) {
                            return Engagement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('engagement', null, { reload: 'engagement' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
