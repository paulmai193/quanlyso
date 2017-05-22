import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { CostFactorComponent } from './cost-factor.component';
import { CostFactorDetailComponent } from './cost-factor-detail.component';
import { CostFactorPopupComponent } from './cost-factor-dialog.component';
import { CostFactorDeletePopupComponent } from './cost-factor-delete-dialog.component';

import { Principal } from '../../shared';

export const costFactorRoute: Routes = [
    {
        path: 'cost-factor',
        component: CostFactorComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.costFactor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'cost-factor/:id',
        component: CostFactorDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.costFactor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const costFactorPopupRoute: Routes = [
    {
        path: 'cost-factor-new',
        component: CostFactorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.costFactor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cost-factor/:id/edit',
        component: CostFactorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.costFactor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cost-factor/:id/delete',
        component: CostFactorDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.costFactor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
