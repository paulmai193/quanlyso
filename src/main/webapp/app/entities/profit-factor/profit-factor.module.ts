import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { QuanlysoSharedModule } from '../../shared';
import {
    ProfitFactorService,
    ProfitFactorPopupService,
    ProfitFactorComponent,
    ProfitFactorDetailComponent,
    ProfitFactorDialogComponent,
    ProfitFactorPopupComponent,
    ProfitFactorDeletePopupComponent,
    ProfitFactorDeleteDialogComponent,
    profitFactorRoute,
    profitFactorPopupRoute,
} from './';

const ENTITY_STATES = [
    ...profitFactorRoute,
    ...profitFactorPopupRoute,
];

@NgModule({
    imports: [
        QuanlysoSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ProfitFactorComponent,
        ProfitFactorDetailComponent,
        ProfitFactorDialogComponent,
        ProfitFactorDeleteDialogComponent,
        ProfitFactorPopupComponent,
        ProfitFactorDeletePopupComponent,
    ],
    entryComponents: [
        ProfitFactorComponent,
        ProfitFactorDialogComponent,
        ProfitFactorPopupComponent,
        ProfitFactorDeleteDialogComponent,
        ProfitFactorDeletePopupComponent,
    ],
    providers: [
        ProfitFactorService,
        ProfitFactorPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class QuanlysoProfitFactorModule {}
