(function() {
    'use strict';

    angular
        .module('checklistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('engagement-member', {
            parent: 'entity',
            url: '/engagement-member?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.engagementMember.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/engagement-member/engagement-members.html',
                    controller: 'EngagementMemberController',
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
                    $translatePartialLoader.addPart('engagementMember');
                    $translatePartialLoader.addPart('engagementAuthorities');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('engagement-member-detail', {
            parent: 'entity',
            url: '/engagement-member/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.engagementMember.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/engagement-member/engagement-member-detail.html',
                    controller: 'EngagementMemberDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('engagementMember');
                    $translatePartialLoader.addPart('engagementAuthorities');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EngagementMember', function($stateParams, EngagementMember) {
                    return EngagementMember.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'engagement-member',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('engagement-member-detail.edit', {
            parent: 'engagement-member-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/engagement-member/engagement-member-dialog.html',
                    controller: 'EngagementMemberDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EngagementMember', function(EngagementMember) {
                            return EngagementMember.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('engagement-member.new', {
            parent: 'engagement-member',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/engagement-member/engagement-member-dialog.html',
                    controller: 'EngagementMemberDialogController',
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
                    $state.go('engagement-member', null, { reload: 'engagement-member' });
                }, function() {
                    $state.go('engagement-member');
                });
            }]
        })
        .state('engagement-member.edit', {
            parent: 'engagement-member',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/engagement-member/engagement-member-dialog.html',
                    controller: 'EngagementMemberDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EngagementMember', function(EngagementMember) {
                            return EngagementMember.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('engagement-member', null, { reload: 'engagement-member' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('engagement-member.delete', {
            parent: 'engagement-member',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/engagement-member/engagement-member-delete-dialog.html',
                    controller: 'EngagementMemberDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EngagementMember', function(EngagementMember) {
                            return EngagementMember.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('engagement-member', null, { reload: 'engagement-member' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
