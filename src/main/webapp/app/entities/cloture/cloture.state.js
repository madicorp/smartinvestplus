(function() {
    'use strict';

    angular
        .module('smartinvestplusApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cloture', {
            parent: 'entity',
            url: '/cloture?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartinvestplusApp.cloture.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cloture/clotures.html',
                    controller: 'ClotureController',
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
                    $translatePartialLoader.addPart('cloture');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('cloture-detail', {
            parent: 'entity',
            url: '/cloture/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartinvestplusApp.cloture.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cloture/cloture-detail.html',
                    controller: 'ClotureDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cloture');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Cloture', function($stateParams, Cloture) {
                    return Cloture.get({id : $stateParams.id});
                }]
            }
        })
        .state('cloture.new', {
            parent: 'cloture',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cloture/cloture-dialog.html',
                    controller: 'ClotureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                volume: null,
                                valeur: null,
                                transactions: null,
                                cours: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cloture', null, { reload: true });
                }, function() {
                    $state.go('cloture');
                });
            }]
        })
        .state('cloture.edit', {
            parent: 'cloture',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cloture/cloture-dialog.html',
                    controller: 'ClotureDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cloture', function(Cloture) {
                            return Cloture.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('cloture', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cloture.delete', {
            parent: 'cloture',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cloture/cloture-delete-dialog.html',
                    controller: 'ClotureDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Cloture', function(Cloture) {
                            return Cloture.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('cloture', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
