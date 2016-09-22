(function() {
    'use strict';

    angular
        .module('checklistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('disclosure-requirement-tag', {
            parent: 'entity',
            url: '/disclosure-requirement-tag?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.disclosureRequirementTag.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/disclosure-requirement-tag/disclosure-requirement-tags.html',
                    controller: 'DisclosureRequirementTagController',
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
                    $translatePartialLoader.addPart('disclosureRequirementTag');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('disclosure-requirement-tag-detail', {
            parent: 'entity',
            url: '/disclosure-requirement-tag/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.disclosureRequirementTag.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/disclosure-requirement-tag/disclosure-requirement-tag-detail.html',
                    controller: 'DisclosureRequirementTagDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('disclosureRequirementTag');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DisclosureRequirementTag', function($stateParams, DisclosureRequirementTag) {
                    return DisclosureRequirementTag.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'disclosure-requirement-tag',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('disclosure-requirement-tag-detail.edit', {
            parent: 'disclosure-requirement-tag-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/disclosure-requirement-tag/disclosure-requirement-tag-dialog.html',
                    controller: 'DisclosureRequirementTagDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DisclosureRequirementTag', function(DisclosureRequirementTag) {
                            return DisclosureRequirementTag.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('disclosure-requirement-tag.new', {
            parent: 'disclosure-requirement-tag',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/disclosure-requirement-tag/disclosure-requirement-tag-dialog.html',
                    controller: 'DisclosureRequirementTagDialogController',
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
                    $state.go('disclosure-requirement-tag', null, { reload: 'disclosure-requirement-tag' });
                }, function() {
                    $state.go('disclosure-requirement-tag');
                });
            }]
        })
        .state('disclosure-requirement-tag.edit', {
            parent: 'disclosure-requirement-tag',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/disclosure-requirement-tag/disclosure-requirement-tag-dialog.html',
                    controller: 'DisclosureRequirementTagDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DisclosureRequirementTag', function(DisclosureRequirementTag) {
                            return DisclosureRequirementTag.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('disclosure-requirement-tag', null, { reload: 'disclosure-requirement-tag' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('disclosure-requirement-tag.delete', {
            parent: 'disclosure-requirement-tag',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/disclosure-requirement-tag/disclosure-requirement-tag-delete-dialog.html',
                    controller: 'DisclosureRequirementTagDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DisclosureRequirementTag', function(DisclosureRequirementTag) {
                            return DisclosureRequirementTag.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('disclosure-requirement-tag', null, { reload: 'disclosure-requirement-tag' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
