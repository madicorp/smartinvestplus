(function() {
    'use strict';

    angular
        .module('smartinvestplusApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bourse', {
            parent: 'entity',
            url: '/bourse?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartinvestplusApp.bourse.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bourse/bourses.html',
                    controller: 'BourseController',
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
                    $translatePartialLoader.addPart('bourse');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('bourse-detail', {
            parent: 'entity',
            url: '/bourse/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartinvestplusApp.bourse.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bourse/bourse-detail.html',
                    controller: 'BourseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bourse');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Bourse', function($stateParams, Bourse) {
                    return Bourse.get({id : $stateParams.id});
                }]
            }
        })
        .state('bourse.new', {
            parent: 'bourse',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bourse/bourse-dialog.html',
                    controller: 'BourseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nom: null,
                                symbole: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('bourse', null, { reload: true });
                }, function() {
                    $state.go('bourse');
                });
            }]
        })
        .state('bourse.edit', {
            parent: 'bourse',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bourse/bourse-dialog.html',
                    controller: 'BourseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bourse', function(Bourse) {
                            return Bourse.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('bourse', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bourse.delete', {
            parent: 'bourse',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bourse/bourse-delete-dialog.html',
                    controller: 'BourseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Bourse', function(Bourse) {
                            return Bourse.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('bourse', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
