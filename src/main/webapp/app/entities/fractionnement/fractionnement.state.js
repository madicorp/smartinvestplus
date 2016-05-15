(function() {
    'use strict';

    angular
        .module('smartinvestplusApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('fractionnement', {
            parent: 'entity',
            url: '/fractionnement',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartinvestplusApp.fractionnement.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/fractionnement/fractionnements.html',
                    controller: 'FractionnementController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fractionnement');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('fractionnement-detail', {
            parent: 'entity',
            url: '/fractionnement/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartinvestplusApp.fractionnement.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/fractionnement/fractionnement-detail.html',
                    controller: 'FractionnementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('fractionnement');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Fractionnement', function($stateParams, Fractionnement) {
                    return Fractionnement.get({id : $stateParams.id});
                }]
            }
        })
        .state('fractionnement.new', {
            parent: 'fractionnement',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fractionnement/fractionnement-dialog.html',
                    controller: 'FractionnementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                valeur: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('fractionnement', null, { reload: true });
                }, function() {
                    $state.go('fractionnement');
                });
            }]
        })
        .state('fractionnement.edit', {
            parent: 'fractionnement',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fractionnement/fractionnement-dialog.html',
                    controller: 'FractionnementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Fractionnement', function(Fractionnement) {
                            return Fractionnement.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('fractionnement', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('fractionnement.delete', {
            parent: 'fractionnement',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/fractionnement/fractionnement-delete-dialog.html',
                    controller: 'FractionnementDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Fractionnement', function(Fractionnement) {
                            return Fractionnement.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('fractionnement', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
