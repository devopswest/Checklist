(function() {
    'use strict';

    angular
        .module('checklistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('disclosure-requirement', {
            parent: 'entity',
            url: '/disclosure-requirement?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.disclosureRequirement.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/disclosure-requirement/disclosure-requirements.html',
                    controller: 'DisclosureRequirementController',
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
                    $translatePartialLoader.addPart('disclosureRequirement');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('disclosure-requirement-detail', {
            parent: 'entity',
            url: '/disclosure-requirement/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.disclosureRequirement.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/disclosure-requirement/disclosure-requirement-detail.html',
                    controller: 'DisclosureRequirementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('disclosureRequirement');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DisclosureRequirement', function($stateParams, DisclosureRequirement) {
                    return DisclosureRequirement.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'disclosure-requirement',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('disclosure-requirement-detail.edit', {
            parent: 'disclosure-requirement-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/disclosure-requirement/disclosure-requirement-dialog.html',
                    controller: 'DisclosureRequirementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DisclosureRequirement', function(DisclosureRequirement) {
                            return DisclosureRequirement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('disclosure-requirement.new', {
            parent: 'disclosure-requirement',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/disclosure-requirement/disclosure-requirement-dialog.html',
                    controller: 'DisclosureRequirementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('disclosure-requirement', null, { reload: 'disclosure-requirement' });
                }, function() {
                    $state.go('disclosure-requirement');
                });
            }]
        })
        .state('disclosure-requirement.edit', {
            parent: 'disclosure-requirement',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/disclosure-requirement/disclosure-requirement-dialog.html',
                    controller: 'DisclosureRequirementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DisclosureRequirement', function(DisclosureRequirement) {
                            return DisclosureRequirement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('disclosure-requirement', null, { reload: 'disclosure-requirement' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('disclosure-requirement.delete', {
            parent: 'disclosure-requirement',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/disclosure-requirement/disclosure-requirement-delete-dialog.html',
                    controller: 'DisclosureRequirementDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DisclosureRequirement', function(DisclosureRequirement) {
                            return DisclosureRequirement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('disclosure-requirement', null, { reload: 'disclosure-requirement' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
