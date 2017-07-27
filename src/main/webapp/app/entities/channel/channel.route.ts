import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { ChannelComponent } from './channel.component';
import { ChannelDetailComponent } from './channel-detail.component';
import { ChannelPopupComponent } from './channel-dialog.component';
import { ChannelDeletePopupComponent } from './channel-delete-dialog.component';

import { Principal } from '../../shared';

export const channelRoute: Routes = [
    {
        path: 'channel',
        component: ChannelComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.channel.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'channel/:id',
        component: ChannelDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.channel.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const channelPopupRoute: Routes = [
    {
        path: 'channel-new',
        component: ChannelPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.channel.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'channel/:id/edit',
        component: ChannelPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.channel.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'channel/:id/delete',
        component: ChannelDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.channel.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
