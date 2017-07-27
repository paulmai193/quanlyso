import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { StyleComponent } from './style.component';
import { StyleDetailComponent } from './style-detail.component';
import { StylePopupComponent } from './style-dialog.component';
import { StyleDeletePopupComponent } from './style-delete-dialog.component';

import { Principal } from '../../shared';

export const styleRoute: Routes = [
    {
        path: 'style',
        component: StyleComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.style.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'style/:id',
        component: StyleDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.style.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const stylePopupRoute: Routes = [
    {
        path: 'style-new',
        component: StylePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.style.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'style/:id/edit',
        component: StylePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.style.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'style/:id/delete',
        component: StyleDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.style.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
