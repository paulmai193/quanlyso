import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2Webstorage } from 'ng2-webstorage';

import { QuanlysoSharedModule, UserRouteAccessService } from './shared';
import { QuanlysoHomeModule } from './home/home.module';
import { QuanlysoAdminModule } from './admin/admin.module';
import { QuanlysoAccountModule } from './account/account.module';
import { QuanlysoEntityModule } from './entities/entity.module';

import { customHttpProvider } from './blocks/interceptor/http.provider';
import { PaginationConfig } from './blocks/config/uib-pagination.config';

import {
    JhiMainComponent,
    LayoutRoutingModule,
    NavbarComponent,
    FooterComponent,
    ProfileService,
    PageRibbonComponent,
    ActiveMenuDirective,
    ErrorComponent
} from './layouts';
import { QuanLySoToolModule } from './tool/tool.module';

@NgModule({
    imports: [
        BrowserModule,
        LayoutRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        QuanlysoSharedModule,
        QuanlysoHomeModule,
        QuanlysoAdminModule,
        QuanlysoAccountModule,
        QuanlysoEntityModule,
        QuanLySoToolModule,
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        ActiveMenuDirective,
        FooterComponent
    ],
    providers: [
        ProfileService,
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService
    ],
    bootstrap: [ JhiMainComponent ]
})
export class QuanlysoAppModule {}
